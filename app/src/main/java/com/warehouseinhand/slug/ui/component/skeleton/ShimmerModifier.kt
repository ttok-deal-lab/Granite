package com.warehouseinhand.slug.ui.component.skeleton

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
import com.warehouseinhand.slug.ui.theme.NeutralMuted
import com.warehouseinhand.slug.ui.theme.NeutralWeak

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

    drawWithContent {
        drawContent()
        val brush = Brush.linearGradient(
            colors = listOf(NeutralWeak, NeutralMuted, NeutralWeak),
            start = Offset(offsetX, 0f),
            end = Offset(offsetX + size.width, size.height),
        )
        drawRect(brush = brush)
    }
}
