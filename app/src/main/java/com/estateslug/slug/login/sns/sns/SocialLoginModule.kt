package com.estateslug.slug.login.sns.sns

import android.content.Context
import androidx.activity.ComponentActivity
import com.estateslug.slug.login.sns.SocialLoginType
import com.estateslug.slug.login.sns.apple.AppleLoginModule
import com.estateslug.slug.login.sns.google.GoogleLoginModule
import com.estateslug.slug.login.sns.kakao.KakaoLoginModule
import com.estateslug.slug.login.sns.naver.NaverLoginModule

class SocialLoginModule(
    private val activity: ComponentActivity,
    loadingDialogShowingCallback: (Boolean) -> Unit
) {
    private val googleLoginModule: GoogleLoginModule = GoogleLoginModule()
    private val naverLoginModule: NaverLoginModule = NaverLoginModule()
    private val appleLoginModule: AppleLoginModule = AppleLoginModule()
    private val kakaoLoginModule: KakaoLoginModule = KakaoLoginModule(loadingDialogShowingCallback)
    private val TAG = this.javaClass.simpleName

    fun socialLoginByType(type: SocialLoginType) {
        when (type) {
            SocialLoginType.NAVER -> naverLoginModule.startLogin(activity)
            SocialLoginType.KAKAO -> kakaoLoginModule.startLogin(activity)
            SocialLoginType.GOOGLE -> googleLoginModule.startLogin(activity)
            SocialLoginType.APPLE -> appleLoginModule.startLogin(activity)
            SocialLoginType.NEVER_LOGIN -> {/* DO NOTING*/}
        }
    }

    fun initSocialLoginModules(
        socialLoginResultCallback: SocialLoginResultCallback,
        whenNaverLoginInitFailed: (Throwable) -> Unit
    ) {
        googleLoginModule.initLoginModuleWithActivity(
            activity = activity,
            socialLoginResultCallback = socialLoginResultCallback
        )
        kakaoLoginModule.initLoginModuleWithActivity(
            activity = activity,
            socialLoginResultCallback = socialLoginResultCallback
        )
        appleLoginModule.initLoginModuleWithActivity(
            activity = activity,
            socialLoginResultCallback = socialLoginResultCallback,
        )
        naverLoginModule.initNaverLogin(
            activity = activity,
            socialLoginResultCallback = socialLoginResultCallback,
            whenNaverLoginInitFailed = whenNaverLoginInitFailed
        )
    }

    companion object {
       suspend fun requestLogOut(context: Context) {//TODO : 실패시에 로깅할 방법생각해야함.
            GoogleLoginModule.signOut(context)
            NaverLoginModule.signOut(context)
            KakaoLoginModule.signOut()
        }

       suspend fun requestWithDraw(context: Context) {
            GoogleLoginModule.withDraw(context)
            NaverLoginModule.withDraw(context)
            KakaoLoginModule.signOut()// KaKao는 로그아웃만으로 처리됨
        }

    }
}