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
        @Path(USER_ID) userId: String,
    ): UserDTO

    @DELETE("$CONTEXT/$VERSION/$USERS/{$USER_ID}")
    suspend fun deleteUser(
        @Path(USER_ID) userId: String,
    ): String

    //TODO : 해당 데이터를 USERS~ 에서 받아 온다고 해서 user데이터로 받는게 맞나?
    @GET("$CONTEXT/$VERSION/$USERS/{$USER_ID}/$FAVORITES")
    suspend fun getUserFavoriteProductList(
        @Path(USER_ID) userId: String,
        @Query("type") type: String = "product",
        @Query("cursor") cursor: String? = "unknown",
        @Query("size") size: Int = 20,
    ): FavoriteSalesCursorResponseDTO

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


    @GET("$CONTEXT/$VERSION/$USERS/{$USER_ID}/$FAVORITES/ids")
    suspend fun getFavoriteStatusByIds(
        @Path("userId") userId: String,
        @Query("ids") ids: List<String>
    ): List<FavoriteStatusResponseDTO>


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