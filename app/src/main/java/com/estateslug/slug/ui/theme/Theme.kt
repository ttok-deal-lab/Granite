package com.estateslug.slug.ui.theme

import androidx.compose.material3.LocalTonalElevationEnabled
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.estateslug.slug.util.RemoveOverScroll

/**
 * Slug 테마. 설정의 화면 테마([ThemeMode]: 라이트/다크/시스템)를 따르고, 시스템이면 기기 다크 모드를 따른다.
 *
 * - `darkTheme` 기본값은 [rememberAppDarkTheme] — 설정값과 기기 값으로 정한다. Preview는 인자로 고정할 수 있다.
 * - Material 역할: [SlugLightColorScheme] / [SlugDarkColorScheme]
 * - 의미 토큰: [ProvideSlugColors]가 [SlugLightColors] / [SlugDarkColors]를 공급한다 (`SlugTheme.colors`)
 * - tonal elevation은 끈다. 켜 두면 Material 컴포넌트가 표면에 surfaceTint를 섞어
 *   팔레트 단계(Black200 / Gray800 / Gray700)를 벗어난다. docs/design-system/dark-color-scheme.md 9절.
 * - dynamicColor는 브랜드 팔레트를 유지하기 위해 쓰지 않는다. 인자는 기존 호출부 호환용이다.
 *
 * 시스템 바 아이콘은 [SyncSystemBarsWithTheme]가 실제 그리는 테마에 맞춘다(설정이 기기 값과 다를 때 필요).
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun SlugTheme(
    darkTheme: Boolean = rememberAppDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    SyncSystemBarsWithTheme(darkTheme)
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
