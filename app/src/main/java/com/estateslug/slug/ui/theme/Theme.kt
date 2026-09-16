package com.estateslug.slug.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.estateslug.slug.util.RemoveOverScroll

// 다크/dynamic 디자인 도입 전까지 라이트 테마 고정. 기존 호출부의 인자 계약은 유지한다.
@Suppress("UNUSED_PARAMETER")
@Composable
fun SlugTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    RemoveOverScroll {
        MaterialTheme(
            colorScheme = SlugLightClolorScheme,
            typography = Typography,
            content = content
        )
    }
}
