package com.warehouseinhand.slug.login.sns.kakao

import android.app.Activity
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.login.sns.sns.SocialLoginResultCallback
import java.lang.ref.WeakReference
import java.util.Arrays

class KakaoLoginModule(
    private val loadingDialogShowingCallback: (Boolean) -> Unit //TODO : ViewModel로 옮기고 stateFlow로 변경
) {
    lateinit var currentActivity: WeakReference<Activity>
    private lateinit var kaKaoLoginResultCallback: SocialLoginResultCallback

    fun initLoginModuleWithActivity(
        activity: Activity,
        socialLoginResultCallback: SocialLoginResultCallback
    ) {
        currentActivity = WeakReference(activity)
        kaKaoLoginResultCallback = socialLoginResultCallback
    }

    fun startLogin(requireActivity: Activity) {
        loadingDialogShowingCallback(true)
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(
                requireActivity,
                callback = loginKakaoCallback
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireActivity,
                callback = loginKakaoCallback
            )
        }
    }

    private val loginKakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            loadingDialogShowingCallback(false)
            kaKaoLoginResultCallback.onFailure(error, currentType)
        } else if (token != null) {
            // 카카오 서버를 통해 로그인 성공
            getKakaoEmail(token.accessToken)

            kaKaoLoginResultCallback.onSuccess(
                token = token.accessToken,
                email = "",
                type = currentType
            )
        }
    }

    private fun getKakaoEmail(accessToken: String) {
        UserApiClient.instance.me { user, throwable ->
            if (user != null) {
                if (user.kakaoAccount?.email == null) {
                    val scopes: List<String> = Arrays.asList("account_email")
                    currentActivity.get()?.let { activity ->
                        UserApiClient.instance.loginWithNewScopes(
                            activity,
                            scopes
                        ) { token, error ->
                            if (error != null) {
                                kaKaoLoginResultCallback.onSuccess(
                                    token = accessToken,
                                    email = "",
                                    type = currentType
                                )
//                                Log.e("TAG_KAKAO", "추가정보 요청 실패", error)
//                                Firebase.crashlytics.recordException(error)//TODO
                            } else {
//                                Log.i("TAG_KAKAO", "추가정보 요청 성공 ${token?.scopes}".trimIndent())
                                kaKaoLoginResultCallback.onSuccess(
                                    token = accessToken,
                                    email = user.kakaoAccount?.email.toString(),
                                    type = currentType
                                )
                            }
                        }
                    }
                    return@me
                }
                kaKaoLoginResultCallback.onSuccess(
                    token = accessToken,
                    email = user.kakaoAccount?.email.toString(),
                    type = currentType
                )
            } else {
                kaKaoLoginResultCallback.onSuccess(
                    token = accessToken,
                    email = "",
                    type = currentType
                )
            }
        }
    }

    companion object {
        private val currentType = SocialLoginType.KAKAO
        private const val TAG = "KakaoLoginModule"

        internal fun signOut() {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("KakaoLogout", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i("KakaoLogout", "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }
    }
}
