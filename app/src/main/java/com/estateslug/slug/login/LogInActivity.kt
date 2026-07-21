package com.estateslug.slug.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.estateslug.slug.firebase.SlugFirebaseMessagingService
import com.estateslug.slug.login.sns.SocialLoginType
import com.estateslug.slug.login.sns.google.DisabledSignInPromptsException
import com.estateslug.slug.login.sns.sns.SocialLoginModule
import com.estateslug.slug.login.sns.sns.SocialLoginResultCallback
import com.estateslug.slug.deeplink.DeepLinkKeys
import com.estateslug.slug.deeplink.DeepLinkRouterActivity
import com.estateslug.slug.main.MainActivity
import com.estateslug.slug.permission.PermissionChecker
import com.estateslug.slug.permission.PermissionRequestActivity
import com.estateslug.slug.ui.component.dialog.OneButtonAlertDialog
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.util.moveToStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            val forceUpdateInfo by loginViewModel.forceUpdateInfo.collectAsStateWithLifecycle()
            SlugTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        LoginPage(
                            onSocialLoginSelected = ::onSocialLoginSelected,
                            lastLoginType
                        )
                    }
                }
                forceUpdateInfo?.let { updateInfo ->
                    Dialog(
                        onDismissRequest = {},
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        )
                    ) {
                        OneButtonAlertDialog(
                            title = updateInfo.updateMsgTitle,
                            description = updateInfo.updateMsgDescription,
                            buttonText = "업데이트",
                            onButtonClick = { moveToStore(this@LogInActivity) }
                        )
                    }
                }
            }
        }
        delaySplash()
    }

    private fun delaySplash() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (loginViewModel.isReadyToRemoveSplash.value) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )
    }


    override fun onResume() {
        super.onResume()
        if (!loginViewModel.isReadyToRemoveSplash.value)
            doInSplash()
    }

    private fun doInSplash() {
        loginViewModel.checkForceUpdate(
            onUpdateNotRequired = {
                loginViewModel.checkTokenValidate(
                    doOnSuccess = {
                        moveToNextActivity()
                    }
                )
            }
        )
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
//            Logger.fe(customException)//TODO : log 처리
        }
    }

    private fun requestUserAuth(snsAccessToken: String, type: SocialLoginType) {
        //AfterRequest
        loginViewModel.requestUserAuth(
            snsAccessToken = snsAccessToken, type = type,
            doAfterSuccess = {
                moveToNextActivity()
            })
    }

    private fun moveToNextActivity() {
        lifecycleScope.launch {
            // 딥링크로 진입해 로그인한 경우 원 목적지(라우터)로 이어보냄 (권한 인트로 생략 — 딥링크 목적지 우선)
            val pending = intent?.getStringExtra(DeepLinkKeys.PENDING_DEEPLINK)
            if (!pending.isNullOrBlank()) {
                val nextIntent = Intent(this@LogInActivity, DeepLinkRouterActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(pending)
                }
                startActivity(nextIntent)
            } else if (!PermissionChecker.isAllOfEssentialAllowed(this@LogInActivity) ||
                (PermissionChecker.isNotificationPermissionMissing(this@LogInActivity) &&
                    loginViewModel.shouldShowNotificationPermissionIntro())
            ) {
                loginViewModel.markNotificationPermissionIntroShown()
                moveToPermissionCheck()
            } else {
                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            }
            finish()
        }
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
