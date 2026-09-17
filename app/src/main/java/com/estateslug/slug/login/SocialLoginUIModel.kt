package com.estateslug.slug.login;

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.estateslug.slug.R
import com.estateslug.slug.login.sns.SocialLoginType
import com.estateslug.slug.ui.theme.GoogleWhite
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.KakaoYellow
import com.estateslug.slug.ui.theme.NaverGreen
import com.estateslug.slug.ui.theme.NeutralContrast
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.NeutralWhite
import com.estateslug.slug.ui.theme.SlugTheme

/**
 * SNS 로그인 버튼의 배경·테두리·글자색은 브랜드 자산이라 테마를 따르지 않는다.
 * 의도적으로 고정 상수를 참조하며 SlugTheme.colors로 옮기지 않는다(docs/design-system/dark-color-scheme.md 3-1절).
 *
 * 예외는 Apple이다. Apple HIG(Sign in with Apple)는 검정 스타일을 어두운 바탕에 쓰지 못하게 하고 그때는 흰색 스타일을 쓰게 한다.
 * 그래서 Apple의 채움·글자·로고 색은 고정값(null) 대신 테마 토큰(`appleButtonBackground`/`appleButtonContent`)에서 온다.
 * 화면은 색 필드를 직접 읽지 않고 [resolveBackgroundColor]·[resolveTextColor]·[resolveIconTint]를 쓴다.
 */
enum class SocialLoginUIModel(
    @StringRes val loginSNSText: Int,
    @DrawableRes val iconId: Int,
    private val backgroundColor: Color?,
    val borderColor: Color,
    private val textColor: Color?,
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
        backgroundColor = null, // 테마 토큰 appleButtonBackground
        textColor = null, // 테마 토큰 appleButtonContent
        iconId = R.drawable.appleicon,
        borderColor = Color.Transparent,
        type = SocialLoginType.APPLE
    ),
    ;

    /** 버튼 채움. Apple만 테마를 따른다(라이트 검정 스타일, 다크 흰색 스타일). */
    @Composable
    @ReadOnlyComposable
    fun resolveBackgroundColor(): Color = backgroundColor ?: SlugTheme.colors.appleButtonBackground

    /** 글자색. Apple은 로고와 같은 색이어야 한다. */
    @Composable
    @ReadOnlyComposable
    fun resolveTextColor(): Color = textColor ?: SlugTheme.colors.appleButtonContent

    /** 로고 tint. Apple 로고(단색 자산)만 글자와 같은 색으로 칠하고, 다른 브랜드 로고는 자산 색 그대로 둔다. */
    @Composable
    @ReadOnlyComposable
    fun resolveIconTint(): Color? = if (this == APPLE) SlugTheme.colors.appleButtonContent else null

    companion object{
        fun byType(type:SocialLoginType):SocialLoginUIModel{
            return entries.find { it.type == type } ?: GOOGLE
        }
    }
}
