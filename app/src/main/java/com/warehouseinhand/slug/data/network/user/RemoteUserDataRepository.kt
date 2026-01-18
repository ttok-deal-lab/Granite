package com.warehouseinhand.slug.data.network.user

import com.warehouseinhand.slug.domain.user.SlugToken
import com.warehouseinhand.slug.domain.user.UserProfile
import javax.inject.Inject

class RemoteUserDataRepository @Inject constructor(
    private val publicUserService: UserPublicService,
    private val privateUserService: UserPrivateService,
) {
    suspend fun postStartAuth(
        snsAccessToken: String,
        provider: String
    ): Result<Pair<SlugToken, UserProfile>> =
        kotlin.runCatching {
            publicUserService.postRegister(
                provider = provider, request = UserPublicService.IdTokenRequest(snsAccessToken)
            ).run {
                token.toDomain() to user.toDomain()
            }
        }

    suspend fun getUserInfo(
        userId: Long
    ): Result<UserProfile> =
        kotlin.runCatching {
            privateUserService.getUserInfo(
                userId = userId
            ).run(UserDTO::toDomain)
        }

    suspend fun addProductFavorite(userId: String, productId: String): Result<String> =
        kotlin.runCatching {
            privateUserService.requestAddFavoriteProduct(userId = userId, productId = productId)
        }
    suspend fun removeProductFavorite(userId: String, productId: String): Result<String> =
        kotlin.runCatching {
            privateUserService.requestRemoveFavoriteProduct(userId = userId, productId = productId)
        }

    suspend fun requestLogout()
            : Result<String> = kotlin.runCatching { privateUserService.logout() }

}