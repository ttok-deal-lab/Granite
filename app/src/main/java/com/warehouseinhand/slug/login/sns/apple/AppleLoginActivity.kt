package com.warehouseinhand.slug.login.sns.apple

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AppleLoginActivity : ComponentActivity() {

    private lateinit var mProvider: OAuthProvider.Builder
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAuth()
        checkPending()
    }

    // 1. 인증 API 초기화
    private fun initAuth() {
        mProvider = OAuthProvider.newBuilder("apple.com")
        mProvider.scopes = listOf("email", "name") //로그인 후 받고 싶은 유저 정보 범위
        mProvider.addCustomParameter("locale", "ko")

        mAuth = FirebaseAuth.getInstance()
    }

    // 2. 이미 받은 응답이 있는지 확인
    private fun checkPending() {
        val pending = mAuth.pendingAuthResult
        if (pending != null) {
            pending.addOnSuccessListener { authResult ->
                Log.d("TTT", "checkPending:onSuccess:$authResult")

                //로그인 결과 및 유저 정보가 AuthResult 객체에 담겨서 받아짐
                //이 객체로 후속 작업 진행

            }.addOnFailureListener { e ->
                Log.d("TTT", "checkPending:onFailure", e)
                finishWithError(e.localizedMessage)
            }
        } else {
            startAuth()
        }
    }

    // 3. 이미 받은 응답이 없다면 로그인 절차 시작
    private fun startAuth() {
        mAuth.startActivityForSignInWithProvider(this, mProvider.build())
            .addOnSuccessListener { authResult: AuthResult ->
                //로그인 결과 및 유저 정보가 AuthResult 객체에 담겨서 받아짐
                //이 객체로 후속 작업 진행
                requestToken(authResult)

            }.addOnFailureListener { e ->
                finishWithError(e.localizedMessage)
            }
    }

    private fun requestToken(authResult: AuthResult) {
        val oAuthCredential = authResult.credential as OAuthCredential
        val token = oAuthCredential.idToken ?: return finishWithError("token is null")
        finishWithToken(token)
    }

    private fun finishWithError(message: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("ErrorMessage", message)
        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }

    private fun finishWithToken(token: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("AppleLoginToken", token)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    //TODO : user backpress 에러 추가.


}