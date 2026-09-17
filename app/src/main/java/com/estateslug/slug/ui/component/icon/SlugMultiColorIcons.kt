package com.estateslug.slug.ui.component.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.theme.ProvideSlugColors
import com.estateslug.slug.ui.theme.SlugTheme
import java.util.concurrent.ConcurrentHashMap

/*
 * 색이 둘 이상인 벡터 아이콘.
 *
 * 이런 자산은 ColorFilter.tint가 두 색을 하나로 뭉개 형태가 사라지므로 XML + tint로는 테마를 입힐 수 없다.
 * 그래서 res/drawable 대신 코드로 두고 색을 인자로 받는다. 단색 아이콘은 계속 XML + ImageProcessor(tint)를 쓴다.
 *
 * pathData 문자열은 삭제한 원본 XML(git 이력의 res/drawable/ic_clock_18_18, ic_check_selected_24_24,
 * ic_check_unselected_24_24, list_empty_50_50)에서 글자 그대로 옮겼고, XML 파서가 내부에서 쓰는 것과 같은
 * ImageVector.Builder.addPath에 넣으므로 라이트 렌더는 XML 시절과 픽셀 단위로 같다.
 * 디자인이 바뀌면 SVG의 path `d` 값을 해당 상수에 붙여 넣는다. 크기·뷰포트·선 굵기는 아래 빌더에 있다.
 *
 * 색 매핑 — 라이트 값은 XML에 박혀 있던 색과 같아야 한다(라이트 보존 규칙).
 *   자산            층      XML 색      토큰              라이트 → 다크
 *   시계            원판    #CFD6D9    neutralMuted      Gray200 → Gray600
 *                   바늘    #FFFFFF    neutralInverted   Gray10  → Black200   (화면 바탕과 같은 "뚫린" 표현)
 *   체크(선택)      원판    #1E9EFF    primary           Blue200 → Blue150
 *                   체크    #FFFFFF    onPrimary         Gray10  → Blue900    (채움색 위 전경)
 *   체크(미선택)    원판    #F1F4F6    neutralWeak       Gray100 → Gray700
 *                   체크    #FFFFFF    neutralInverted   Gray10  → Black200
 *   빈 상태         원판    #F1F4F6    neutralWeak       Gray100 → Gray700
 *                   점      #AEB7BC    neutralDisabled   Gray300 → Gray500    (라이트 명도차 Δ22에 가장 가까운 Δ19.
 *                                                                              neutralSubtle(Gray300)은 Δ49로 과함)
 */

/**
 * 색 조합별로 만든 벡터를 프로세스에서 공유한다. painterResource가 XML 벡터를 캐시하던 것과 같은 역할이다.
 * 테마가 둘뿐이라 항목 수는 아이콘당 많아야 몇 개다. 리스트 항목마다 path를 다시 파싱하지 않게 한다.
 */
private val vectorCache = ConcurrentHashMap<Triple<String, Color, Color>, ImageVector>()

private fun cachedVector(name: String, disc: Color, glyph: Color, build: (Color, Color) -> ImageVector): ImageVector =
    vectorCache.getOrPut(Triple(name, disc, glyph)) { build(disc, glyph) }

/** 최근 검색어 행의 시계. 바늘은 화면 바탕과 같은 색으로 "뚫린" 표현이라 기본값이 바탕 토큰이다. */
@Composable
fun ClockIcon(
    modifier: Modifier = Modifier,
    disc: Color = SlugTheme.colors.neutralMuted,
    glyph: Color = SlugTheme.colors.neutralInverted,
    contentDescription: String? = null,
) {
    val vector = remember(disc, glyph) { cachedVector("clock", disc, glyph, ::clockVector) }
    Image(
        modifier = modifier,
        imageVector = vector,
        contentScale = ContentScale.Fit,
        contentDescription = contentDescription
    )
}

/**
 * 원형 체크. 선택은 주요색 채움 위의 전경(onPrimary), 미선택은 약한 중립 원판에 바탕색 체크.
 * 두 상태의 도형은 같고 색만 다르다.
 */
@Composable
fun CheckCircleIcon(
    checked: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val colors = SlugTheme.colors
    val disc = if (checked) colors.primary else colors.neutralWeak
    val glyph = if (checked) colors.onPrimary else colors.neutralInverted
    val vector = remember(disc, glyph) { cachedVector("checkCircle", disc, glyph, ::checkCircleVector) }
    Image(
        modifier = modifier,
        imageVector = vector,
        contentScale = ContentScale.Fit,
        contentDescription = contentDescription
    )
}

