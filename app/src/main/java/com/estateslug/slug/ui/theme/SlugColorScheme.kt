package com.estateslug.slug.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme

/**
 * Material 색상 역할을 기존 Slug 팔레트에 연결한 라이트 테마.
 *
 * 버튼의 pressed/disabled 상태, 도메인 상태, 그라디언트, SNS 브랜드 색은
 * 각 컴포넌트의 토큰을 사용한다. 전체 매핑 근거는 docs/design-system/color-usage.md 참고.
 */
val SlugLightClolorScheme: ColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = NeutralWhite,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = NeutralContrast,
    inversePrimary = PrimaryWeak,

    secondary = Secondary,
    onSecondary = NeutralContrast,
    secondaryContainer = Secondary,
    onSecondaryContainer = NeutralContrast,
    // 독립적인 tertiary 팔레트가 없어 기존 테마의 Secondary 매핑을 유지한다.
    tertiary = Secondary,
    onTertiary = NeutralContrast,
    tertiaryContainer = Secondary,
    onTertiaryContainer = NeutralContrast,

    background = NeutralInverted,
    onBackground = Neutral,
    surface = NeutralInverted,
    onSurface = Neutral,
    surfaceVariant = NeutralWeak,
    onSurfaceVariant = NeutralSubtler,
    surfaceTint = Primary,
    inverseSurface = Gray600,
    inverseOnSurface = NeutralInverted,

    error = Critical,
    onError = NeutralWhite,
    errorContainer = CriticalLight,
    onErrorContainer = NeutralContrast,
    outline = NeutralSubtle,
    outlineVariant = Gray150,
    scrim = PrimaryBlack,

    surfaceBright = NeutralInverted,
    surfaceDim = Gray150,
    surfaceContainerLowest = NeutralInverted,
    // 기존 bottom sheet의 흰 배경을 유지한다.
    surfaceContainerLow = NeutralInverted,
    surfaceContainer = NeutralLight,
    surfaceContainerHigh = NeutralWeak,
    surfaceContainerHighest = Gray150,
)
