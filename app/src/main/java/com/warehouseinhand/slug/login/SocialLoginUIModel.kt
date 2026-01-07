package com.warehouseinhand.slug.login;

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.ui.theme.AppleBlack
import com.warehouseinhand.slug.ui.theme.GoogleWhite
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.KakaoYellow
import com.warehouseinhand.slug.ui.theme.NaverGreen
import com.warehouseinhand.slug.ui.theme.NeutralContrast
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.NeutralWhite

enum class SocialLoginUIModel(
    @StringRes val loginSNSText: Int,
    @DrawableRes val iconId: Int,
    val backgroundColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val type: SocialLoginType,
) {
    GOOGLE(
        loginSNSText = R.string.login_social_google_name,
        backgroundColor = GoogleWhite,
        iconId = R.drawable.googleicon,
        textColor = Neutral,
        borderColor = NeutralWeak,
        type = SocialLoginType.GOOGLE
    ),
    KAKAO(
        loginSNSText = R.string.login_social_kakao_name,
        backgroundColor = KakaoYellow,
        textColor = NeutralContrast,
        iconId = R.drawable.kakaoicon,
        borderColor = Color.Transparent,
        type = SocialLoginType.KAKAO
    ),
    NAVER(
        loginSNSText = R.string.login_social_naver_name,
        backgroundColor = NaverGreen,
        textColor = NeutralWhite,
        iconId = R.drawable.navericon,
        borderColor = Color.Transparent,
        type = SocialLoginType.NAVER
    ),
    APPLE(
        loginSNSText = R.string.login_social_apple_name,
        backgroundColor = AppleBlack,
        textColor = NeutralWhite,
        iconId = R.drawable.appleicon,
        borderColor = Color.Transparent,
        type = SocialLoginType.APPLE
    ),
    ;
    companion object{
        fun byType(type:SocialLoginType):SocialLoginUIModel{
            return entries.find { it.type == type } ?: GOOGLE
        }
    }
}