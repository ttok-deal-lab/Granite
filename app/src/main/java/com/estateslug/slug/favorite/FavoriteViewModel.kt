package com.estateslug.slug.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estateslug.slug.data.favorite.FavoriteChange
import com.estateslug.slug.data.favorite.FavoriteStateStore
import com.estateslug.slug.data.favorite.withFavoriteOverrides
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.user.RemoteUserDataRepository
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.home.ProductItemUiModel.Companion.toUiModel
import com.estateslug.slug.util.CursorPage
import com.estateslug.slug.util.CursorPaginationState
import com.estateslug.slug.util.CursorPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    val remoteUserDataRepository: RemoteUserDataRepository,
    val localUserDataRepository: LocalUserDataRepository,
    private val favoriteStateStore: FavoriteStateStore,
) : ViewModel() {
    val userId: String = runBlocking {
        localUserDataRepository.getUserId().getOrNull() ?: ""
        //TODO : 에러 처리하던가 useCase로 보내던가.
    }
    private val _paginationState = MutableStateFlow(CursorPaginationState<ProductItemUiModel>())
    val paginationState: StateFlow<CursorPaginationState<ProductItemUiModel>> =
        combine(_paginationState, favoriteStateStore.overrides) { state, overrides ->
            state.withFavoriteOverrides(overrides)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _paginationState.value)

    private val paginator = CursorPaginator(
        state = _paginationState,
        itemKey = { it.id },
        fetchPage = { cursor ->
            val page = remoteUserDataRepository.getFavoriteProductsByCursor(
                userId = userId,
                cursor = cursor,
            )
            val items =
                page.items.map { it.toUiModel().copy(isFavorite = true)/*Favorite 목록이기에 항상 true*/ }
            favoriteStateStore.onServerStateObserved(items.associate { it.id to true })
            CursorPage(
                items = items,
                nextCursor = page.nextCursor,
                totalCount = page.totalCount,
            )
        }
    )

    /** 해제 이벤트로 제거한 행 캐시 — 재등록 시 스냅샷 없이도 복원 가능 */
    private val removedItemCache = mutableMapOf<String, ProductItemUiModel>()
    private var needsRefresh = false

    init {
        viewModelScope.launch {
            paginator.loadInitial()
            // VM 생성 전에 발생했던 변경(replay 없는 SharedFlow라 유실됨)과
            // 아직 서버에 커밋되지 않은 쓰기를 오버레이 기준으로 보정
            reconcileWithOverrides()
        }
        viewModelScope.launch { favoriteStateStore.changes.collect(::onFavoriteChange) }
    }

    /** 페이지 로드가 진행 중 쓰기와 레이스한 경우 오버레이 기준으로 목록 멤버십을 보정한다 */
    private fun reconcileWithOverrides() {
        val overrides = favoriteStateStore.overrides.value
        if (overrides.isEmpty()) return
        overrides.forEach { (id, isFavorite) ->
            val items = _paginationState.value.items
            if (isFavorite && items.none { it.id == id }) {
                val cached = removedItemCache.remove(id)
                if (cached != null) paginator.prependItem(cached.copy(isFavorite = true))
                else needsRefresh = true
            } else if (!isFavorite && items.any { it.id == id }) {
                items.find { it.id == id }?.let { removedItemCache[id] = it }
                paginator.removeItem { it.id == id }
            }
        }
    }

    private fun onFavoriteChange(change: FavoriteChange) {
        if (change.isFavorite) {
            if (_paginationState.value.items.any { it.id == change.productId }) return
            val item = change.snapshot ?: removedItemCache.remove(change.productId)
            if (item != null) {
                // 서버 정렬이 최신 등록순이므로 최상단 삽입이 정합
                paginator.prependItem(item.copy(isFavorite = true))
            } else {
                // 스냅샷 없이 등록됨 — 다음 화면 복귀 시 1회 리프레시로 반영
                needsRefresh = true
            }
        } else {
            _paginationState.value.items.find { it.id == change.productId }
                ?.let { removedItemCache[change.productId] = it }
            paginator.removeItem { it.id == change.productId }
        }
    }

    /** 스냅샷 폴백이 필요했던 경우에만 1회 리프레시 (기존 무조건 ON_RESUME refresh 대체) */
    fun consumePendingRefresh() {
        if (!needsRefresh) return
        needsRefresh = false // 연속 ON_RESUME의 이중 실행 방지 — 실패 시 아래에서 복원
        viewModelScope.launch {
            if (paginator.refresh()) reconcileWithOverrides()
            else needsRefresh = true // 오프라인 등 실패 시 다음 복귀에서 재시도
        }
    }

    fun refresh() {
        viewModelScope.launch {
            if (paginator.refresh()) reconcileWithOverrides()
        }
    }

    fun loadMore() {
        viewModelScope.launch { paginator.loadMore() }
    }

    fun removeFavorite(productId: String) {
        // 단일 쓰기 경로(FavoriteStateStore)로 위임 — 행 제거는 changes 이벤트가 처리
        favoriteStateStore.setFavorite(productId, isFavorite = false)
    }
}
