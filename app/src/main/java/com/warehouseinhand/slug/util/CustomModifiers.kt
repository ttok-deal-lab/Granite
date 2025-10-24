package com.warehouseinhand.slug.util

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp

inline fun Modifier.noRippleClickable(
    enable: Boolean = true,
    crossinline onClick: () -> Unit,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
): Modifier = this.composed {
    clickable(
        indication = null,
        enabled = enable,
        interactionSource = remember { interactionSource }) {
        onClick()
    }
}

fun Modifier.blockingClickable(
    eventLimitMillisecond: Long = 0,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    indication: Indication? = null,
    onClick: () -> Unit,
    enable: Boolean = true,
): Modifier = this.composed {
    val cutter =
        remember { if (eventLimitMillisecond != 0L) EventCutter(eventLimitMillisecond) else EventCutter() }
    clickable(
        indication = indication,
        enabled = enable,
        interactionSource = remember { interactionSource }) {
        cutter.processEvent(onClick)
    }
}

fun Modifier.blockingClickableWithRipple(
    eventLimitMillisecond: Long = 0,
    enable: Boolean = true,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onClick: () -> Unit
): Modifier = composed {
    blockingClickable(
        onClick = onClick,
        enable = enable,
        indication = ripple(),
        interactionSource = interactionSource,
        eventLimitMillisecond = eventLimitMillisecond
    )
}

fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}
//figma와 1:1대응
fun Modifier.dropShadow(
    shape: Shape,
    color: Color,
    blur: Dp ,
    offsetY: Dp,
    offsetX: Dp,
    spread: Dp,
) = this.drawBehind {
    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint().apply {
        this.color = color
    }

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}