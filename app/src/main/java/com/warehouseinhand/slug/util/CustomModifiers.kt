package com.warehouseinhand.slug.util

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

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
        indication = rememberRipple(),
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