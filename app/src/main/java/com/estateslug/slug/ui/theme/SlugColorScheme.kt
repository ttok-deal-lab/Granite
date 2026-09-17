package com.estateslug.slug.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

/**
 * Material 색상 역할을 기존 Slug 팔레트에 연결한 라이트 테마.
 *
 * 버튼의 pressed/disabled 상태, 도메인 상태, 그라디언트, SNS 브랜드 색은
 * 각 컴포넌트의 토큰을 사용한다. 전체 매핑 근거는 docs/design-system/color-usage.md 참고.
 */
val SlugLightColorScheme: ColorScheme = lightColorScheme(
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

/**
 * 기존 원시 팔레트만 사용하는 다크 테마. 역할별 결정은 docs/design-system/dark-color-scheme.md 참고.
 *
 * 표면은 Black200 → Gray800 → Gray700으로 구분한다. 테마 연결부에서 tonal elevation을
 * 비활성화해야 이 표면값이 유지된다. 버튼 상태색·도메인 토큰은 별도로 공급한다.
 */
val SlugDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Blue150,
    onPrimary = Blue900,
    primaryContainer = Blue800,
    onPrimaryContainer = Blue50,
    inversePrimary = Blue600,

    secondary = Green400,
    onSecondary = Green900,
    secondaryContainer = Green800,
    onSecondaryContainer = Green100,
    tertiary = Green400,
    onTertiary = Green900,
    tertiaryContainer = Green800,
    onTertiaryContainer = Green100,

    background = Black200,
    onBackground = Gray150,
    surface = Black200,
    onSurface = Gray150,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray300,
    surfaceTint = Blue150,
    inverseSurface = Gray150,
    inverseOnSurface = Gray800,

    // Red300 글자는 Black200·Gray800·위험 패널에 사용하며 Gray700 표면에는 사용하지 않는다.
    error = Red300,
    onError = Red900,
    errorContainer = Red800,
    onErrorContainer = Red150,
    outline = Gray400,
    outlineVariant = Gray600,
    scrim = Gray900,

    surfaceBright = Gray700,
    surfaceDim = Black200,
    surfaceContainerLowest = Gray900,
    surfaceContainerLow = Gray800,
    surfaceContainer = Gray800,
    surfaceContainerHigh = Gray700,
    // Gray600은 보조 글자 대비가 부족하므로 콘텐츠 표면에서 제외한다.
    surfaceContainerHighest = Gray700,
)
