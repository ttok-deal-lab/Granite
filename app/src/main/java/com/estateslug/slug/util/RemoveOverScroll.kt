package com.estateslug.slug.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemoveOverScroll(composableContent: @Composable () -> Unit){
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        composableContent()
    }
}
