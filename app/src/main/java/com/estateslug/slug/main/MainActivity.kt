package com.estateslug.slug.main

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.estateslug.slug.R
import com.estateslug.slug.deeplink.DeepLinkKeys
import com.estateslug.slug.deeplink.DeepLinkTab
import com.estateslug.slug.ui.theme.SlugTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setBackPress()
        // 딥링크 인텐트는 TaskStackBuilder가 만든 태스크의 base intent로 recents에 남아,
        // 종료 후 최근 앱에서 재실행하면 그대로 재전달된다(FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY 세팅됨).
        // 그 경우 extras를 무시해 상세/탭이 다시 열리지 않게 한다
        val isFromHistory =
            intent != null && (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0
        val startItem = intent?.takeUnless { isFromHistory }
            ?.getStringExtra(DeepLinkKeys.START_TAB)
            ?.let { runCatching { DeepLinkTab.valueOf(it) }.getOrNull() }
            .toBottomBarItem()
        // 회전 등 재생성 시(savedInstanceState != null)에는 nav 상태가 복원되므로
        // 딥링크 상세를 다시 push하지 않는다
        val startProductId: String? =
            if (savedInstanceState == null && !isFromHistory) intent?.getStringExtra(DeepLinkKeys.DETAIL_ID)
            else null
        setContent {
            SlugTheme {
                MainScreen(
                    startItem = startItem,
                    startProductId = startProductId,
                )
            }
        }
    }

    private var lastBackPressedAt = 0L
    private var exitToast: Toast? = null
    private fun setBackPress() {
        // NavHost가 컴포지션 시 자체 콜백을 나중에 등록하므로, nav backstack이 비었을 때만 이 콜백이 발동한다
        onBackPressedDispatcher.addCallback(this) {
            val now = SystemClock.elapsedRealtime()
            if (now - lastBackPressedAt <= BACK_PRESS_EXIT_WINDOW_MS) {
                exitToast?.cancel()
                finish()
            } else {
                lastBackPressedAt = now
                exitToast = Toast.makeText(
                    this@MainActivity,
                    R.string.main_back_press_exit_toast,
                    Toast.LENGTH_SHORT
                ).also { it.show() }
            }
        }
    }

    companion object {
        private const val BACK_PRESS_EXIT_WINDOW_MS = 2_000L
    }
}

private fun DeepLinkTab?.toBottomBarItem(): BottomBarItemUiModel = when (this) {
    DeepLinkTab.FAVORITE -> BottomBarItemUiModel.FAVORITES
    DeepLinkTab.MYPAGE -> BottomBarItemUiModel.MY_PAGE
    DeepLinkTab.HOME, null -> BottomBarItemUiModel.HOME
}
