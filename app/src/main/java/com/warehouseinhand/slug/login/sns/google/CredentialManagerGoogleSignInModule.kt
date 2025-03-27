package com.warehouseinhand.slug.login.sns.google

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.warehouseinhand.slug.BuildConfig
import com.warehouseinhand.slug.login.sns.SocialLoginType
import kotlinx.coroutines.launch
import com.warehouseinhand.slug.login.sns.sns.SocialLoginResultCallback

class CredentialManagerGoogleSignInModule(
    private val googleLoginResultCallback: SocialLoginResultCallback
) {
    private lateinit var credentialManager: CredentialManager

    //                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
//                    // Show all accounts on the device.
//                    .setFilterByAuthorizedAccounts(false)
//                    .build()
    fun initForLogin(activity: ComponentActivity) {
        credentialManager = CredentialManager.create(activity)
    }

    fun startLogin(
        activity: ComponentActivity
    ) {
        val signInWithGoogleOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_APP_KEY)
                .build()
        val credentialRequestWithGoogleId: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        activity.lifecycleScope.launch {
            runCatching {
                credentialManager.getCredential(
                    context = activity,
                    request = credentialRequestWithGoogleId
                )
            }.onSuccess { result: GetCredentialResponse ->
                handleSignIn(result)
            }.onFailure { t: Throwable ->
                val customException =
                    Exception("${currentType.name} : getCredential failed with exception: ${t.message.toString()}")
//                Logger.fe(customException)//TODO : 로깅추가
                googleLoginResultCallback.onFailure(customException, currentType)

            }
        }
    }

    private fun createExceptionByMessage(message: String): Exception {
        if (message.contains("User disabled the feature")) {
            return DisabledSignInPromptsException(message)
        }
        return Exception(message)
    }

    //추가적인 정보가 필요하다면 아래의 사이트들을 참조
    //https://github.com/android/identity-samples/tree/main/CredentialManager
    //https://developer.android.com/training/sign-in/credential-manager
    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is GoogleIdTokenCredential -> {
                Log.d(TAG, "Successfully get GoogleIdTokenCredential")
                googleLoginResultCallback.onSuccess(
                    token = credential.idToken,
                    email = credential.id,
                    type = currentType
                )
                return
            }

//            is PublicKeyCredential -> { //Not Use
//                val responseJson = credential.authenticationResponseJson
//            }
//
//            is PasswordCredential -> { //Not Use
//                val username = credential.id
//                val password = credential.password
//                // Use id and password to send to your server to validate
//                // and authenticate
//            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    kotlin.runCatching { GoogleIdTokenCredential.createFrom(credential.data) }
                        .onSuccess { createdCredential ->
                            Log.d(
                                TAG,
                                "Successfully created GoogleIdTokenCredential from CustomCredential"
                            )

                            googleLoginResultCallback.onSuccess(
                                token = createdCredential.idToken,
                                email = createdCredential.id,
                                type = currentType
                            )
                            return
                        }.onFailure { throwable ->
                            //TODO : 로깅추가
//                            Logger.fe("Cannot Create GoogleIdToken From CustomCredential :  Received an invalid google id token response" + throwable.message)
                        }
                } else {
                    //TODO : 로깅추가
//                    Logger.fe("This CustomCredential is not a GoogleIdTokenCredential. Type is ${credential.type}")
                }
            }

            else -> {
                //TODO : 로깅추가
//                Logger.fe(
//                    "Unexpected type of credential class : ${credential::class.java.simpleName}\n" +
//                            "type: ${credential.type}"
//                )

//                val customApiException =
//                    Exception("(Google) signInResult:failed code = ${e.statusCode}", e)
////            Log.d(TAG, "legacyGoogleSignInResultCallback: E = ${e.message}")
////            Logger.fe(customApiException.message!!)//TODO : 로깅추가
//                googleLoginResultCallback.onFailure(customApiException, currentType) // TODO : 살려야함!
            }
        }

    }

    companion object {
        suspend fun signOut(context: Context) {
            kotlin.runCatching {
                CredentialManager.create(context)
                    .clearCredentialState(ClearCredentialStateRequest())
            }
//                .onFailure(Logger::fe)//TODO : 로깅추가
        }

        private val currentType = SocialLoginType.GOOGLE
        private const val TAG = "CredentialManagerGoogleSignInModule"
    }
}