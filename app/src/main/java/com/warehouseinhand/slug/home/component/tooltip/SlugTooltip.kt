/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//Modified by KSH_SLUG

package com.warehouseinhand.slug.home.component.tooltip

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.rememberTooltipState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.PopupPositionProvider
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable
import kotlinx.coroutines.launch

/**
 *
 * m3 패키지 에서 android 플랫폼의 Tooltip 코드를 확인 할 것.
 *
 *  plain 수준의 tooltip에 맞춰서 동작하도록 수정, 툴팁및 caret의 기본 위치를 반전
 */


//TODO : caret 어긋난 부분 수정 필요!
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertSlugTooltip(
    tooltipText: String
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val coroutineScope = rememberCoroutineScope()
    TooltipBox(
        modifier = Modifier,
        positionProvider = rememberSlugPlainTooltipPositionProvider(7.dp),
        tooltip = {
            SlugPlainTooltip(
                modifier = Modifier,
                caretSize = DpSize(12.dp, 5.dp),
                containerColor = Neutral,
                contentColor = Neutral,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = tooltipText,
                    style = SlugTypographyStyle.CaptionMediumMedium,
                    color = NeutralInverted
                )
            }
        },
        state = tooltipState
    ) {
        Box(Modifier.blockingClickable(onClick = {
            coroutineScope.launch {
                tooltipState.show()
            }
        })) {
            ImageProcessor(
                modifier = Modifier.size(16.dp),
                imageResource = ImageResource.Id(R.drawable.ic_info_circle_16_16)
            )
        }
    }
}

@Composable
private fun rememberSlugPlainTooltipPositionProvider(
    spacingBetweenTooltipAndAnchor: Dp = SpacingBetweenTooltipAndAnchor
): PopupPositionProvider {
    val tooltipAnchorSpacing =
        with(LocalDensity.current) { spacingBetweenTooltipAndAnchor.roundToPx() }
    return remember(tooltipAnchorSpacing) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val x = anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
                windowSize.height

                var y = anchorBounds.bottom + tooltipAnchorSpacing

                if (y > windowSize.height)
                    y = anchorBounds.top - tooltipAnchorSpacing - popupContentSize.height

                return IntOffset(x, y)
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun TooltipScope.SlugPlainTooltip(
    modifier: Modifier,
    caretSize: DpSize,
    shape: Shape,
    contentColor: Color,
    containerColor: Color,
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    val drawCaretModifier =
        if (caretSize.isSpecified) {
            val density = LocalDensity.current
            val configuration = LocalConfiguration.current
            Modifier
                .drawCaret { anchorLayoutCoordinates ->
                    drawCaretWithPath(
                        density,
                        configuration,
                        containerColor,
                        caretSize,
                        anchorLayoutCoordinates
                    )
                }
                .then(modifier)
        } else modifier
    Surface(
        modifier = drawCaretModifier,
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation
    ) {
        Box(
            modifier =
                Modifier
                    .sizeIn(
                        minWidth = TooltipMinWidth,
                        maxWidth = PlainTooltipMaxWidth,
                        minHeight = TooltipMinHeight
                    )
                    .padding(PlainTooltipContentPadding)
        ) {

            CompositionLocalProvider(
                LocalContentColor provides contentColor,
                content = content
            )
        }
    }
}


@ExperimentalMaterial3Api
private fun CacheDrawScope.drawCaretWithPath(
    density: Density,
    configuration: Configuration,
    containerColor: Color,
    caretSize: DpSize,
    anchorLayoutCoordinates: LayoutCoordinates?
): DrawResult {
    val path = Path()

    if (anchorLayoutCoordinates != null) {
        val caretHeightPx: Int
        val caretWidthPx: Int
        val screenWidthPx: Int
        val tooltipAnchorSpacing: Int
        with(density) {
            caretHeightPx = caretSize.height.roundToPx()
            caretWidthPx = caretSize.width.roundToPx()
            screenWidthPx = configuration.screenWidthDp.dp.roundToPx()
            tooltipAnchorSpacing = SpacingBetweenTooltipAndAnchor.roundToPx()
        }
        val anchorBounds = anchorLayoutCoordinates.boundsInWindow()
        val anchorLeft = anchorBounds.left
        val anchorRight = anchorBounds.right
        val anchorTop = anchorBounds.top
        val anchorMid = (anchorRight + anchorLeft) / 2
        val anchorWidth = anchorRight - anchorLeft
        val tooltipWidth = this.size.width
        val tooltipHeight = this.size.height


        val isCaretTop = anchorBounds.bottom + tooltipAnchorSpacing > caretHeightPx


        val caretY =
            if (isCaretTop) {
                0f
            } else {
                tooltipHeight
            }

        val position: Offset =
            if (anchorMid + tooltipWidth / 2 > screenWidthPx) {
                // Caret needs to be near the right
                val anchorMidFromRightScreenEdge = screenWidthPx - anchorMid
                val caretX = tooltipWidth - anchorMidFromRightScreenEdge
                Offset(caretX, caretY)
            } else {
                // Caret needs to be near the left
                val tooltipLeft = anchorLeft - (this.size.width / 2 - anchorWidth / 2)
                val caretX = anchorMid - maxOf(tooltipLeft, 0f)
                Offset(caretX, caretY)
            }

        if (isCaretTop) {
            path.apply {
                moveTo(x = position.x, y = position.y)
                lineTo(x = position.x + caretWidthPx / 2, y = position.y)
                lineTo(x = position.x, y = position.y - caretHeightPx)
                lineTo(x = position.x - caretWidthPx / 2, y = position.y)
                close()
            }
        } else {
            path.apply {
                moveTo(x = position.x, y = position.y)
                lineTo(x = position.x + caretWidthPx / 2, y = position.y)
                lineTo(x = position.x, y = position.y + caretHeightPx.toFloat())
                lineTo(x = position.x - caretWidthPx / 2, y = position.y)
                close()
            }
        }
    }

    return onDrawWithContent {
        if (anchorLayoutCoordinates != null) {
            drawContent()
            drawPath(path = path, color = containerColor)
        }
    }
}

private val PlainTooltipVerticalPadding = 4.dp
private val PlainTooltipHorizontalPadding = 8.dp
internal val PlainTooltipContentPadding =
    PaddingValues(PlainTooltipHorizontalPadding, PlainTooltipVerticalPadding)
private val SpacingBetweenTooltipAndAnchor = 7.dp
internal val TooltipMinHeight = 24.dp
internal val TooltipMinWidth = 40.dp
internal val PlainTooltipMaxWidth = 200.dp



@Composable
@Preview(widthDp = 100, heightDp = 100)
fun PreviewSlugTooltip() {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        val tooltipText = "법원이 책정한 입찰을 시작할 수 있는 가장 낮은 가격이에요."
        AlertSlugTooltip(tooltipText)
    }
}