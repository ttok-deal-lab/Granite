package com.warehouseinhand.slug.ui.component.button.basic

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle

enum class BasicButtonSizeType(private val sizes: BasicButtonSize) {
    LARGE(BasicButtonSize.Large),
    MEDIUM(BasicButtonSize.Medium),
    SMALL(BasicButtonSize.Small),
    MINI(BasicButtonSize.Mini),
    MICRO(BasicButtonSize.Micro),
    ;

    fun getSizes() = sizes
}

sealed class BasicButtonSize {
    abstract val verticalPadding: Dp
    abstract val horizontalPadding: Dp
    abstract val radius: Dp
    abstract val textStyle: TextStyle

    data object Large : BasicButtonSize() {
        override val verticalPadding: Dp = 16.dp
        override val horizontalPadding: Dp = 24.dp
        override val radius: Dp = 12.dp
        override val textStyle: TextStyle = SlugTypographyStyle.BodyLargeBold
    }

    data object Medium : BasicButtonSize() {
        override val verticalPadding: Dp = 12.dp
        override val horizontalPadding: Dp = 20.dp
        override val radius: Dp = 12.dp
        override val textStyle: TextStyle = SlugTypographyStyle.BodyLargeMedium
    }

    data object Small : BasicButtonSize() {
        override val verticalPadding: Dp = 12.dp
        override val horizontalPadding: Dp = 16.dp
        override val radius: Dp = 10.dp
        override val textStyle: TextStyle = SlugTypographyStyle.BodySmallMedium
    }

    data object Mini : BasicButtonSize() {
        override val verticalPadding: Dp = 8.dp
        override val horizontalPadding: Dp = 12.dp
        override val radius: Dp = 8.dp
        override val textStyle: TextStyle = SlugTypographyStyle.BodySmallMedium
    }

    data object Micro : BasicButtonSize() {
        override val verticalPadding: Dp = 7.dp
        override val horizontalPadding: Dp = 10.dp
        override val radius: Dp = 8.dp
        override val textStyle: TextStyle = SlugTypographyStyle.BodyMicroMedium
    }
}