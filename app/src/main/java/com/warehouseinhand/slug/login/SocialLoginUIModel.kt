package com.warehouseinhand.slug.login;

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.ui.theme.AppleBlack
import com.warehouseinhand.slug.ui.theme.GoogleBlue
import com.warehouseinhand.slug.ui.theme.KakaoYellow
import com.warehouseinhand.slug.ui.theme.NaverGreen
import com.warehouseinhand.slug.ui.theme.PrimaryBlack
import com.warehouseinhand.slug.ui.theme.PrimaryWhite

enum class SocialLoginUIModel(
    @StringRes val localizedName: Int,
    val backgroundColor: Color,
    val textColor: Color,
    val type: SocialLoginType,
) {
    KAKAO(
        localizedName = R.string.login_social_kakao_name,
        backgroundColor = KakaoYellow,
        textColor = PrimaryWhite,
        type = SocialLoginType.KAKAO
    ),
    NAVER(
        localizedName = R.string.login_social_naver_name,
        backgroundColor = NaverGreen,
        textColor = PrimaryWhite,
        type = SocialLoginType.NAVER
    ),
    APPLE(
        localizedName = R.string.login_social_apple_name,
        backgroundColor = AppleBlack,
        textColor = PrimaryWhite,
        type = SocialLoginType.GOOGLE
    ),
    GOOGLE(
        localizedName = R.string.login_social_google_name,
        backgroundColor = GoogleBlue,
        textColor = PrimaryWhite,
        type = SocialLoginType.GOOGLE
    ),
    ;
}