package com.estateslug.slug.data.favorite

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.user.RemoteUserDataRepository
import com.estateslug.slug.data.network.user.UserPrivateService
import com.estateslug.slug.data.network.user.UserPublicService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Retrofit 서비스 인터페이스만 mock하고 repository는 실제 인스턴스를 쓴다 —
 * kotlin.Result 반환 함수는 value class 맹글링 때문에 mockk로 직접 스텁하면 깨진다.
 * Result 생성(runCatching)이 실제 프로덕션 코드에서 일어나므로
 * CancellationException 삼킴 같은 실제 동작까지 그대로 검증된다.
 */
class FavoriteStateStoreTest {

    private val privateService = mockk<UserPrivateService>()
    private val publicService = mockk<UserPublicService>()
    private val remote = RemoteUserDataRepository(publicService, privateService)

    private val userDataStore = FakePreferencesDataStore()
    private val local = LocalUserDataRepository(userDataStore)

    private val notifier = RecordingNotifier()

    private class RecordingNotifier : FavoriteErrorNotifier {
        var failureCount = 0
        override suspend fun notifyFavoriteActionFailed() {
            failureCount++
        }
    }

    private class FakePreferencesDataStore : DataStore<Preferences> {
        private val state = MutableStateFlow(emptyPreferences())
        override val data: Flow<Preferences> = state
        override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
            state.value = transform(state.value)
            return state.value
        }
    }

    @Before
    fun setUp() {
        runBlocking { local.setUserId(USER) }
    }

    private fun TestScope.createStore(
        localRepository: LocalUserDataRepository = local,
    ) = FavoriteStateStore(
        appScope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler)),
        errorNotifier = notifier,
        remoteUserDataRepository = remote,
        localUserDataRepository = localRepository,
    )

    @Test
    fun `토글은 오버레이를 선반영하고 멤버십 이벤트를 방출한다`() = runTest {
        coEvery { privateService.requestAddFavoriteProduct(USER, ID, any()) } returns "ok"
        val store = createStore()
        val events = mutableListOf<FavoriteChange>()
        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            store.changes.toList(events)
        }

        store.setFavorite(ID, isFavorite = true)

        // 서버 응답 전에 이미 반영(낙관적)
        assertEquals(mapOf(ID to true), store.overrides.value)
        advanceUntilIdle()
        assertEquals(1, events.size)
        assertTrue(events.first().isFavorite)
        coVerify(exactly = 1) { privateService.requestAddFavoriteProduct(USER, ID, any()) }
        collector.cancel()
    }

    @Test
    fun `서버가 이미 원하는 상태면 같은 값 재요청은 전송을 생략한다`() = runTest {
        coEvery { privateService.requestAddFavoriteProduct(USER, ID, any()) } returns "ok"
        val store = createStore()

        store.setFavorite(ID, isFavorite = true)
        advanceUntilIdle()
        store.setFavorite(ID, isFavorite = true)
        advanceUntilIdle()

        coVerify(exactly = 1) { privateService.requestAddFavoriteProduct(USER, ID, any()) }
    }

    @Test
    fun `연타로 의도가 상쇄되면(ON-OFF) 아무 요청도 보내지 않는다`() = runTest {
        val store = createStore()
        store.onServerStateObserved(mapOf(ID to false))

        store.setFavorite(ID, isFavorite = true)
        store.setFavorite(ID, isFavorite = false)
        advanceUntilIdle()

        coVerify(exactly = 0) { privateService.requestAddFavoriteProduct(any(), any(), any()) }
        coVerify(exactly = 0) { privateService.requestRemoveFavoriteProduct(any(), any(), any()) }
    }

    @Test
    fun `실패하면 오버레이를 롤백하고 역방향 이벤트와 실패 알림을 낸다`() = runTest {
        coEvery {
            privateService.requestAddFavoriteProduct(USER, ID, any())
        } throws RuntimeException("boom")
        val store = createStore()
        val events = mutableListOf<FavoriteChange>()
        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            store.changes.toList(events)
        }

        store.setFavorite(ID, isFavorite = true)
        advanceUntilIdle()

        assertEquals(mapOf(ID to false), store.overrides.value)
        assertFalse(events.last().isFavorite) // 역방향 이벤트
        assertEquals(1, notifier.failureCount)
        collector.cancel()
    }

    @Test
    fun `오래된 쓰기의 실패는 더 새로운 의도를 롤백하지 않는다`() = runTest {
        var addCalls = 0
        coEvery { privateService.requestAddFavoriteProduct(USER, ID, any()) } coAnswers {
            addCalls++
            if (addCalls == 1) {
                delay(100) // 첫 ON 쓰기는 느리게 실패
                throw RuntimeException("slow failure")
            }
            "ok"
        }
        val store = createStore()
        store.onServerStateObserved(mapOf(ID to false))

        store.setFavorite(ID, isFavorite = true) // A: 느리게 실패할 쓰기
        runCurrent() // A 시작(delay 대기)
        store.setFavorite(ID, isFavorite = false) // B: 대기
        store.setFavorite(ID, isFavorite = true) // C: 최종 의도
        advanceUntilIdle()

        // A의 실패가 C의 의도(ON)를 되돌리지 않고, C가 실제로 전송된다
        assertEquals(mapOf(ID to true), store.overrides.value)
        assertEquals(0, notifier.failureCount)
        assertEquals(2, addCalls)
        coVerify(exactly = 0) { privateService.requestRemoveFavoriteProduct(any(), any(), any()) }
    }

    @Test
    fun `stale 관측은 serverStates를 되감지 않고, 일치 관측만 오버레이를 축출한다`() = runTest {
        coEvery { privateService.requestAddFavoriteProduct(USER, ID, any()) } returns "ok"
        coEvery { privateService.requestRemoveFavoriteProduct(USER, ID, any()) } returns "ok"
        val store = createStore()

        store.setFavorite(ID, isFavorite = true)
        advanceUntilIdle() // 서버 반영 완료, 오버레이(ID→true) 잔존

        // 쓰기 완료 전 시점의 stale 응답(false) 도착 — 오버레이와 어긋나므로 무시되어야 한다
        store.onServerStateObserved(mapOf(ID to false))
        assertEquals(mapOf(ID to true), store.overrides.value) // 축출 안 됨

        // serverStates가 되감기지 않았으므로 OFF 토글은 실제로 전송된다
        store.setFavorite(ID, isFavorite = false)
        advanceUntilIdle()
        coVerify(exactly = 1) { privateService.requestRemoveFavoriteProduct(USER, ID, any()) }

        // 일치 관측 → 축출
        store.onServerStateObserved(mapOf(ID to false))
        assertTrue(store.overrides.value.isEmpty())
    }

    @Test
    fun `clear로 취소된 쓰기는 롤백으로 오버레이를 재오염하지 않는다`() = runTest {
        // 실제 RemoteUserDataRepository의 runCatching이 취소 시 CancellationException을
        // Result.failure로 삼키는 경로 그대로 — store의 ensureActive 가드가 롤백을 막아야 한다
        coEvery { privateService.requestAddFavoriteProduct(USER, ID, any()) } coAnswers {
            delay(1_000)
            "never"
        }
        val store = createStore()

        store.setFavorite(ID, isFavorite = true)
        runCurrent() // 쓰기 시작(delay 대기)
        store.clear()
        advanceUntilIdle()

        assertTrue(store.overrides.value.isEmpty())
        assertEquals(0, notifier.failureCount)
    }

    @Test
    fun `userId가 없으면 롤백하고 실패를 알린다`() = runTest {
        val emptyLocal = LocalUserDataRepository(FakePreferencesDataStore())
        val store = createStore(localRepository = emptyLocal)

        store.setFavorite(ID, isFavorite = true)
        advanceUntilIdle()

        assertEquals(mapOf(ID to false), store.overrides.value)
        assertEquals(1, notifier.failureCount)
        coVerify(exactly = 0) { privateService.requestAddFavoriteProduct(any(), any(), any()) }
    }

    companion object {
        private const val USER = "user-1"
        private const val ID = "product-1"
    }
}
