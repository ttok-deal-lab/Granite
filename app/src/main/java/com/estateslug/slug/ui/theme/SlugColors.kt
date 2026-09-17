package com.estateslug.slug.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 테마를 따르는 Slug 의미 색 토큰.
 *
 * 화면과 공통 컴포넌트는 [Color.kt]의 최상위 상수(`Neutral`, `Primary` …)를 직접 쓰지 않고
 * `SlugTheme.colors.neutral`처럼 이 토큰을 읽는다. 최상위 상수는 라이트 값으로 고정된 원시/별칭 값이며,
 * 테마와 무관하게 고정해야 하는 곳(사진 위 D-day 칩, 인증 라벨 그라디언트, SNS 브랜드 버튼)만 계속 직접 참조한다.
 *
 * 값의 근거는 docs/design-system/dark-color-scheme.md 3절(의미 토큰)과 3-1절(사용처 이동 검증)이다.
 * 라이트 인스턴스는 기존 별칭 상수를 그대로 참조하므로 라이트 렌더 결과가 바뀌지 않는다.
 *
 * ## Theme.kt 연결 (다크 스킴 담당자에게)
 * [LocalSlugColors]의 기본값은 [SlugLightColors]라 연결 전에도 모든 화면은 라이트로 그려진다.
 * `SlugTheme(darkTheme)`에서 `MaterialTheme(colorScheme = …)` 바깥이나 안쪽에
 * `ProvideSlugColors(darkTheme) { … }`로 감싸면 다크에서 [SlugDarkColors]가 공급된다.
 */
@Immutable
data class SlugColors(
    // 주요색
    val primary: Color,
    val primaryContrast: Color,
    val primaryMuted: Color,
    val primaryWeak: Color,
    val primaryLight: Color,
    val onPrimary: Color,
    val secondary: Color,

    // 중립 — 글자·아이콘·배경
    val neutral: Color,
    val neutralContrast: Color,
    val neutralSubtler: Color,
    /** 읽어야 하는 약한 정보 글자(관심 수)와 검색 아이콘. 비활성은 [neutralDisabled]. */
    val neutralSubtle: Color,
    /** 비활성 컨트롤의 글자. 라이트는 [neutralSubtle]과 같은 값. */
    val neutralDisabled: Color,
    /** 눌림·비활성 배경, 스켈레톤 강조. 조작 가능한 미선택 아이콘은 [iconUnselected]. */
    val neutralMuted: Color,
    val neutralWeak: Color,
    /**
     * 화면 바탕 위에 놓이는 패널·칩·표 셀 배경. 카드·시트 안에서는 [surfaceInset], 어두운 버튼 위 글자 용도는 [onTertiary].
     * 예외: shimmer 블록을 담는 스켈레톤 칸은 카드 안이어도 이 토큰을 쓴다(DetailScreenSkeleton의 주석 참고).
     */
    val neutralLight: Color,
    /** 홈 필터 pill: 미선택 배경이자 선택(Neutral 배경) pill의 글자. [neutral]과 반전 짝. */
    val neutralPill: Color,
    /** 고정 밝은 전경. 주요 버튼 글자 용도는 [onPrimary]. */
    val neutralWhite: Color,
    /** 화면 바탕과, 바탕색 글자(선택된 칩 위 글자). 카드·시트는 [surfaceRaised]. */
    val neutralInverted: Color,
    /** 가장 강한 글자(라이트 Black200). */
    val neutralBlack: Color,

    // 표면·경계·조작
    /** 바탕 위에 올라오는 카드·바텀시트·시트 내부 배경·다이얼로그. */
    val surfaceRaised: Color,
    /** 넓은 면과 두꺼운 구분띠(설정 페이지 바닥, 10dp 섹션 구분). 작은 pill·라벨 배경은 [neutralWeak]. */
    val surfaceSunken: Color,
    /**
     * 올라온 면([surfaceRaised] 카드·시트) 안에서 한 단 구분되는 면: 2톤 카드의 아래 칸, 시트 안 안내 상자, 눌린 행, 시트 헤더 아래 1dp 구분선.
     * 라이트는 [neutralLight]와 같은 값이다. 다크는 [neutralLight]가 [surfaceRaised]와 같은 단계라 그 위에서 사라지므로 한 단 밝은 값을 쓴다.
     * 위험 글자([critical])는 올리지 않는다(다크 4.20:1). 위험 상자는 [criticalLight] 배경을 쓴다.
     */
    val surfaceInset: Color,
    /** 조작 경계(꺼진 스위치 트랙 등). */
    val outline: Color,
    /** 장식 구분선·칩 테두리(라이트 Gray150). */
    val outlineVariant: Color,
    val imagePlaceholder: Color,
    /** 조작 가능한 미선택 아이콘(관심 하트 off, 내비 미선택). */
    val iconUnselected: Color,
    /** 스위치 꺼진 상태 thumb. 켜진 상태는 onPrimary. */
    val switchThumbOff: Color,
    /** 스낵바 배경·글자·액션. */
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val inversePrimary: Color,

    // 의미색
    /** 위험·가격 상승·경매 결과 글자. */
    val critical: Color,
    /** 관심 하트 등 강조 아이콘. 라이트는 [critical]과 같은 값. */
    val criticalIcon: Color,
    val criticalSubtle: Color,
    val criticalWeak: Color,
    val criticalLight: Color,
    val positive: Color,
    val positiveLight: Color,
    val gradientUpper: Color,
    val gradientLower: Color,

    // BasicButton 상태 — 테마별로 상태 방향이 다르므로 테마가 값을 소유한다
    /** Fill Tertiary 활성 글자. */
    val onTertiary: Color,
    val buttonPrimaryDisabledText: Color,
    val buttonTertiaryPressed: Color,
    val buttonTertiaryDisabledText: Color,
    val buttonGhostPrimaryDisabled: Color,
    val buttonGhostSecondaryBorder: Color,
    val buttonGhostSecondaryBorderPressed: Color,
    val buttonGhostSecondaryBorderDisabled: Color,
)

