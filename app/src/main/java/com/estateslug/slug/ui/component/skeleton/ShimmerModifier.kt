package com.estateslug.slug.ui.component.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.estateslug.slug.ui.theme.SlugTheme

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offsetX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerOffset",
    )
    val neutralWeak = SlugTheme.colors.neutralWeak
    val neutralMuted = SlugTheme.colors.neutralMuted

    drawWithContent {
        drawContent()
        val brush = Brush.linearGradient(
            colors = listOf(neutralWeak, neutralMuted, neutralWeak),
            start = Offset(offsetX, 0f),
            end = Offset(offsetX + size.width, size.height),
        )
        drawRect(brush = brush)
    }
}