/** 빈 목록·오류 상태의 말줄임 일러스트. 점은 라이트의 약한 명도차를 다크에서도 유지하도록 비활성 단계를 쓴다. */
@Composable
fun ListEmptyIcon(
    modifier: Modifier = Modifier,
    disc: Color = SlugTheme.colors.neutralWeak,
    glyph: Color = SlugTheme.colors.neutralDisabled,
    contentDescription: String? = null,
) {
    val vector = remember(disc, glyph) { cachedVector("listEmpty", disc, glyph, ::listEmptyVector) }
    Image(
        modifier = modifier,
        imageVector = vector,
        contentScale = ContentScale.Fit,
        contentDescription = contentDescription
    )
}

// 원본: ic_clock_18_18.xml (18×18)
private const val CLOCK_DISC = "M16.5,9C16.5,13.142 13.142,16.5 9,16.5C4.858,16.5 1.5,13.142 1.5,9C1.5,4.858 4.858,1.5 9,1.5C13.142,1.5 16.5,4.858 16.5,9Z"
private const val CLOCK_HANDS = "M9,5.016V9.328L12.656,10.875"

// 원본: ic_check_selected_24_24.xml / ic_check_unselected_24_24.xml (24×24, 도형 동일)
private const val CHECK_DISC = "M12,0L12,0A12,12 0,0 1,24 12L24,12A12,12 0,0 1,12 24L12,24A12,12 0,0 1,0 12L0,12A12,12 0,0 1,12 0z"
private const val CHECK_MARK = "M7,13.036L9.519,15.169C10.11,15.669 10.967,15.595 11.473,15L17,8.5"

// 원본: list_empty_50_50.xml (51×50)
private const val EMPTY_DISC = "M50.5,25C50.5,38.81 39.308,50 25.5,50C11.693,50 0.5,38.81 0.5,25C0.5,11.194 11.693,-0.001 25.5,-0.001C39.308,-0.001 50.5,11.194 50.5,25Z"
private const val EMPTY_DOTS = "M14.303,22.009C15.955,22.009 17.298,23.351 17.298,24.999C17.298,26.649 15.955,27.994 14.303,27.994C12.65,27.994 11.308,26.649 11.308,24.999C11.308,23.351 12.65,22.009 14.303,22.009ZM25.5,22.009C27.153,22.009 28.495,23.351 28.495,24.999C28.495,26.649 27.153,27.994 25.5,27.994C23.848,27.994 22.505,26.649 22.505,24.999C22.505,23.352 23.848,22.009 25.5,22.009ZM36.699,22.009C38.352,22.009 39.694,23.352 39.694,24.999C39.694,26.649 38.352,27.994 36.699,27.994C35.047,27.994 33.704,26.649 33.704,24.999C33.704,23.351 35.047,22.009 36.699,22.009Z"

private fun clockVector(disc: Color, glyph: Color): ImageVector =
    ImageVector.Builder(
        name = "SlugClock",
        defaultWidth = 18.dp,
        defaultHeight = 18.dp,
        viewportWidth = 18f,
        viewportHeight = 18f
    )
        .addPath(pathData = addPathNodes(CLOCK_DISC), fill = SolidColor(disc))
        .addPath(
            pathData = addPathNodes(CLOCK_HANDS),
            stroke = SolidColor(glyph),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round
        )
        .build()

private fun checkCircleVector(disc: Color, glyph: Color): ImageVector =
    ImageVector.Builder(
        name = "SlugCheckCircle",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    )
        .addPath(pathData = addPathNodes(CHECK_DISC), fill = SolidColor(disc))
        .addPath(
            pathData = addPathNodes(CHECK_MARK),
            stroke = SolidColor(glyph),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        )
        .build()

private fun listEmptyVector(disc: Color, glyph: Color): ImageVector =
    ImageVector.Builder(
        name = "SlugListEmpty",
        defaultWidth = 51.dp,
        defaultHeight = 50.dp,
        viewportWidth = 51f,
        viewportHeight = 50f
    )
        .addPath(pathData = addPathNodes(EMPTY_DISC), fill = SolidColor(disc))
        .addPath(pathData = addPathNodes(EMPTY_DOTS), fill = SolidColor(glyph))
        .build()

@Composable
private fun MultiColorIconRow() {
    Row(
        modifier = Modifier
            .background(SlugTheme.colors.neutralInverted)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ClockIcon()
        CheckCircleIcon(checked = true)
        CheckCircleIcon(checked = false)
        ListEmptyIcon()
    }
}

@Preview
@Composable
private fun PreviewMultiColorIconsLight() {
    ProvideSlugColors(darkTheme = false) { MultiColorIconRow() }
}

@Preview
@Composable
private fun PreviewMultiColorIconsDark() {
    ProvideSlugColors(darkTheme = true) { MultiColorIconRow() }
}
