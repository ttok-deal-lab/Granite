package com.estateslug.slug.domain.user

import android.content.Context
import android.provider.Settings
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.user.RemoteUserDataRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RegisterFcmTokenUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localUserDataRepository: LocalUserDataRepository,
    private val remoteUserDataRepository: RemoteUserDataRepository
) {

    suspend operator fun invoke(): Result<Long> = runCatching {
        val userId = localUserDataRepository.getUserId().getOrThrow()
        val fcmToken = getCurrentFcmToken()
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        remoteUserDataRepository.registerFcmToken(
            userId = userId,
            deviceId = deviceId,
            fcmToken = fcmToken,
        ).getOrThrow()
    }

    private suspend fun getCurrentFcmToken(): String =
        suspendCancellableCoroutine { continuation ->
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    continuation.resume(token)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
}