/** 라이트: 기존 별칭 상수를 그대로 참조한다. 값이 아니라 참조로 연결해 라이트 렌더가 바뀌지 않게 한다. */
val SlugLightColors: SlugColors = SlugColors(
    primary = Primary,
    primaryContrast = PrimaryContrast,
    primaryMuted = PrimaryMuted,
    primaryWeak = PrimaryWeak,
    primaryLight = PrimaryLight,
    onPrimary = NeutralWhite,
    secondary = Secondary,

    neutral = Neutral,
    neutralContrast = NeutralContrast,
    neutralSubtler = NeutralSubtler,
    neutralSubtle = NeutralSubtle,
    neutralDisabled = NeutralSubtle,
    neutralMuted = NeutralMuted,
    neutralWeak = NeutralWeak,
    neutralLight = NeutralLight,
    neutralPill = Gray150,
    neutralWhite = NeutralWhite,
    neutralInverted = NeutralInverted,
    neutralBlack = Black200,

    surfaceRaised = NeutralInverted,
    surfaceSunken = NeutralWeak,
    surfaceInset = NeutralLight,
    outline = NeutralSubtle,
    outlineVariant = Gray150,
    imagePlaceholder = Gray150,
    iconUnselected = NeutralMuted,
    switchThumbOff = NeutralWhite,
    inverseSurface = Gray600,
    inverseOnSurface = NeutralInverted,
    inversePrimary = PrimaryWeak,

    critical = Critical,
    criticalIcon = Critical,
    criticalSubtle = CriticalSubtle,
    criticalWeak = CriticalWeak,
    criticalLight = CriticalLight,
    positive = Positive,
    positiveLight = PositiveLight,
    gradientUpper = GradientUpper,
    gradientLower = GradientLower,

    onTertiary = NeutralLight,
    buttonPrimaryDisabledText = NeutralWhite,
    buttonTertiaryPressed = NeutralContrast,
    buttonTertiaryDisabledText = NeutralInverted,
    buttonGhostPrimaryDisabled = PrimaryMuted,
    buttonGhostSecondaryBorder = NeutralMuted,
    buttonGhostSecondaryBorderPressed = NeutralSubtler,
    buttonGhostSecondaryBorderDisabled = NeutralSubtler,
)

/** 다크: docs/design-system/dark-color-scheme.md 3절·4절의 값. 원시 단계를 직접 참조한다. */
val SlugDarkColors: SlugColors = SlugColors(
    primary = Blue150,
    primaryContrast = Blue100,
    primaryMuted = Blue800,
    primaryWeak = Blue600,
    primaryLight = Blue800,
    onPrimary = Blue900,
    secondary = Green400,

    neutral = Gray150,
    neutralContrast = Gray50,
    neutralSubtler = Gray300,
    neutralSubtle = Gray300,
    neutralDisabled = Gray500,
    neutralMuted = Gray600,
    neutralWeak = Gray700,
    neutralLight = Gray800,
    neutralPill = Gray700,
    neutralWhite = NeutralWhite,
    neutralInverted = Black200,
    neutralBlack = Gray50,

    surfaceRaised = Gray800,
    surfaceSunken = Gray800,
    surfaceInset = Gray700,
    outline = Gray400,
    outlineVariant = Gray600,
    imagePlaceholder = Gray700,
    iconUnselected = Gray400,
    switchThumbOff = Gray150,
    inverseSurface = Gray150,
    inverseOnSurface = Gray800,
    inversePrimary = Blue600,

    critical = Red300,
    criticalIcon = Red500,
    criticalSubtle = Red300,
    criticalWeak = Red900,
    criticalLight = Red900,
    positive = Blue150,
    positiveLight = Blue800,
    gradientUpper = Green400,
    gradientLower = Blue150,

    onTertiary = Gray800,
    buttonPrimaryDisabledText = Blue150,
    buttonTertiaryPressed = Gray200,
    buttonTertiaryDisabledText = Gray300,
    buttonGhostPrimaryDisabled = Blue600,
    buttonGhostSecondaryBorder = Gray500,
    buttonGhostSecondaryBorderPressed = Gray300,
    buttonGhostSecondaryBorderDisabled = Gray700,
)

/** 기본값이 라이트라 [ProvideSlugColors] 연결 전(Preview 포함)에도 기존과 같게 그려진다. */
val LocalSlugColors = staticCompositionLocalOf { SlugLightColors }

/** `SlugTheme.colors.neutral`처럼 접근한다. MaterialTheme 객체와 같은 방식. */
object SlugTheme {
    val colors: SlugColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSlugColors.current
}

@Composable
fun ProvideSlugColors(darkTheme: Boolean, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalSlugColors provides if (darkTheme) SlugDarkColors else SlugLightColors,
        content = content
    )
}
