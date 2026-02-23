package com.warehouseinhand.slug.ui.component.button.basic

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.component.button.ButtonState
import com.warehouseinhand.slug.util.RemoveOverScroll
import com.warehouseinhand.slug.util.blockingClickable

@Composable
internal fun BasicButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    isDisabled: Boolean = false,
    buttonStyle: BasicButtonStyle = BasicButtonStyle.Fill.PRIMARY,
    sizeType: BasicButtonSizeType = BasicButtonSizeType.LARGE,
    fillWide: Boolean = true,
    content: @Composable BoxScope.(currentSize: BasicButtonSize, currentColor: ButtonColor) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonState: ButtonState = when {
        isDisabled -> ButtonState.Disabled
        isPressed -> ButtonState.Pressed
        else -> ButtonState.Default
    }

    val currentColor = buttonStyle.getColors().byState(buttonState)
    val currentSize = sizeType.getSizes()
    val backgroundShape = RoundedCornerShape(currentSize.radius)
    Box(
        modifier = modifier
            .clip(shape = backgroundShape)
            .background(color = currentColor.backGround)
            .border(width = 1.dp, color = currentColor.border, shape = backgroundShape)
            .padding(
                horizontal = currentSize.horizontalPadding,
                vertical = currentSize.verticalPadding
            )
            .blockingClickable(
                onClick = onButtonClick,
                interactionSource = interactionSource,
                enable = !isDisabled
            ).run {
                if (fillWide)
                    this.fillMaxWidth()
                else
                    this
            },
        contentAlignment = Alignment.Center,
    ) {
        content(currentSize, currentColor)
    }
}

@Composable
internal fun BasicTextButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onButtonClick: () -> Unit,
    isDisabled: Boolean = false,
    buttonStyle: BasicButtonStyle = BasicButtonStyle.Fill.PRIMARY,
    sizeType: BasicButtonSizeType = BasicButtonSizeType.LARGE,
    fillWide: Boolean = true
) {
    BasicButton(
        modifier = modifier,
        onButtonClick = onButtonClick,
        isDisabled = isDisabled,
        buttonStyle = buttonStyle,
        sizeType = sizeType,
        fillWide = fillWide,
        content = { currentSize, currentColor ->
            Text(
                text = buttonText,
                style = currentSize.textStyle,
                color = currentColor.text
            )
        }
    )
}

@Composable
@Preview(heightDp = 1250)
fun PreviewBasicPrimaryTextButton() {
    val context = LocalContext.current
    val toastIt: (String) -> Unit = {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
    RemoveOverScroll {
        Surface(modifier = Modifier.systemBarsPadding()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BasicButtonStyle.Fill.entries.forEach {
                    BasicButtonSizeType.entries.forEach { size ->
                        Row {
                            Box(Modifier.weight(1f)) {
                                BasicTextButton(
                                    buttonText = "Button",
                                    buttonStyle = it,
                                    sizeType = size,
                                    fillWide = false,
                                    onButtonClick = {
                                        toastIt("BUTTON Clicked")
                                    })
                            }
                            Spacer(Modifier.width(8.dp))
                            Box(Modifier.weight(1f)) {
                                BasicTextButton(
                                    buttonText = "Button",
                                    buttonStyle = it,
                                    sizeType = size,
                                    onButtonClick = {},
                                    fillWide = false,
                                    isDisabled = true
                                )
                            }
                        }
                    }
                }

                BasicButtonStyle.Ghost.entries.forEach {
                    BasicButtonSizeType.entries.forEach { size ->
                        Row {
                            Box(Modifier.weight(1f)) {
                                BasicTextButton(
                                    buttonText = "Button",
                                    buttonStyle = it,
                                    fillWide = false,
                                    sizeType = size,
                                    onButtonClick = {
                                        toastIt("BUTTON Clicked")
                                    })
                            }
                            Spacer(Modifier.width(8.dp))
                            Box(Modifier.weight(1f)) {
                                BasicTextButton(
                                    buttonText = "Button",
                                    buttonStyle = it,
                                    fillWide = false,
                                    sizeType = size,
                                    onButtonClick = {},
                                    isDisabled = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
