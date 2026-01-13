package com.warehouseinhand.slug.data.network.user

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserPrivateService {

    //user Default
    @GET("$CONTEXT/$VERSION/$USERS/{$USER_ID}")
    suspend fun getUserInfo(
        @Path(USER_ID) userId: Long,
    ): UserDTO

    @DELETE("$CONTEXT/$VERSION/$USERS/{$USER_ID}")
    suspend fun deleteUser(
        @Path(USER_ID) userId: String,
    ): String

    @POST("$CONTEXT/$VERSION/$AUTH}")
    suspend fun logout(): String

    companion object {
        private const val CONTEXT: String = "api/sherbet-auth"
        private const val VERSION: String = "v1"

        private const val AUTH: String = "auth"

        private const val USERS: String = "users"
        private const val USER_ID: String = "userId"

    }
}