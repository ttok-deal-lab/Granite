package com.estateslug.slug.domain.user

import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.user.RemoteUserDataRepository
import javax.inject.Inject

class UnregisterFcmTokenUseCase @Inject constructor(
    private val localUserDataRepository: LocalUserDataRepository,
    private val remoteUserDataRepository: RemoteUserDataRepository
) {

    suspend operator fun invoke(): Result<String> = runCatching {
        val userId = localUserDataRepository.getUserId().getOrThrow()
        remoteUserDataRepository.deleteFcmToken(userId).getOrThrow()
    }
}
