package com.estateslug.slug.ui.component.label

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.ui.component.SlugText
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.NeutralLight
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.ui.theme.VerifiedGradientLower
import com.estateslug.slug.ui.theme.VerifiedGradientUpper

@Composable
fun SlugLabelLarge(
    uiModel: SlugLabelUiModel,
    frontSlot: @Composable RowScope.() -> Unit = { },
    backSlot: @Composable RowScope.() -> Unit = { },
) {
    SlugLabel(
        uiModel = uiModel,
        textStyle = SlugTypographyStyle.BodyMicroMedium,
        frontSlot = frontSlot,
        backSlot = backSlot,
    )
}

@Composable
fun VerifiedSlugLabelLarge(
    uiModel: SlugLabelUiModel,
    frontSlot: @Composable RowScope.() -> Unit = {
        ImageProcessor(
            modifier = Modifier.size(16.dp),
            imageResource = ImageResource.Id(R.drawable.verified_star_white_22_22)
        )
    },
    backSlot: @Composable RowScope.() -> Unit = { },
) {
    SlugLabel(
        uiModel = uiModel,
        textStyle = SlugTypographyStyle.BodyMicroMedium,
        frontSlot = frontSlot,
        backSlot = backSlot,
    )
}

@Composable
fun SlugLabelSmall(
    uiModel: SlugLabelUiModel,
    frontSlot: @Composable RowScope.() -> Unit = { },
    backSlot: @Composable RowScope.() -> Unit = { },
) {
    SlugLabel(
        uiModel = uiModel,
        textStyle = SlugTypographyStyle.CaptionLargeMedium,
        frontSlot = frontSlot,
        backSlot = backSlot,
    )
}


@Composable
private fun SlugLabel(
    uiModel: SlugLabelUiModel,
    textStyle: TextStyle,
    frontSlot: @Composable RowScope.() -> Unit = { },
    backSlot: @Composable RowScope.() -> Unit = { },
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .labelBackground(slugBackground = uiModel.labelStyle.resolveBackground())
            .padding(vertical = 3.dp, horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        frontSlot()
        Text(text = uiModel.text(), color = uiModel.labelStyle.resolveTextColor(), style = textStyle)
        backSlot()
    }
}

@Composable
private fun Modifier.labelBackground(slugBackground: SlugLabelBackground): Modifier {
    return when (val background = slugBackground) {
        is SlugLabelBackground.Solid ->
            background(color = background.getBackground())

        is SlugLabelBackground.Gradient ->
            background(brush = background.getBackground())
    }
}

/**
 * 라벨 색은 렌더 시점에 테마 토큰에서 해석한다. UI 모델(ViewModel)은 역할 객체만 들고 색을 들지 않는다.
 * [Dynamic]은 호출부가 색을 직접 정하는 예외이며 테마를 따르지 않는다.
 */
sealed class SlugLabelStyle {
    @Composable
    abstract fun resolveBackground(): SlugLabelBackground

    @Composable
    abstract fun resolveTextColor(): Color

    sealed class BuildingInfo : SlugLabelStyle() {
        /** 건물 유형: 주요색 연한 배경 + 주요색 글자. */
        data object BuildingType : BuildingInfo() {
            @Composable
            override fun resolveBackground() = SlugLabelBackground.Solid(SlugTheme.colors.primaryLight)

            @Composable
            override fun resolveTextColor() = SlugTheme.colors.primary
        }

        /** 유찰·매각완료 등 상태: 약한 중립 배경 + 보조 글자. */
        data object State : BuildingInfo() {
            @Composable
            override fun resolveBackground() = SlugLabelBackground.Solid(SlugTheme.colors.neutralWeak)

            @Composable
            override fun resolveTextColor() = SlugTheme.colors.neutralSubtler
        }

        /** 매각 임박(D-2 이내): 위험 연한 배경 + 위험 글자. */
        data object SaleImminent : BuildingInfo() {
            @Composable
            override fun resolveBackground() = SlugLabelBackground.Solid(SlugTheme.colors.criticalWeak)

            @Composable
            override fun resolveTextColor() = SlugTheme.colors.critical
        }
    }

    /** 호출부가 색을 직접 정한다. 테마를 따르지 않으므로 테마 무관 색에만 쓴다. */
    data class Dynamic(
        val background: SlugLabelBackground,
        val textColor: Color
    ) : SlugLabelStyle() {
        @Composable
        override fun resolveBackground() = background

        @Composable
        override fun resolveTextColor() = textColor
    }

    sealed class GradientBackground : SlugLabelStyle() {
        data class Dynamic(
            val background: SlugLabelBackground.Gradient,
            val textColor: Color
        ) : GradientBackground() {
            @Composable
            override fun resolveBackground() = background

            @Composable
            override fun resolveTextColor() = textColor
        }

        /** 인증 라벨은 브랜드 그라디언트라 테마와 무관하게 고정한다(글자도 라이트 상수 NeutralLight 고정). */
        data object Verified : GradientBackground() {
            private val background = SlugLabelBackground.Gradient(
                color1 = VerifiedGradientUpper,
                color2 = VerifiedGradientLower
            )

            @Composable
            override fun resolveBackground() = background

            @Composable
            override fun resolveTextColor() = NeutralLight
        }
    }
}

sealed class SlugLabelBackground() {
    data class Solid(val color: Color) : SlugLabelBackground() {
        fun getBackground() = color
    }

    data class Gradient(val color1: Color, val color2: Color) : SlugLabelBackground() {
        fun getBackground(): Brush =
            Brush.linearGradient(
                listOf(color1, color2),
                start = Offset(0.0f, 0.0f),
                end = Offset.Infinite
            )
    }
}

data class SlugLabelUiModel(
    val labelStyle: SlugLabelStyle,
    val text: SlugText,
)


@Composable
@Preview
fun PreviewSlugLabel() {
    val text = SlugText.Text("아파트")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            SlugLabelStyle.BuildingInfo.BuildingType,
            SlugLabelStyle.GradientBackground.Verified
        ).forEach { it ->
            Column {
                SlugLabelLarge(uiModel = SlugLabelUiModel(labelStyle = it, text = text))
                Spacer(Modifier.height(8.dp))
                SlugLabelSmall(uiModel = SlugLabelUiModel(labelStyle = it, text = text))
            }
        }

        SlugLabelLarge(
            SlugLabelUiModel(labelStyle = SlugLabelStyle.GradientBackground.Verified, text = text),
            frontSlot = {
                ImageProcessor(
                    modifier = Modifier.size(16.dp),
                    imageResource = ImageResource.Id(R.drawable.verified_star_white_22_22)
                )
            },
            backSlot = {
                ImageProcessor(
                    modifier = Modifier.size(16.dp),
                    imageResource = ImageResource.Id(R.drawable.verified_star_white_22_22)
                )
            }
        )
        SlugLabelLarge(
            SlugLabelUiModel(
                labelStyle = SlugLabelStyle.GradientBackground.Verified,
                text = SlugText.Text("인증매물")
            ),
            frontSlot = {
                ImageProcessor(
                    modifier = Modifier.size(16.dp),
                    imageResource = ImageResource.Id(R.drawable.verified_star_white_22_22)
                )
            }
        )
    }
}