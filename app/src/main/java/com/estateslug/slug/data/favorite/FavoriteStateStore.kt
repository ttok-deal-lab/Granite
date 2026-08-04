package com.estateslug.slug.data.favorite

import android.content.Context
import android.widget.Toast
import com.estateslug.slug.R
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.user.RemoteUserDataRepository
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.util.CursorPaginationState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 관심(찜) 상태의 단일 진실 공급원.
 *
 * 모든 관심 쓰기는 [setFavorite] 하나로 수렴한다: 오버레이 선반영(낙관적) → 서버 호출 →
 * 실패 시 롤백. 목록 화면들은 자신의 페이지네이션 상태(서버 진실)를 그대로 두고
 * [overrides]를 방출 시점에 combine으로 병합하므로, 어느 화면에서 토글하든
 * 홈·관심탭·상세가 refetch 없이 즉시 일치한다. (Activity/NavHost/2-pane 어느 호스트든 동일)
 *
 * - [overrides]: id → 사용자가 마지막으로 의도한 상태. 서버가 같은 값을 확인하면 축출됨.
 * - [changes]: 관심탭처럼 "목록 멤버십" 자체가 바뀌어야 하는 소비자용 이벤트(행 추가/제거).
 * - 매물별 쓰기는 직렬화되고, 연타로 의도가 상쇄되면(서버 상태 == 최종 의도) 전송 자체를 생략한다.
 */
@Singleton
class FavoriteStateStore @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    @ApplicationContext private val appContext: Context,
    private val remoteUserDataRepository: RemoteUserDataRepository,
    private val localUserDataRepository: LocalUserDataRepository,
) {
    private val lock = Any()

    private val _overrides = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val overrides: StateFlow<Map<String, Boolean>> = _overrides.asStateFlow()

    private val _changes = MutableSharedFlow<FavoriteChange>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val changes: SharedFlow<FavoriteChange> = _changes.asSharedFlow()

    /** 마지막으로 확인된 서버 상태 — 불필요한 전송 생략과 오버레이 축출 판단에 사용 */
    private val serverStates = mutableMapOf<String, Boolean>()
    private val writeJobs = mutableMapOf<String, Job>()

    fun setFavorite(productId: String, isFavorite: Boolean, snapshot: ProductItemUiModel? = null) {
        if (productId.isBlank()) return
        _overrides.update { it + (productId to isFavorite) }
        _changes.tryEmit(FavoriteChange(productId, isFavorite, snapshot))
        synchronized(lock) {
            val previous = writeJobs[productId]
            val job = appScope.launch {
                previous?.join()
                // 더 새로운 의도로 대체되었으면 이 요청은 보내지 않는다
                if (_overrides.value[productId] != isFavorite) return@launch
                // 서버가 이미 원하는 상태면(연타 상쇄 포함) 전송 생략
                if (synchronized(lock) { serverStates[productId] } == isFavorite) return@launch

                // 롤백은 이 잡이 여전히 "마지막 의도"일 때만 — 오래된 쓰기의 실패가
                // 더 새로운 탭(같은 값 포함)을 되돌리면 최종 의도가 유실된다
                val self = coroutineContext[Job]
                val isLatestIntent = { synchronized(lock) { writeJobs[productId] === self } }

                val userId = localUserDataRepository.getUserId().getOrNull()
                if (userId == null) {
                    if (isLatestIntent()) rollback(productId, isFavorite)
                    return@launch
                }
                val result =
                    if (isFavorite) remoteUserDataRepository.addProductFavorite(userId, productId)
                    else remoteUserDataRepository.removeProductFavorite(userId, productId)
                // clear()로 취소된 잡이 롤백으로 오버레이를 재오염시키지 않도록
                // (repository의 runCatching이 CancellationException을 Result.failure로 삼킨다)
                coroutineContext.ensureActive()
                result
                    .onSuccess { synchronized(lock) { serverStates[productId] = isFavorite } }
                    .onFailure { if (isLatestIntent()) rollback(productId, isFavorite) }
            }
            writeJobs[productId] = job
            job.invokeOnCompletion {
                synchronized(lock) { if (writeJobs[productId] === job) writeJobs.remove(productId) }
            }
        }
    }

    /**
     * 목록/상세 fetch가 확인한 서버 상태 보고.
     * 서버가 오버레이와 같은 값을 확인했고 진행 중인 쓰기가 없으면 해당 엔트리를 축출한다
     * (맵 무한 성장·원격 변경 마스킹 방지).
     */
    fun onServerStateObserved(observed: Map<String, Boolean>) {
        if (observed.isEmpty()) return
        synchronized(lock) {
            // 진행 중 쓰기가 있거나(값이 곧 바뀜), 오버레이와 어긋나는 관측(쓰기 완료 직후 도착한
            // stale 응답)은 serverStates를 되감아 필요한 쓰기를 생략시키므로 버린다
            val trusted = observed.filter { (id, value) ->
                writeJobs[id] == null && (_overrides.value[id] ?: value) == value
            }
            serverStates.putAll(trusted)
        }
        _overrides.update { current ->
            if (current.isEmpty()) current
            else current.filterNot { (id, value) ->
                observed[id] == value && synchronized(lock) { writeJobs[id] == null }
            }
        }
    }

    /** 로그아웃·탈퇴·세션만료 시 호출 — 다음 세션(다른 계정) 오염 방지 */
    fun clear() {
        synchronized(lock) {
            writeJobs.values.forEach { it.cancel() }
            writeJobs.clear()
            serverStates.clear()
        }
        _overrides.value = emptyMap()
    }

    private suspend fun rollback(productId: String, attempted: Boolean) {
        _overrides.update { it + (productId to !attempted) }
        _changes.tryEmit(FavoriteChange(productId, !attempted, snapshot = null))
        withContext(Dispatchers.Main) {
            Toast.makeText(appContext, R.string.favorite_action_failed_toast, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

data class FavoriteChange(
    val productId: String,
    val isFavorite: Boolean,
    /** 관심탭 prepend용 목록 아이템 스냅샷 — 상세에서 등록할 때 제공, 없으면 소비 측이 폴백 처리 */
    val snapshot: ProductItemUiModel?,
)

fun ProductItemUiModel.withFavoriteOverride(override: Boolean?): ProductItemUiModel =
    if (override == null || override == isFavorite) this
    else copy(
        isFavorite = override,
        favoritePersons = (favoritePersons + if (override) 1 else -1).coerceAtLeast(0L),
    )

fun CursorPaginationState<ProductItemUiModel>.withFavoriteOverrides(
    overrides: Map<String, Boolean>,
): CursorPaginationState<ProductItemUiModel> =
    if (overrides.isEmpty()) this
    else copy(items = items.map { it.withFavoriteOverride(overrides[it.id]) })
