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
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.Primary
import com.estateslug.slug.ui.theme.PrimaryLight
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
            .labelBackground(slugBackground = uiModel.labelStyle.background)
            .padding(vertical = 3.dp, horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        frontSlot()
        Text(text = uiModel.text(), color = uiModel.labelStyle.textColor, style = textStyle)
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

sealed class SlugLabelStyle(
) {
    abstract val background: SlugLabelBackground
    abstract val textColor: Color

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SlugLabelStyle) return false
        return background == other.background && textColor == other.textColor
    }

    override fun hashCode(): Int {
        var result = background.hashCode()
        result = 31 * result + textColor.hashCode()
        return result
    }

    sealed class BuildingInfo : SlugLabelStyle() {
        data object BuildingType : BuildingInfo() {
            override val background = SlugLabelBackground.Solid(PrimaryLight)
            override val textColor = Primary
        }

        data object State : BuildingInfo() {
            override val background = SlugLabelBackground.Solid(NeutralWeak)
            override val textColor = NeutralSubtler
        }
    }

    data class Dynamic(
        override val background: SlugLabelBackground,
        override val textColor: Color
    ) : SlugLabelStyle()

    sealed class GradientBackground(
    ) : SlugLabelStyle() {
        data class Dynamic(
            override val background: SlugLabelBackground.Gradient,
            override val textColor: Color
        ) : GradientBackground()

        data object Verified : GradientBackground() {

            override val background = SlugLabelBackground.Gradient(
                color1 = VerifiedGradientUpper,
                color2 = VerifiedGradientLower
            )
            override val textColor: Color = NeutralLight
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