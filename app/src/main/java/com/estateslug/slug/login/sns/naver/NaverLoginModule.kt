package com.estateslug.slug.login.sns.naver

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.estateslug.slug.BuildConfig
import com.estateslug.slug.R
import com.estateslug.slug.login.sns.SocialLoginType
import com.estateslug.slug.util.SharedPreferenceManager
import kotlinx.coroutines.launch
import com.estateslug.slug.login.sns.sns.SocialLoginResultCallback

class NaverLoginModule {
    lateinit var naverLoginResultCallback: SocialLoginResultCallback

    //TODO : 로깅 관련 보강 고려하기
    //oauthLoginCallback은 로그인 성공여부만 확인 데이터를 완전히 받아 온 것이 아님!
    private val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onError(errorCode: Int, message: String) = onFailure(errorCode, message)

        override fun onFailure(httpStatus: Int, message: String) = onNaverLoginFail()

        override fun onSuccess() = onNaverLoginSuccess()
    }

    private fun initLoginModuleWithActivity(
        activity: Activity,
        socialLoginResultCallback: SocialLoginResultCallback
    ) {
        naverLoginResultCallback = socialLoginResultCallback
        initializeNaverLoginModule(activity)
    }

    fun startLogin(activity: Activity) {
        NaverIdLoginSDK.authenticate(activity, oauthLoginCallback)
    }

    private fun onNaverLoginSuccess() {
        // 네이버 로그인 인증성공
        // User의 정보를 받아옴.
        NidOAuthLogin().callProfileApi(getNaverProfileCallback())
    }

    private fun getNaverProfileCallback() = object : NidProfileCallback<NidProfileResponse> {
        override fun onError(errorCode: Int, message: String) = onFailure(errorCode, message)

        override fun onFailure(httpStatus: Int, message: String) = onNaverLoginFail()

        override fun onSuccess(result: NidProfileResponse) {
            val naverAccessToken = NaverIdLoginSDK.getAccessToken()
            val userEmail: String = getEmailFromNidProfileResponse(result)

            if (naverAccessToken == null) {
                val message = "Fail to login, AccessToken is null"
                naverLoginResultCallback.onFailure(Exception(message), currentType)
                return
            }

            //finally Success
            naverLoginResultCallback.onSuccess(
                token = naverAccessToken,
                email = userEmail,
                SocialLoginType.NAVER
            )
        }
    }

    private fun onNaverLoginFail() {
        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
        val message = "OAuth API ERROR Code: $errorCode, description: $errorDescription"
        val exception = Exception(message)

        naverLoginResultCallback.onFailure(exception, SocialLoginType.NAVER)

//        Logger.fe("(Naver) $message") //TODO 로깅 추가
    }

    private fun getEmailFromNidProfileResponse(result: NidProfileResponse): String {
        val profile = result.profile
        if (profile == null) {
//            Logger.fe("Getting Naver Profile ERROR") //TODO 로깅 추가
            return ""
        }
        val email = profile.email
        if (email == null) {
//            Logger.fe("Getting Naver Email ERROR") //TODO 로깅 추가
            return ""
        }

        return email
    }

    internal fun initNaverLogin(
        activity: ComponentActivity,
        socialLoginResultCallback: SocialLoginResultCallback,
        whenNaverLoginInitFailed: (Throwable) -> Unit
    ) {
        kotlin.runCatching {
            initLoginModuleWithActivity(activity, socialLoginResultCallback)
        }.onFailure { exception ->
            //TODO : LOG 추가부분

            activity.lifecycleScope.launch {
                SharedPreferenceManager.deleteNaverLoginData(activity)
                    .onSuccess {
//                        Log.d(TAG, "Naver data remove success")
                        initLoginModuleWithActivity(
                            activity,
                            socialLoginResultCallback
                        )
                    }.onFailure {
                        whenNaverLoginInitFailed.invoke(it)
                    }
            }
        }
    }

    companion object {
        private val currentType = SocialLoginType.NAVER
        internal fun initializeNaverLoginModule(context: Context) {
            NaverIdLoginSDK.initialize(
                context,
                BuildConfig.NAVER_CLIENT_ID,
                BuildConfig.NAVER_CLIENT_SECRET,
                context.getString(R.string.login_social_naver_name)
            )
        }

        internal fun withDraw(context: Context) {
            initializeNaverLoginModule(context)
            NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
                    override fun onSuccess() {
                        // 서버에서 토큰 삭제에 성공한 상태입니다.
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                        // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                    }

                    override fun onError(errorCode: Int, message: String) {
                        // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                        // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                        onFailure(errorCode, message)
                    }
                }
            )
        }

        internal fun signOut(context: Context) {
            kotlin.runCatching {
                NaverIdLoginSDK.logout()
            }.onFailure {
                initializeNaverLoginModule(context)
                NaverIdLoginSDK.logout()
            }
        }
    }
}
