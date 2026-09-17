package com.estateslug.slug.login;

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.estateslug.slug.R
import com.estateslug.slug.login.sns.SocialLoginType
import com.estateslug.slug.ui.theme.AppleBlack
import com.estateslug.slug.ui.theme.GoogleWhite
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.KakaoYellow
import com.estateslug.slug.ui.theme.NaverGreen
import com.estateslug.slug.ui.theme.NeutralContrast
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.NeutralWhite

/**
 * SNS 로그인 버튼의 배경·테두리·글자색은 브랜드 자산이라 테마를 따르지 않는다.
 * 의도적으로 고정 상수를 참조하며 SlugTheme.colors로 옮기지 않는다(docs/design-system/dark-color-scheme.md 3-1절).
 */
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