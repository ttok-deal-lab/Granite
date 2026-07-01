package com.estateslug.slug.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.estateslug.slug.deeplink.DeepLinkKeys
import com.estateslug.slug.deeplink.DeepLinkTab
import com.estateslug.slug.ui.theme.SlugTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val startItem = intent?.getStringExtra(DeepLinkKeys.START_TAB)
            ?.let { runCatching { DeepLinkTab.valueOf(it) }.getOrNull() }
            .toBottomBarItem()
        setContent {
            SlugTheme {
                MainScreen(startItem = startItem)
            }
        }
    }
}

private fun DeepLinkTab?.toBottomBarItem(): BottomBarItemUiModel = when (this) {
    DeepLinkTab.FAVORITE -> BottomBarItemUiModel.FAVORITES
    DeepLinkTab.MYPAGE -> BottomBarItemUiModel.MY_PAGE
    DeepLinkTab.HOME, null -> BottomBarItemUiModel.HOME
}
