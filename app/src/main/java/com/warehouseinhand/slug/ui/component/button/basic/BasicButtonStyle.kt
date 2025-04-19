package com.warehouseinhand.slug.ui.component.button.basic

sealed interface BasicButtonStyle {
    fun getColors(): BasicButtonColors

    enum class Fill(private val colors: BasicButtonColors) : BasicButtonStyle {
        PRIMARY(BasicButtonColors.Fill.Primary),
        SECONDARY(BasicButtonColors.Fill.Secondary),
        TERTIARY(BasicButtonColors.Fill.Tertiary);

        override fun getColors() = colors
    }

    enum class Ghost(private val colors: BasicButtonColors) : BasicButtonStyle {
        PRIMARY(BasicButtonColors.Ghost.Primary),
        SECONDARY(BasicButtonColors.Ghost.Secondary);

        override fun getColors() = colors
    }
}