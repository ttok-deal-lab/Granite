package com.warehouseinhand.slug.data.network.user

import com.warehouseinhand.slug.domain.SlugToken
import com.warehouseinhand.slug.domain.UserProfile
import javax.inject.Inject

class RemoteUserDataRepository @Inject constructor(
    private val publicUserService: UserPublicService,
//    private val privateUserService: UserPrivateService,
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

}