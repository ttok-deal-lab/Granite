package com.warehouseinhand.slug.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.warehouseinhand.slug.firebase.SlugFirebaseMessagingService
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.login.sns.google.DisabledSignInPromptsException
import com.warehouseinhand.slug.login.sns.sns.SocialLoginModule
import com.warehouseinhand.slug.login.sns.sns.SocialLoginResultCallback
import com.warehouseinhand.slug.permission.PermissionRequestActivity
import com.warehouseinhand.slug.ui.theme.SlugTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity : ComponentActivity() {
    private lateinit var socialLoginModule: SocialLoginModule
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initSocialLoginModule()
        loginViewModel.requestLastLoginType()
        setContent {
            val lastLoginType by loginViewModel.lastLoginType.collectAsStateWithLifecycle()
            SlugTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        LoginPage(
                            onSocialLoginSelected = ::onSocialLoginSelected,
                            lastLoginType
                        )
                    }
                }
            }
        }
    }

    private fun onSocialLoginSelected(type: SocialLoginType) {
        socialLoginModule.socialLoginByType(type)
    }

    private fun moveToPermissionCheck() {
        val intent = Intent(this, PermissionRequestActivity::class.java)
        startActivity(intent)
    }

    private fun initSocialLoginModule() {
        //네이버 로그인 이슈발생시 활성화 해야하는 부분.

//        val whenNaverLoginInitFailed: (Throwable) -> Unit =
//            { throwable: Throwable ->
////                Log.d(TAG, "Naver data remove fail")
////                Logger.fe(throwable)
//                //Naver비활성화 필요?
////                CoroutineScope(Dispatchers.Main).launch {
////                    binding.btnLoginNaver.apply {
////                        isCheckable = false
////                        focusable = View.NOT_FOCUSABLE
////                    }
////                }
//            }

        socialLoginModule = SocialLoginModule(
            activity = this,
            loadingDialogShowingCallback = { }
        )
        socialLoginModule.initSocialLoginModules(
            socialLoginResultCallback = socialLoginResultCallback,
            whenNaverLoginInitFailed = { /*whenNaverLoginInitFailed*/ }
        )
    }

    private val socialLoginResultCallback = object : SocialLoginResultCallback {
        override fun onSuccess(token: String, email: String, type: SocialLoginType) {
//            Log.d("TESTTEST", "onSuccess: ${type.name} Login")
//            Log.d(
//                "TESTTEST", """
//                    |onSuccess: email - $email
//                    |onSuccess: token - $token
//                    |""".trimMargin()
//            )
            SlugFirebaseMessagingService().registerFcmToken()
            if (email.isNotEmpty()) {
                Firebase.crashlytics.setUserId(email)
            } else {
//                Logger.fe(Throwable(type.name + " email is Empty"))//TODO : Logger 구현
            }
            requestUserAuth(snsAccessToken = token, type = type)
        }

        override fun onFailure(exception: Throwable, type: SocialLoginType) {
            when {
                exception is DisabledSignInPromptsException -> {
                    showToast("Google 계정 로그인 메시지를 설정 해 주세요.")
                }

                //TODO NAVER 처리
//                (type == SocialLoginType.NAVER) && (exception.message?.contains("no_catagorized_error") == true) -> {
//                    showToast("네이버 로그인을 진행할 수 있는 앱이 없습니다.\n 다른 SNS 로그인을 시도해 주세요.")
//                }

                else -> {
                    showToast("로그인을 재시도 해주세요.")
                }
            }
//            val customException = Exception("${type.name} : ${exception.localizedMessage}")
//            Log.d("TESTTEST", "onFailure ${exception.message}")
//            Logger.fe(customException)//TODO : log 처리
        }
    }

    private fun requestUserAuth(snsAccessToken: String, type: SocialLoginType) {
        //AfterRequest
        loginViewModel.requestUserAuth(
            snsAccessToken = snsAccessToken, type = type,
            doAfterSuccess = {
//                    Log.d("TESTTEST", "requestUserAuth onSuccess:!! ")
                moveToPermissionCheck()
            })
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    @Preview
    @Composable
    private fun Preview() {
        LoginPage(
            onSocialLoginSelected = {},
            lastUsedLoginType = SocialLoginType.GOOGLE
        )
    }

}
