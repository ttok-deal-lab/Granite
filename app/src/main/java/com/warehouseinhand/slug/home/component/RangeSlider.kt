package com.warehouseinhand.slug.home.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.PrimaryLight

/**
 * @param segments 스냅할 구간 개수. 0이면 양 끝점으로만 스냅
 * - segments < 0: snap 미동작
 * - segments = 0: 양 끝점 (0%, 100%)
 * - segments = 1: 3지점 (0%, 50%, 100%)
 * - segments = 4: 5지점 (0%, 25%, 50%, 75%, 100%)
 */
@Composable
fun SlugRangeSlider(
    modifier: Modifier = Modifier,
    startValue: Float = 0f,
    endValue: Float = 1f,
    trackSize: Dp = 8.dp,
    thumbSize: Dp = 30.dp,
    inActiveTrackColor: Color = NeutralWeak,
    activeTrackColor: Color = Primary,
    segments: Int = 9, // segment < 0 일시 snap 미동작
    onRangeChanged: (Pair<Float, Float>) -> Unit
) {
    var sliderMaxOffset by remember { mutableStateOf(1.dp) }// 1.dp 설정으로 Divided by Zero 방지
    val localDensity = LocalDensity.current
    val startOffsetX = remember { mutableStateOf(0.dp) }
    val endOffsetX = remember { mutableStateOf(0.dp) }
    var isStartThumbDragging by remember { mutableStateOf(false) }
    var isEndThumbDragging by remember { mutableStateOf(false) }

    fun snapValue(value: Dp): Dp {
        if (segments < 0) {
            return value
        }
        val snapPoints = segments + 1 // 양끝 포함한 스냅 포인트 개수
        val segmentSize = 1f / snapPoints
        val halfSegment = segmentSize / 2
        val currentRatio = value / sliderMaxOffset
        val nearestSnapPoint = ((currentRatio + halfSegment) / segmentSize).toInt()
        val snappedRatio = nearestSnapPoint * segmentSize
        return sliderMaxOffset * snappedRatio
    }
    //지속적으로 snap 단위로 전달합니다.
    val innerStartValue = remember { derivedStateOf { snapValue(startOffsetX.value) } }
    val innerEndValue = remember { derivedStateOf { snapValue(endOffsetX.value) } }

    //snap단위 동작 변환이 있을때만 동작.
    LaunchedEffect(innerStartValue.value, innerEndValue.value) {
        if (sliderMaxOffset.value > 1.dp.value)
            onRangeChanged(
                innerStartValue.value / sliderMaxOffset to innerEndValue.value / sliderMaxOffset
            )
    }

    //드래그 역전 상태일시 양 끝점이 따라감
    fun checkInversionCorrection() {
        if (isStartThumbDragging && endOffsetX.value < startOffsetX.value) {
            endOffsetX.value = startOffsetX.value
        }
        if (isEndThumbDragging && startOffsetX.value > endOffsetX.value) {
            startOffsetX.value = endOffsetX.value
        }
    }
    //snap단위 이동 적용.
    fun snapToSegments(state: MutableState<Dp>) {
        if (segments < 0) // segment<0 일시 snap 미동작
            return
        state.value = snapValue(state.value)
    }
    // 드래그 멈춤시 snap 적용
    fun onDragStopped() {
        snapToSegments(startOffsetX)
        snapToSegments(endOffsetX)
    }
    // thumb 위치 변경 처리.
    fun onDeltaChanged(state: MutableState<Dp>, delta: Float) {
        val newValue = state.value + with(localDensity) { delta.toDp() }
        state.value = when {
            newValue < 0.dp -> 0.dp
            newValue > sliderMaxOffset -> sliderMaxOffset
            else -> newValue
        }
        checkInversionCorrection()
    }


    Box(
        modifier = modifier
            .onSizeChanged {
                sliderMaxOffset = with(localDensity) { it.width.toDp() } - thumbSize
                startOffsetX.value = sliderMaxOffset * startValue
                endOffsetX.value = sliderMaxOffset * endValue
            }
            .height(thumbSize),
        contentAlignment = Alignment.CenterStart
    ) {
        InActiveTrack(trackSize = trackSize, trackColor = inActiveTrackColor)
        ActiveTrack(
            trackSize = trackSize,
            width = (endOffsetX.value - startOffsetX.value),
            xOffset = startOffsetX.value + thumbSize / 2,
            trackColor = activeTrackColor
        )
        Thumb(
            xOffset = startOffsetX.value,
            thumbSize = thumbSize,
            onDeltaChanged = { delta -> onDeltaChanged(startOffsetX, delta) },
            onDragStateChanged = { isStartThumbDragging = it },
            onDragStopped = { onDragStopped() },
        )
        Thumb(
            thumbSize = thumbSize,
            xOffset = endOffsetX.value,
            onDeltaChanged = { delta -> onDeltaChanged(endOffsetX, delta) },
            onDragStateChanged = { isEndThumbDragging = it },
            onDragStopped = { onDragStopped() },
        )
    }
}

@Composable
private fun Thumb(
    modifier: Modifier = Modifier,
    innerColor: Color = PrimaryLight,
    borderColor: Color = Primary,
    xOffset: Dp,
    thumbSize: Dp,
    onDeltaChanged: (delta: Float) -> Unit,
    onDragStateChanged: (Boolean) -> Unit,
    onDragStopped: () -> Unit,
) {

    Box(
        modifier
            .offset(x = xOffset, y = 0.dp)
            .draggable(
                orientation = Orientation.Horizontal,
                onDragStarted = {
                    onDragStateChanged(true)
                },
                onDragStopped = {
                    onDragStateChanged(false)
                    onDragStopped()
                },
                state = rememberDraggableState(onDeltaChanged)
            )
            .size(thumbSize)
            .clip(CircleShape)
            .background(color = innerColor)
            .border(width = 4.dp, color = borderColor, shape = CircleShape)
    )
}


@Composable
private fun ActiveTrack(
    trackSize: Dp,
    width: Dp,
    xOffset: Dp,
    trackColor: Color
) {
    Spacer(
        modifier = Modifier
            .height(trackSize)
            .width(width)
            .offset(x = xOffset, y = 0.dp)
            .background(shape = RoundedCornerShape(100.dp), color = trackColor)
    )
}

@Composable
private fun InActiveTrack(
    trackSize: Dp,
    trackColor: Color
) {
    Spacer(
        modifier = Modifier
            .height(trackSize)
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(100.dp), color = trackColor)
    )
}


@Composable
@Preview
fun PreviewSlugRangeSlider() {
    Column(
        modifier = Modifier
            .padding(top = 40.dp)
    ) {
        var text by remember { mutableStateOf("0..1") }
        SlugRangeSlider(modifier = Modifier.fillMaxWidth()) {
            text = it.toString()
            Log.d("value", "PreviewSlugRangeSlider: $it")
        }
        Text(text = text)
    }
}