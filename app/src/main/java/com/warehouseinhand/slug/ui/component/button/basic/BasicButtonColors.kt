package com.warehouseinhand.slug.ui.component.button.basic

import androidx.compose.ui.graphics.Color
import com.warehouseinhand.slug.ui.component.button.ButtonState
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralContrast
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.NeutralMuted
import com.warehouseinhand.slug.ui.theme.NeutralSubtle
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.NeutralWhite
import com.warehouseinhand.slug.ui.theme.PrimaryMuted
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.PrimaryContrast


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
        data object PrimaryColor : Fill() {
            override val default =
                ButtonColor.FillButtonColor(text = NeutralWhite, backGround = Primary)
            override val pressed =
                ButtonColor.FillButtonColor(text = NeutralWhite, backGround = PrimaryContrast)
            override val disabled =
                ButtonColor.FillButtonColor(text = NeutralWhite, backGround = PrimaryMuted)
        }

        data object SecondaryColor : Fill() {
            override val default =
                ButtonColor.FillButtonColor(text = Neutral, backGround = NeutralWeak)
            override val pressed =
                ButtonColor.FillButtonColor(text = Neutral, backGround = NeutralMuted)
            override val disabled =
                ButtonColor.FillButtonColor(text = NeutralSubtle, backGround = NeutralWeak)
        }

        data object TertiaryColor : Fill() {
            override val default =
                ButtonColor.FillButtonColor(text = NeutralLight, backGround = Neutral)
            override val pressed =
                ButtonColor.FillButtonColor(text = NeutralLight, backGround = NeutralContrast)
            override val disabled =
                ButtonColor.FillButtonColor(text = NeutralInverted, backGround = NeutralMuted)
        }
    }

    sealed class Ghost : BasicButtonColors() {
        data object PrimaryColor : Ghost() {
            override val default =
                ButtonColor.GhostButtonColor(text = Primary, border = Primary)
            override val pressed =
                ButtonColor.GhostButtonColor(text = PrimaryContrast, border = PrimaryContrast)
            override val disabled =
                ButtonColor.GhostButtonColor(text = PrimaryMuted, border = PrimaryMuted)
        }

        data object SecondaryColor : Ghost() {
            override val default =
                ButtonColor.GhostButtonColor(text = Neutral, border = NeutralMuted)
            override val pressed =
                ButtonColor.GhostButtonColor(text = Neutral, border = NeutralSubtler)
            override val disabled =
                ButtonColor.GhostButtonColor(text = NeutralSubtle, border = NeutralSubtler)
        }
    }
}