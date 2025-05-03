package com.warehouseinhand.slug.data.network

import android.os.Build
import com.warehouseinhand.slug.BuildConfig
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.domain.SlugToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WithAccessToken

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WithOutAccessToken

@Module
@InstallIn(SingletonComponent::class)
internal object RetrofitModule {

    @Singleton
    @Provides
    @WithAccessToken
    fun provideOkHttpClientWithAccessToken(
        localUserDataRepository: LocalUserDataRepository
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor = getLoggingInterceptor())
            .addInterceptor { chain ->
                interceptorForTokenAndError(
                    chain,
                    localUserDataRepository::getUserSlugToken
                )
            }
            .retryOnConnectionFailure(false)
            .build()
    }

    @Singleton
    @Provides
    @WithOutAccessToken
    fun provideOkHttpClientWithOutAccessToken(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor = getLoggingInterceptor())
            .retryOnConnectionFailure(false)
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG)
                setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    private fun interceptorForTokenAndError(
        chain: Interceptor.Chain,
        getToken: suspend () -> Result<SlugToken>,
    ): Response {
//        chain.request().logByNetworkModel(networkConfigModel = networkConfigModel)
        val userToken = runBlocking { getToken().getOrNull() }//TODO 수정되어야함

        //check TokenValidate
//        val validationResult = validateToken(networkConfigModel)

//        if (validationResult.isFailure) {
//            val exception = validationResult.exceptionOrNull()
//            if (exception is TokenValidationException) {
//                return Response.Builder()
//                    .code(401)  // Unauthorized
//                    .message(exception.message ?: "Token validation failed")
//                    .protocol(Protocol.HTTP_1_1)
//                    .request(chain.request())
//                    .body(
//                        (exception.message ?: "Token validation failed").toResponseBody(
//                            "application/json".toMediaTypeOrNull()
//                        )
//                    )
//                    .build()
//            }
//        }

        if (userToken == null) {
            return Response.Builder()
                .code(401)  // Unauthorized
                .message("Token Not Exist")
                .protocol(Protocol.HTTP_1_1)
                .request(chain.request())
                .body(
                    ("Token validation failed").toResponseBody(
                        "application/json".toMediaTypeOrNull()
                    )
                )
                .build()
        }

        //Add Access
        val originalPath = chain.request().url.encodedPath
        var currentResponse = responseWithAccess(chain, userToken.accessToken)

        //REFRESH TODO: 리프래시 구현이후 진행
//        if (currentResponse.isNeedToRefresh) {
//            currentResponse.closeQuietly()
//            currentResponse = responseWithRefreshed(chain, networkConfigModel)
//        }

        // Successful Response
        if (currentResponse.isSuccessful) {
            return currentResponse
//            return loggingAndLoggedResponse(
//                response = currentResponse,
//                log = networkConfigModel::setNetWorkLogByFireBase
//            )
        }

        // proceed ERROR
        return responseByError(
            currentResponse = currentResponse,
            chain = chain,
            originalPath = originalPath
        )
    }

    private fun loggingAndLoggedResponse(response: Response, log: (String) -> Unit): Response {
        val responseBodyBytes = getResponseBody(response)

        // 로깅
        log(
            StringBuilder()
                .append("method : ${response.request.method}   ")
                .append("resultCode : ${response.code} ")
                .append("  url : ${response.request.url}")
                .toString()
        )
        log(String(responseBodyBytes))


        // 새로운 ResponseBody 생성
        val newResponseBody =
            responseBodyBytes.toResponseBody(response.body?.contentType())

        // 새로운 Response 객체 반환
        return response.newBuilder()
            .body(newResponseBody)
            .build()
    }

    private fun getResponseBody(response: Response): ByteArray {
        val bodyResult = kotlin.runCatching { response.body?.bytes() }
        if (bodyResult.isSuccess) {
            return bodyResult.getOrNull() ?: EMPTY_BODY
        }
        //Only Failed Results
        val exception: Throwable = bodyResult.exceptionOrNull() ?: return ERROR_UNKNOWN_NULL_BODY
        return "${exception::class.java.simpleName} : ${exception.message}".toByteArray()
    }


    private fun responseByError(
        currentResponse: Response,
        chain: Interceptor.Chain,
        originalPath: String? = null
    ): Response {

        val errorBodyStr = currentResponse.body?.string()
        val errorCode = currentResponse.code
        val errorPath = currentResponse.request.url.encodedPath
        val errorMessage = StringBuilder().apply {
            append("errorCode  = $errorCode \n")
            if (errorPath != originalPath && !originalPath.isNullOrEmpty())
                append("originalPath = ${originalPath}\n")
            append("currentPath = ${errorPath}\n")
            append("errorBody = ${errorBodyStr}\n")
        }.toString()
        // Create error Response
        val errorResponseBody = errorMessage
            .toResponseBody("application/json".toMediaTypeOrNull())

        return Response.Builder()
            .code(errorCode)
            .message(errorMessage)
            .body(errorResponseBody)
            .protocol(currentResponse.protocol)
            .request(chain.request())
            .build()
    }

    private val Response.isNeedToRefresh: Boolean
        get() = code == 401 || code == 403

    private val EMPTY_BODY = "EMPTY BODY".toByteArray()
    private val ERROR_UNKNOWN_NULL_BODY = "ERROR BUT UNKNOWN IS NULL".toByteArray()


    //토큰 및 Agent
    private fun responseWithAccess(
        chain: Interceptor.Chain,
        token: String
    ): Response {
        val accessTokenAddedOriginalRequest = chain.request()
            .newBuilder()
            .setUserAgent()
            .setAccessToken(token)
            .build()
        return chain.proceed(accessTokenAddedOriginalRequest)
    }

    private fun Request.Builder.setAccessToken(accessToken: String) = removeHeader("Authorization")
        .addHeader("Authorization", "Bearer $accessToken")


    private fun Request.Builder.setUserAgent() = removeHeader("User-Agent")
        .addHeader("User-Agent", getUserAgent())


    private fun getUserAgent(): String {
        val appName = if (BuildConfig.DEBUG) "Granite-DEV" else "Granite"
        val applicationId = BuildConfig.APPLICATION_ID
        val versionName = BuildConfig.VERSION_NAME
        val deviceModel = Build.MODEL
        val versionCode = BuildConfig.VERSION_CODE
        val osVersion = Build.VERSION.RELEASE

        return "${appName}/${versionName} " +
                " (package:${applicationId}; " +
                "build:${versionCode}; " +
                "Android ${osVersion}; " +
                "Model:$deviceModel; ) " +
                "Retrofit2"
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

}