package com.warehouseinhand.slug.login.sns.apple

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.login.sns.sns.SocialLoginResultCallback
import java.lang.ref.WeakReference

class AppleLoginModule {
    private lateinit var currentActivity: WeakReference<Activity>
    private lateinit var appleLoginResultCallback: SocialLoginResultCallback
    private lateinit var appleSignInResultLauncher: ActivityResultLauncher<Intent>

    fun initLoginModuleWithActivity(
        activity: ComponentActivity,
        socialLoginResultCallback: SocialLoginResultCallback
    ) {
        currentActivity = WeakReference(activity)
        appleLoginResultCallback = socialLoginResultCallback
        appleSignInResultLauncher = getAppleSignInResultLauncher(activityResultCaller = activity)
    }

    fun startLogin(activity: ComponentActivity) {
        val intent = Intent(activity, AppleLoginActivity::class.java)
        appleSignInResultLauncher.launch(intent)
    }

    private fun getAppleSignInResultLauncher(activityResultCaller: ActivityResultCaller): ActivityResultLauncher<Intent> =
        activityResultCaller.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                appleLoginResultCallback.onFailure(
                    exception = Exception(
                        "failed to get token : resultCode = ${result.resultCode}, " +
                                "message = ${result.data?.getStringExtra("ErrorMessage")}"
                    ),
                    type = currentType
                )
            }
            val token = result.data?.getStringExtra("AppleLoginToken")
            if (token == null) {
                appleLoginResultCallback.onFailure(
                    exception = InvalidTokenNullException(),
                    type = currentType
                )
            } else if (token.isEmpty()) {
                appleLoginResultCallback.onFailure(
                    exception = InvalidTokenEmptyException(),
                    type = currentType
                )
            } else {
                appleLoginResultCallback.onSuccess(token = token, "", currentType)
            }
        }

    companion object {
        private val currentType = SocialLoginType.APPLE
    }

}

class InvalidTokenEmptyException(message: String = "The provided token is empty") :
    Exception(message)

class InvalidTokenNullException(message: String = "The provided token is null") :
    Exception(message)
