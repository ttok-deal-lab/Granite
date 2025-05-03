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
//                Log.d("TESTTEST", "activitySignIn:onSuccess ")
                requestToken(authResult)

            }.addOnFailureListener { e ->
//                Log.d("TESTTEST", "activitySignIn:onFailure", e)
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


//Ah

//@AndroidEntryPoint
//class AppleLoginActivity : ComponentActivity() {
//
//    private val mAuthEndpoint = "https://appleid.apple.com/auth/authorize"
//    private val mResponseType = "code%20id_token"
//    private val mResponseMode = "form_post"
//    private val mScope = "name%20email"
//    private val mState = UUID.randomUUID().toString()
//    private val mRedirectUrl = "https://ttokdeal.pursue503.com/oauth/apple"
//
////    @Inject
////    lateinit var appleLoginRepository: AppleLoginRepository
//
////    private val stateCodeForApi: String = currentTimeMillisToBase64Android()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SlugTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    AppleLoginWebView(
//                        url = getAppleLoginUrl(),
//                        onLoginResult = ::requestToken
//                    )
//                }
//            }
//        }
//    }
////
////    private fun currentTimeMillisToBase64Android(): String {
////        val currentTime = System.currentTimeMillis().toString().toByteArray()
////        return Base64.encodeToString(currentTime, Base64.NO_WRAP or Base64.URL_SAFE)
////    }
//
//    private fun requestToken(appleIdToken: String) {
//        finishWithToken(appleIdToken)
//    }
//
//    private fun finishWithError(message: String) {
//        val returnIntent = Intent()
//        returnIntent.putExtra("ErrorMessage", message)
//        setResult(RESULT_CANCELED, returnIntent)
//        finish()
//    }
//
//    private fun finishWithToken(token: String) {
//        val returnIntent = Intent()
//        returnIntent.putExtra("AppleLoginToken", token)
//        setResult(RESULT_OK, returnIntent)
//        finish()
//    }
//
//    private fun getAppleLoginUrl() =
//        mAuthEndpoint +
//                "?response_type=$mResponseType" +
//                "&response_mode=$mResponseMode" +
//                "&client_id=${BuildConfig.APPLE_CLIENT_ID}" +
//                "&scope=$mScope" +
//                "&state=$mState" +
//                "&redirect_uri=$mRedirectUrl"
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun AppleLoginWebView(
//    onLoginResult: (appleIdToken:String) -> Unit,
//    url: String
//) {
//    val client: AccompanistWebViewClient =
//        remember { AppleLoginWebViewClient(onLoginResult = onLoginResult) }
//
//    val webViewState = rememberWebViewState(url = url)
//    CompositionLocalProvider(
//        LocalOverscrollConfiguration provides null
//    ) {
//        WebView(
//            modifier = Modifier,
//            state = webViewState,
//            client = client,
//            onCreated = {
//                runCatching {
//                    it.apply {
//                        settings.javaScriptEnabled = true
//                        settings.domStorageEnabled = true
//                    }
//                }
//            }
//        )
//    }
//}
//
//class AppleLoginWebViewClient(
//    private val onLoginResult: (String) -> Unit
//) : AccompanistWebViewClient() {
//
//    override fun shouldOverrideUrlLoading(
//        view: WebView?,
//        request: WebResourceRequest?
//    ): Boolean {
//        // 로드하려는 URL 받기
//        val url = request?.url
//        Log.d("TESTTEST", "shouldOverrideUrlLoading: URL : $url")
//
//        // 리턴값이 true: 여기서 세팅한 대로 동작 명령
//        // 리턴값이 false: 웹뷰 기본 설정대로 동작 명령
//        return when {
//            // url null 체크용
//            url == null -> {
//                false
//            }
//            // 로드하려는 URL이 리다이렉트 서버의 응답URL이면 로그인 정보 추출하기
//            url.toString().contains("mResponseUrl") -> {
//                val idTokenParam = url.getQueryParameter("id_token")
//                if (idTokenParam != null){
//
//                    // 받은 IdToken 디코딩
//                    val decodedIdToken = decodeJWT(idTokenParam)
//
//                    // 추출한 id token으로 후속 작업
//                    onLoginResult(decodedIdToken)
//                }
//                true
//            }
//            else -> false
//        }
//    }
//    // 애플의 IdToken은 Java Web Token으로, Base64로 인코딩되어 있어 디코딩을 거쳐야 함
//    private fun decodeJWT(JWT: String): String {
//        var decodedJson = ""
//        try {
//            // 토큰의 header와 body를 분리
//            val split = JWT.split("\\.".toRegex()).toTypedArray()
//
//            // header 디코딩 (안 해도 됨)
//            val decodedHeader = Base64.decode(split[0], Base64.URL_SAFE)
//            Log.d("TTT", "header: ${String(decodedHeader, charset("UTF-8"))}")
//
//            // body 디코딩
//            val decodedBody = Base64.decode(split[1], Base64.URL_SAFE)
//            decodedJson = String(decodedBody, charset("UTF-8"))
//            Log.d("TTT", "body: $decodedJson")
//
//        }catch (e: UnsupportedEncodingException){
//            e.printStackTrace()// TODO : 변경되어야함
//        }
//        return decodedJson
//    }
//
////    override fun onPageFinished(view: WebView, url: String?) {
////        super.onPageFinished(view, url)
////        if (url?.contains("code") == true) {
////            // 해당 URL이 열렸을 때만 JavaScript 코드를 주입하여 JSON 데이터를 가져옴
////            view.evaluateJavascript("document.body.textContent") { jsonBody ->
////                val converted = jsonBody.replace("\\\"", "\"").removeSurrounding("\"")
////                runCatching { Gson().fromJson(converted, AppleLoginData::class.java) }
////                    .onSuccess { currentData ->
////                        changeWebViewContent(view)
////                        removeCurrentCookie()
////                        onLoginResult(currentData)
////                    }
////            }
////        }
////    }
//}
//
////    private fun removeCurrentCookie() {
////        val cookieManager = CookieManager.getInstance()
////        val domain = ".whalespace.io"  // 원하는 도메인 입력
////        val cookies = cookieManager.getCookie("https://auth.whalespace.io")
////        cookies?.split(";")?.forEach { cookie ->
////            val cookieName = cookie.split("=")[0].trim()
////            cookieManager.setCookie(domain, "$cookieName=; Expires=Thu, 01 Jan 1970 00:00:00 GMT")
////        }
////        cookieManager.flush()
////    }