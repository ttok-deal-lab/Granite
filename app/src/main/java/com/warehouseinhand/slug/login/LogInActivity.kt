package com.warehouseinhand.slug.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.login.sns.google.DisabledSignInPromptsException
import com.warehouseinhand.slug.login.sns.sns.SocialLoginModule
import com.warehouseinhand.slug.login.sns.sns.SocialLoginResultCallback
import com.warehouseinhand.slug.ui.theme.SlugTheme

class LogInActivity : ComponentActivity() {
    private lateinit var socialLoginModule: SocialLoginModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initSocialLoginModule()
        setContent {
            SlugTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    private fun initSocialLoginModule() {
        socialLoginModule = SocialLoginModule(
            activity = this,
            loadingDialogShowingCallback = { }
        )
        socialLoginModule.initSocialLoginModules(
            socialLoginResultCallback = socialLoginResultCallback
        )
    }

    private val socialLoginResultCallback = object : SocialLoginResultCallback {
        override fun onSuccess(token: String, email: String, type: SocialLoginType) {
            Log.d("TESTTEST", "onSuccess: ${type.name} Login")
            Log.d(
                "TESTTEST", """
                    |onSuccess: email - $email
                    |onSuccess: token - $token
                    |""".trimMargin()
            )
//            VFFirebaseMessagingService().registerFcmToken()
            if (email.isNotEmpty()) {
//                Firebase.crashlytics.setUserId(email)
            } else {
//                Logger.fe(Throwable(type.name + " email is Empty"))//TODO : Logger 구현
            }

            //SignIn으로 이동! // TODO : 차후구현

//            loginViewModel.requestUserAuth(
//                snsAccessToken = token, type = type,
//                doAfterSuccess = { initialized ->
//                    if (initialized) {
//                        moveToMainActivity()
//                    } else {
//                        navToSignUp()
//                    }
//                })
        }

        override fun onFailure(exception: Throwable, type: SocialLoginType) {
            when {
                exception is DisabledSignInPromptsException -> {
                    showToast("Google 계정 로그인 메시지를 설정 해 주세요.")
                }

//                (type == SocialLoginType.NAVER) && (exception.message?.contains("no_catagorized_error") == true) -> {
//                    showToast("네이버 로그인을 진행할 수 있는 앱이 없습니다.\n 다른 SNS 로그인을 시도해 주세요.")
//                }

                else -> {
                    showToast("로그인을 재시도 해주세요.")
                }
            }

            val customException = Exception("${type.name} : ${exception.localizedMessage}")
            Log.d("TESTTEST", "onFailure ${exception.message}")
//            Logger.fe(customException)//TODO : log 처리
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SlugTheme {
        Greeting2("Android")
    }
}