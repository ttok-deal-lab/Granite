package com.estateslug.slug.data.network.user

import com.estateslug.slug.data.network.MapperToDomain.Companion.toDomain
import com.estateslug.slug.domain.search.AuctionSearchItem
import com.estateslug.slug.domain.user.FavoriteStatus
import com.estateslug.slug.domain.user.SlugToken
import com.estateslug.slug.domain.user.UserProfile
import com.estateslug.slug.util.CursorPage
import javax.inject.Inject

class RemoteUserDataRepository @Inject constructor(
    private val publicUserService: UserPublicService,
    private val privateUserService: UserPrivateService,
) {
    suspend fun postStartAuth(
        snsAccessToken: String,
        provider: String
    ): Result<Pair<SlugToken, UserProfile>> =
        runCatching {
            publicUserService.postRegister(
                provider = provider, request = UserPublicService.IdTokenRequest(snsAccessToken)
            ).run {
                token.toDomain() to user.toDomain()
            }
        }

    suspend fun getUserInfo(
        userId: String
    ): Result<UserProfile> =
        runCatching {
            privateUserService.getUserInfo(
                userId = userId
            ).run(UserDTO::toDomain)
        }

    suspend fun addProductFavorite(userId: String, productId: String): Result<String> =
        runCatching {
            privateUserService.requestAddFavoriteProduct(userId = userId, productId = productId)
        }

    suspend fun removeProductFavorite(userId: String, productId: String): Result<String> =
        runCatching {
            privateUserService.requestRemoveFavoriteProduct(userId = userId, productId = productId)
        }

    suspend fun getFavoriteProductsByCursor(
        userId: String, cursor: String?, size: Int = 20
    ): CursorPage<AuctionSearchItem> {
        val dto = privateUserService.getUserFavoriteProductList(
            userId = userId,
            cursor = if (cursor.isNullOrBlank()) "unknown" else cursor,
            size = size,
        )
        val domainItems = dto.items.map { it.toDomain() }
        val nextKey = if (dto.nextCursor == "unknown") null else dto.nextCursor
        return CursorPage(
            items = domainItems,
            nextCursor = nextKey,
        )
    }

    suspend fun getFavoriteStatusMap(
        userId: String,
        productIds: List<String>
    ): Result<List<FavoriteStatus>> =
        runCatching {
            privateUserService
                .getFavoriteStatusByIds(userId, productIds)
                .toDomain()
        }

    suspend fun requestLogout()
            : Result<String> = runCatching { privateUserService.logout() }

    suspend fun deleteUser(userId: String): Result<String> =
        runCatching { privateUserService.deleteUser(userId) }

}
