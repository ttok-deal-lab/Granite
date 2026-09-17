package com.estateslug.slug.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTonalElevationEnabled
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.estateslug.slug.util.RemoveOverScroll

/**
 * 시스템 다크 모드를 따르는 Slug 테마.
 *
 * - Material 역할: [SlugLightColorScheme] / [SlugDarkColorScheme]
 * - 의미 토큰: [ProvideSlugColors]가 [SlugLightColors] / [SlugDarkColors]를 공급한다 (`SlugTheme.colors`)
 * - tonal elevation은 끈다. 켜 두면 Material 컴포넌트가 표면에 surfaceTint를 섞어
 *   팔레트 단계(Black200 / Gray800 / Gray700)를 벗어난다. docs/design-system/dark-color-scheme.md 9절.
 * - dynamicColor는 브랜드 팔레트를 유지하기 위해 쓰지 않는다. 인자는 기존 호출부 호환용이다.
 *
 * 시스템 바 아이콘은 각 Activity의 `enableEdgeToEdge()` 기본값이 같은 uiMode 기준으로 맞춘다.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun SlugTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    RemoveOverScroll {
        ProvideSlugColors(darkTheme) {
            CompositionLocalProvider(LocalTonalElevationEnabled provides false) {
                MaterialTheme(
                    colorScheme = if (darkTheme) SlugDarkColorScheme else SlugLightColorScheme,
                    typography = Typography,
                    content = content
                )
            }
        }
    }
}
