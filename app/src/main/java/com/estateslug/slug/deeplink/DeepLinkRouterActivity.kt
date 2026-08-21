package com.estateslug.slug.deeplink

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.TaskStackBuilder
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.login.LogInActivity
import com.estateslug.slug.main.MainActivity
import com.estateslug.slug.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * 모든 딥링크(외부 App Links + FCM 푸시)의 단일 진입점.
 *
 * 흐름: intent.data → [DeepLinkResolver] → (인증 게이팅) → [TaskStackBuilder]로 백스택 합성.
 * UI가 없으므로 즉시 finish 한다. (Translucent 테마)
 *
 * 추후 NavHost 통합 시 이 라우터는 navController.handleDeepLink 로 축소된다.
 */
@AndroidEntryPoint
class DeepLinkRouterActivity : ComponentActivity() {

    @Inject
    lateinit var localUserDataRepository: LocalUserDataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이 액티비티가 태스크 루트라 recents(최근 앱)에서 죽은 태스크를 되살리면
        // base intent(원본 딥링크)가 그대로 재전달된다(FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY 세팅).
        // 그때 원 목적지로 다시 dispatch하면 종료했던 상세가 재진입하므로 uri를 버리고 홈으로 보낸다
        val isFromHistory =
            intent != null && (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0
        val uri = if (isFromHistory) null else intent?.data
        val destination = DeepLinkResolver.resolve(uri)

        val token = runBlocking { localUserDataRepository.getUserAccessToken().getOrNull() }
        if (token.isNullOrBlank()) {
            // 미인증 → 로그인 후 원 목적지로 이어보내기
            startActivity(
                Intent(this, LogInActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    uri?.let { putExtra(DeepLinkKeys.PENDING_DEEPLINK, it.toString()) }
                }
            )
            finish()
            return
        }

        dispatch(destination)
        finish()
    }

    private fun dispatch(destination: DeepLinkDestination) {
        val builder = TaskStackBuilder.create(this)
        when (destination) {
            is DeepLinkDestination.Detail -> {
                // 상세는 MainActivity NavHost의 destination이므로 Main 단일 인텐트에 id를 실어 보낸다
                // (TaskStackBuilder 유지 → task-rebuild 시맨틱과 onCreate 보장은 기존과 동일)
                builder.addNextIntent(
                    mainIntent(DeepLinkTab.HOME)
                        .putExtra(DeepLinkKeys.DETAIL_ID, destination.id)
                )
            }

            is DeepLinkDestination.Search -> {
                builder.addNextIntent(mainIntent(DeepLinkTab.HOME))
                builder.addNextIntent(
                    Intent(this, SearchActivity::class.java)
                        .putExtra(DeepLinkKeys.SEARCH_KEYWORD, destination.keyword)
                )
            }

            is DeepLinkDestination.Tab -> builder.addNextIntent(mainIntent(destination.tab))

            DeepLinkDestination.Home -> builder.addNextIntent(mainIntent(DeepLinkTab.HOME))
        }
        builder.startActivities()
    }

    private fun mainIntent(tab: DeepLinkTab): Intent =
        Intent(this, MainActivity::class.java)
            .putExtra(DeepLinkKeys.START_TAB, tab.name)
}
