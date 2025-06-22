package com.warehouseinhand.slug.ui.component.button.basic

sealed interface BasicButtonStyle {
    fun getColors(): BasicButtonColors

    enum class Fill(private val colors: BasicButtonColors) : BasicButtonStyle {
        PRIMARY(BasicButtonColors.Fill.PrimaryColor),
        SECONDARY(BasicButtonColors.Fill.SecondaryColor),
        TERTIARY(BasicButtonColors.Fill.TertiaryColor);

        override fun getColors() = colors
    }

    enum class Ghost(private val colors: BasicButtonColors) : BasicButtonStyle {
        PRIMARY(BasicButtonColors.Ghost.PrimaryColor),
        SECONDARY(BasicButtonColors.Ghost.SecondaryColor);

        override fun getColors() = colors
    }
}