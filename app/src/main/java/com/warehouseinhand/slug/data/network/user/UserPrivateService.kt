package com.warehouseinhand.slug.data.network.user

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserPrivateService {

    //user Default
    @GET("$CONTEXT/$VERSION/$USERS/{$USER_ID}")
    suspend fun getUserInfo(
        @Path(USER_ID) userId: Long,
    ): UserDTO

    @DELETE("$CONTEXT/$VERSION/$USERS/{$USER_ID}")
    suspend fun deleteUser(
        @Path(USER_ID) userId: Long,
    ): String

    @GET("$CONTEXT/$VERSION/$USERS/{$USER_ID}/$FAVORITES")
    suspend fun getUserFavoriteProductList(
        @Path(USER_ID) userId: Long,
        @Path(PRODUCT_ID) productId: Long,
        @Query("type") type: String = "product",
        @Query("cursor") cursor: String = "unknown",
        @Query("size") size: Int = 20,
    ): String

    @POST("$CONTEXT/$VERSION/$USERS/{$USER_ID}/$FAVORITES/{$PRODUCT_ID}")
    suspend fun requestAddFavoriteProduct(
        @Path(USER_ID) userId: String,
        @Path(PRODUCT_ID) productId: String,
        @Query("type") type: String = "product",
    ): String

    @DELETE("$CONTEXT/$VERSION/$USERS/{$USER_ID}/$FAVORITES/{$PRODUCT_ID}")
    suspend fun requestRemoveFavoriteProduct(
        @Path(USER_ID) userId: String,
        @Path(PRODUCT_ID) productId: String,
        @Query("type") type: String = "product",
    ): String


    @POST("$CONTEXT/$VERSION/$AUTH/$LOGOUT")
    suspend fun logout(): String

    companion object {
        private const val CONTEXT: String = "api/sherbet-auth"
        private const val VERSION: String = "v1"

        private const val AUTH: String = "auth"
        private const val LOGOUT: String = "logout"
        private const val FAVORITES: String = "favorites"

        private const val USERS: String = "users"
        private const val USER_ID: String = "userId"
        private const val PRODUCT_ID: String = "id"

    }
}