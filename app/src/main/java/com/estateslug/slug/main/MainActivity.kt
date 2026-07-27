package com.estateslug.slug.main

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
        val startItem = intent?.getStringExtra(DeepLinkKeys.START_TAB)
            ?.let { runCatching { DeepLinkTab.valueOf(it) }.getOrNull() }
            .toBottomBarItem()
        setContent {
            SlugTheme {
                MainScreen(startItem = startItem)
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
