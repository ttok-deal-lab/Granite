package com.warehouseinhand.slug.login;

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.ui.theme.AppleBlack
import com.warehouseinhand.slug.ui.theme.GoogleWhite
import com.warehouseinhand.slug.ui.theme.Gray10
import com.warehouseinhand.slug.ui.theme.Gray150
import com.warehouseinhand.slug.ui.theme.Gray700
import com.warehouseinhand.slug.ui.theme.Gray900
import com.warehouseinhand.slug.ui.theme.KakaoYellow
import com.warehouseinhand.slug.ui.theme.NaverGreen
import com.warehouseinhand.slug.ui.theme.PrimaryWhite

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
        textColor = Gray700,
        borderColor = Gray150,
        type = SocialLoginType.GOOGLE
    ),
    KAKAO(
        loginSNSText = R.string.login_social_kakao_name,
        backgroundColor = KakaoYellow,
        textColor = Gray900,
        iconId = R.drawable.kakaoicon,
        borderColor = Color.Transparent,
        type = SocialLoginType.KAKAO
    ),
    NAVER(
        loginSNSText = R.string.login_social_naver_name,
        backgroundColor = NaverGreen,
        textColor = Gray10,
        iconId = R.drawable.navericon,
        borderColor = Color.Transparent,
        type = SocialLoginType.NAVER
    ),
    APPLE(
        loginSNSText = R.string.login_social_apple_name,
        backgroundColor = AppleBlack,
        textColor = Gray10,
        iconId = R.drawable.appleicon,
        borderColor = Color.Transparent,
        type = SocialLoginType.APPLE
    ),
    ;
}