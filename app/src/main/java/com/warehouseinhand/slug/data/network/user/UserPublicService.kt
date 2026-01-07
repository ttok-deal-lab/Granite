package com.warehouseinhand.slug.data.network.user

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserPublicService {
    @POST("$CONTEXT/$VERSION/$AUTH/{$PROVIDER}")
    suspend fun postRegister(
        @Path(PROVIDER) provider: String,
        @Body request: IdTokenRequest,
    ): UserResponseDTO

    data class IdTokenRequest(
        @SerializedName("idToken")
        val idToken: String
    )

    companion object {
        //TODO : API ROUTES로 CONTEXT 와 VERSION 분리
        private const val CONTEXT: String = "api/sherbet-auth"
        private const val VERSION: String = "v1"
        private const val PROVIDER: String = "provider"
        private const val AUTH: String = "auth"
    }
}