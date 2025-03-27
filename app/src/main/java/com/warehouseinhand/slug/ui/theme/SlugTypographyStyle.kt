package com.warehouseinhand.slug.ui.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object SlugTypographyStyle {

    /**
     *         fontSize = 14.sp,
     *         fontWeight = FontWeight.Normal,
     *         fontFamily = pretendardFontFamily
     *         */
    val SocialLoginButton: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = pretendardFontFamily
    )

    /**
     *         fontSize = 14.sp,
     *         fontWeight = FontWeight.Normal,
     *         fontFamily = pretendardFontFamily
     *         */
    val LoginSubTitle: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = pretendardFontFamily
    )

    fun TextStyle.removeFontPadding() = copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

}