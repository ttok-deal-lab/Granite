package com.warehouseinhand.slug.data.network.user

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.warehouseinhand.slug.data.network.search.DEFAULT_PAGING_SIZE
import com.warehouseinhand.slug.data.network.search.ProductsByCursorPagingSource
import com.warehouseinhand.slug.domain.sales.FavoriteSaleSummary
import com.warehouseinhand.slug.domain.user.SlugToken
import com.warehouseinhand.slug.domain.user.UserProfile
import kotlinx.coroutines.flow.Flow
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

    fun getUserFavoriteProductList(userId: String, size: Int = DEFAULT_PAGING_SIZE): Flow<PagingData<FavoriteSaleSummary>> =
        Pager(
            config = PagingConfig(
                pageSize = size,
                initialLoadSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FavoriteProductsByCursorPagingSource(
                    service = privateUserService,
                    userId = userId,
                )
            }
        ).flow

    suspend fun requestLogout()
            : Result<String> = runCatching { privateUserService.logout() }

}