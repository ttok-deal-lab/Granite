package com.estateslug.slug.ui.component.button.basic

import androidx.compose.ui.graphics.Color
import com.estateslug.slug.ui.component.button.ButtonState
import com.estateslug.slug.ui.theme.SlugColors


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

/**
 * 버튼 상태별 색은 테마 토큰([SlugColors])에서 읽는다. 라이트는 눌리면 어두워지고 비활성은 밝아지지만
 * 다크는 눌리면 밝아지고 비활성은 컨테이너 단계로 가라앉으므로, 상태 순서를 상수가 아니라 테마가 소유한다.
 * 매핑 근거: docs/design-system/dark-color-scheme.md 4절.
 */
sealed class BasicButtonColors {
    abstract fun default(colors: SlugColors): ButtonColor
    abstract fun pressed(colors: SlugColors): ButtonColor
    abstract fun disabled(colors: SlugColors): ButtonColor

    fun byState(colors: SlugColors, state: ButtonState): ButtonColor =
        when (state) {
            ButtonState.Default -> default(colors)
            ButtonState.Pressed -> pressed(colors)
            ButtonState.Disabled -> disabled(colors)
        }

    sealed class Fill : BasicButtonColors() {
        data object PrimaryColor : Fill() {
            override fun default(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.onPrimary, backGround = colors.primary)

            override fun pressed(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.onPrimary, backGround = colors.primaryContrast)

            override fun disabled(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.buttonPrimaryDisabledText, backGround = colors.primaryMuted)
        }

        data object SecondaryColor : Fill() {
            override fun default(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.neutral, backGround = colors.neutralWeak)

            override fun pressed(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.neutral, backGround = colors.neutralMuted)

            override fun disabled(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.neutralDisabled, backGround = colors.neutralWeak)
        }

        data object TertiaryColor : Fill() {
            override fun default(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.onTertiary, backGround = colors.neutral)

            override fun pressed(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.onTertiary, backGround = colors.buttonTertiaryPressed)

            override fun disabled(colors: SlugColors) =
                ButtonColor.FillButtonColor(text = colors.buttonTertiaryDisabledText, backGround = colors.neutralMuted)
        }
    }

    sealed class Ghost : BasicButtonColors() {
        data object PrimaryColor : Ghost() {
            override fun default(colors: SlugColors) =
                ButtonColor.GhostButtonColor(text = colors.primary, border = colors.primary)

            override fun pressed(colors: SlugColors) =
                ButtonColor.GhostButtonColor(text = colors.primaryContrast, border = colors.primaryContrast)

            override fun disabled(colors: SlugColors) =
                ButtonColor.GhostButtonColor(
                    text = colors.buttonGhostPrimaryDisabled,
                    border = colors.buttonGhostPrimaryDisabled
                )
        }

        data object SecondaryColor : Ghost() {
            override fun default(colors: SlugColors) =
                ButtonColor.GhostButtonColor(text = colors.neutral, border = colors.buttonGhostSecondaryBorder)

            override fun pressed(colors: SlugColors) =
                ButtonColor.GhostButtonColor(text = colors.neutral, border = colors.buttonGhostSecondaryBorderPressed)

            override fun disabled(colors: SlugColors) =
                ButtonColor.GhostButtonColor(
                    text = colors.neutralDisabled,
                    border = colors.buttonGhostSecondaryBorderDisabled
                )
        }
    }
}
