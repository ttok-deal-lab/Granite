package com.warehouseinhand.slug.ui.component.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.NeutralSubtle
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.util.blockingClickable

@Composable
fun ToggleSwitchCircle(
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    width: Dp = 50.dp,
    height: Dp = 30.dp,
    strokeWidth: Dp = 0.5.dp,
    checkedTrackColor: Color = Primary,
    uncheckedTrackColor: Color = NeutralSubtle,
    checkedThumbColor: Color = Color(0xFFFFFFFF),
    uncheckedThumbColor: Color = Color(0xFFFFFFFF),
    gapBetweenThumbAndTrackEdge: Dp = 2.dp,
    state: Boolean,
) {
    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge
    val density = LocalDensity.current
    val unSelectedPos = with(density) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    val selectedPos = with(density) { width.toPx() - unSelectedPos }

    val animatePosition = animateFloatAsState(
        targetValue = if (state) selectedPos else unSelectedPos,
        label = "circleSwitchAnimation"
    )
    Canvas(
        modifier = modifier
            .size(width = width, height = height)
            .scale(scale = scale)
    ) {
        val rectRadius = (thumbRadius + gapBetweenThumbAndTrackEdge).toPx()
        val thumbRadiusPx = thumbRadius.toPx()
        // Track
        drawRoundRect(
            color = if (state) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(rectRadius),
            style = Fill
        )
        // Thumb
        // TODO : dropshadow 적용
        if (strokeWidth != 0.dp) {
            val strokeBackground = thumbRadiusPx + strokeWidth.toPx()
            drawCircle(
                color = Color(0x0A000000),
                radius = strokeBackground,
                center = Offset(
                    x = animatePosition.value,
                    y = size.height / 2
                )
            )
        }
        drawCircle(
            color = if (state) checkedThumbColor else uncheckedThumbColor,
            radius = thumbRadiusPx,
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}

@Preview
@Composable
fun PreviewToggleSwitchCircle() {
    var state by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .blockingClickable(onClick = { state = !state }),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ToggleSwitchCircle(
            state = state
        )
        ToggleSwitchCircle(
            state = !state
        )
    }
}