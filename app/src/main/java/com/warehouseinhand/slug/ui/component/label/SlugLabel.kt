package com.warehouseinhand.slug.ui.component.label

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.PrimaryLight
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.ui.theme.VerifiedGradientLower
import com.warehouseinhand.slug.ui.theme.VerifiedGradientUpper

@Composable
fun SlugLabelLarge(
    labelStyle: SlugLabelStyle,
    text: String
) {
    SlugLabel(labelStyle = labelStyle, text = text, textStyle = SlugTypographyStyle.BodyMicroMedium)
}

@Composable
fun SlugLabelSmall(
    labelStyle: SlugLabelStyle,
    text: String
) {
    SlugLabel(
        labelStyle = labelStyle,
        text = text,
        textStyle = SlugTypographyStyle.CaptionLargeMedium
    )
}



@Composable
@Stable
private fun SlugLabel(
    labelStyle: SlugLabelStyle,
    text: String,
    textStyle: TextStyle
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .labelBackground(slugBackground = labelStyle.background)
            .padding(vertical = 3.dp, horizontal = 6.dp)
    ) {
        Text(text = text, color = labelStyle.textColor, style = textStyle)
    }
}

@Composable
fun Modifier.labelBackground(slugBackground: SlugLabelBackground): Modifier {
    return when (val background = slugBackground) {
        is SlugLabelBackground.Solid ->
            background(color = background.getBackground())

        is SlugLabelBackground.Gradient ->
            background(brush = background.getBackground())
    }
}

//TODO : 미리 정의된 데이터 정리
sealed class SlugLabelStyle(
) {
    abstract val background: SlugLabelBackground
    abstract val textColor: Color

    sealed class BuildingInfo : SlugLabelStyle() {
        data object Apartment : BuildingInfo() {
            override val background = SlugLabelBackground.Solid(PrimaryLight)
            override val textColor = Primary
        }

        data object Villa : BuildingInfo() {
            override val background = SlugLabelBackground.Solid(PrimaryLight)
            override val textColor = Primary
        }

        data object Officetel : BuildingInfo() {
            override val background = SlugLabelBackground.Solid(PrimaryLight)
            override val textColor = Primary
        }

        data object Dagagu : BuildingInfo() {
            override val background = SlugLabelBackground.Solid(PrimaryLight)
            override val textColor = Primary
        }

        data object State : BuildingInfo() {
            override val background = SlugLabelBackground.Solid(NeutralWeak)
            override val textColor = NeutralSubtler
        }

        companion object {
            val entries = listOf<BuildingInfo>(Apartment, Villa, Officetel, Dagagu, State)
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

        //TODO :실제 ui의 width에 맞춘 gradient가 되어야함.
        fun getBackground(): Brush =
            Brush.linearGradient(
                listOf(color1, color2),
                start = Offset(0.0f, 0.0f),
                end = Offset(75.0f, 75.0f)
            )
    }
}


@Composable
@Preview
fun PreviewSlugLabel() {
    val text: String = "아파트"
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(SlugLabelStyle.BuildingInfo.Apartment,
            SlugLabelStyle.GradientBackground.Verified).forEach { it ->
            Column {
                SlugLabelLarge(labelStyle = it, text = text)
                Spacer(Modifier.height(8.dp))
                SlugLabelSmall(labelStyle = it, text = text)
            }
        }
    }
}