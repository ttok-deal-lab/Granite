package com.warehouseinhand.slug.ui.component.button.basic

import androidx.compose.ui.graphics.Color
import com.warehouseinhand.slug.ui.component.button.ButtonState
import com.warehouseinhand.slug.ui.theme.Gray10
import com.warehouseinhand.slug.ui.theme.Gray100
import com.warehouseinhand.slug.ui.theme.Gray200
import com.warehouseinhand.slug.ui.theme.Gray300
import com.warehouseinhand.slug.ui.theme.Gray400
import com.warehouseinhand.slug.ui.theme.Gray50
import com.warehouseinhand.slug.ui.theme.Gray700
import com.warehouseinhand.slug.ui.theme.Gray800
import com.warehouseinhand.slug.ui.theme.Primary150
import com.warehouseinhand.slug.ui.theme.Primary200
import com.warehouseinhand.slug.ui.theme.Primary400


sealed class ButtonColor {
    abstract val text: Color
    abstract val backGround: Color
    abstract val border: Color

    class FillButtonColor(
        override val text: Color,
        override val backGround: Color
    ) : ButtonColor() {
        override val border: Color = Color.Transparent
    }

    class GhostButtonColor(
        override val text: Color,
        override val border: Color
    ) : ButtonColor() {
        override val backGround: Color = Color.Transparent
    }
}

sealed class BasicButtonColors {
    abstract val default: ButtonColor
    abstract val pressed: ButtonColor
    abstract val disabled: ButtonColor

    fun byState(state: ButtonState) =
        when (state) {
            ButtonState.Default -> default
            ButtonState.Pressed -> pressed
            ButtonState.Disabled -> disabled
        }

    sealed class Fill : BasicButtonColors() {
        data object Primary : Fill() {
            override val default =
                ButtonColor.FillButtonColor(text = Gray10, backGround = Primary200)
            override val pressed =
                ButtonColor.FillButtonColor(text = Gray10, backGround = Primary400)
            override val disabled =
                ButtonColor.FillButtonColor(text = Gray10, backGround = Primary150)
        }

        data object Secondary : Fill() {
            override val default =
                ButtonColor.FillButtonColor(text = Gray700, backGround = Gray100)
            override val pressed =
                ButtonColor.FillButtonColor(text = Gray700, backGround = Gray200)
            override val disabled =
                ButtonColor.FillButtonColor(text = Gray300, backGround = Gray100)
        }

        data object Tertiary : Fill() {
            override val default =
                ButtonColor.FillButtonColor(text = Gray50, backGround = Gray700)
            override val pressed =
                ButtonColor.FillButtonColor(text = Gray50, backGround = Gray800)
            override val disabled =
                ButtonColor.FillButtonColor(text = Gray10, backGround = Gray200)
        }
    }

    sealed class Ghost : BasicButtonColors() {
        data object Primary : Ghost() {
            override val default =
                ButtonColor.GhostButtonColor(text = Primary200, border = Primary200)
            override val pressed =
                ButtonColor.GhostButtonColor(text = Primary400, border = Primary400)
            override val disabled =
                ButtonColor.GhostButtonColor(text = Primary150, border = Primary150)
        }

        data object Secondary : Ghost() {
            override val default =
                ButtonColor.GhostButtonColor(text = Gray700, border = Gray200)
            override val pressed =
                ButtonColor.GhostButtonColor(text = Gray700, border = Gray400)
            override val disabled =
                ButtonColor.GhostButtonColor(text = Gray300, border = Gray300)
        }
    }
}