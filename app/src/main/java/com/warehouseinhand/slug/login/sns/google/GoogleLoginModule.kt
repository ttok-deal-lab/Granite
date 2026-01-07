package com.warehouseinhand.slug.login.sns.google

import android.content.Context
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.warehouseinhand.slug.login.sns.sns.SocialLoginResultCallback

class GoogleLoginModule(private val runLegacyIfFail: Boolean = true) {
    //    private lateinit var legacyFailOverModule: LegacyGoogleSignInModule
    private lateinit var credentialManagerGoogleSignInModule: CredentialManagerGoogleSignInModule
    private lateinit var googleLoginResultCallback: SocialLoginResultCallback

    fun initLoginModuleWithActivity(
        activity: ComponentActivity,
        socialLoginResultCallback: SocialLoginResultCallback
    ) {
        googleLoginResultCallback = socialLoginResultCallback
        credentialManagerGoogleSignInModule =
            CredentialManagerGoogleSignInModule(googleLoginResultCallback).apply {
                initForLogin(activity)
            }
//        legacyFailOverModule =
//            LegacyGoogleSignInModule(googleLoginResultCallback)
//                .apply { initForLogin(activity) }
    }

    fun startLogin(activity: ComponentActivity) {
        credentialManagerGoogleSignInModule.startLogin(
            activity = activity
        )

//        oneTapLoginModule.startLogin(
//            activity = activity,
//            runFailOverTry = legacyFailOverModule::runLoginResultLauncher
//        )
    }

    companion object {

        internal suspend fun signOut(context: Context) {
            CredentialManagerGoogleSignInModule.signOut(context)
        }

        internal suspend fun withDraw(context: Context) {
            signOut(context)
        }
    }

}
