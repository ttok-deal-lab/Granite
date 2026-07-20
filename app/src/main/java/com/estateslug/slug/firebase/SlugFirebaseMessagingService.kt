package com.estateslug.slug.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build

import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.estateslug.slug.R
import com.estateslug.slug.deeplink.DeepLinkKeys
import com.estateslug.slug.deeplink.DeepLinkRouterActivity
import com.estateslug.slug.domain.user.RegisterFcmTokenUseCase
import com.estateslug.slug.main.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SlugFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // 로그아웃 시 deleteToken()이 새 토큰 발급을 유발해 onNewToken이 불릴 수 있다.
        // 이때는 auto-init이 꺼진 상태(unregisterFcmToken 선행)이므로 서버 재등록을 건너뛴다.
        if (!FirebaseMessaging.getInstance().isAutoInitEnabled) return

        // 새 토큰을 서버에 등록 (미로그인 상태면 UseCase 내부에서 Result.failure 처리됨)
        val registerFcmTokenUseCase = EntryPointAccessors.fromApplication(
            applicationContext,
            FcmTokenEntryPoint::class.java
        ).registerFcmTokenUseCase()

        CoroutineScope(Dispatchers.IO).launch {
            registerFcmTokenUseCase()
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface FcmTokenEntryPoint {
        fun registerFcmTokenUseCase(): RegisterFcmTokenUseCase
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 화면 이동용 딥링크(https://link.estateslug.com/...) — data payload의 "link"
        val link = remoteMessage.data[DeepLinkKeys.FCM_LINK]

        // Check if message contains a data payload. : 데이터 메시지
        if (remoteMessage.data.isNotEmpty()) {
            val body = remoteMessage.data["body"]
            requestNotification(body = body, link = link)
        }

        // Check if message contains a notification payload. : 알림 메시지
        remoteMessage.notification?.let {
            val body = remoteMessage.notification!!.body
            requestNotification(body = body, link = link)
        }
    }

    private fun requestNotification(body: String?, link: String? = null) {
        val id = System.currentTimeMillis().toInt()

        // notification 클릭 시 이동: 딥링크가 있으면 라우터로, 없으면 메인으로
        val notiIntent = if (!link.isNullOrBlank()) {
            Intent(this, DeepLinkRouterActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(link)
            }
        } else {
            Intent(this, MainActivity::class.java)
                .putExtra("ExtraFragment", "Notification")
        }

        notiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            id,
            notiIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // notification channel 생성
        val CHANNEL_ID = CHANNEL_ID
        val CHANNEL_NAME = CHANNEL_ID
//        val CHANNEL_ID = getString(R.string.notification_channel_id)
//        val CHANNEL_NAME = getString(R.string.notification_channel_name)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // notification 생성
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            // small icon 없이 notify() 호출 시 IllegalArgumentException 크래시 (QA 시트 참고)
            .setSmallIcon(R.drawable.notification_line_28_28)
            .setContentTitle(applicationInfo.loadLabel(packageManager).toString())
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
        notificationManager.notify(id, notificationBuilder.build())
    }

    suspend fun getCurrentToken() = suspendCoroutine<String> { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
//                    Lg.d("token: $token")
                    continuation.resume(token)
                } else {
//                    Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                    continuation.resume("")
                    return@OnCompleteListener
                }
            }
        )
    }

    fun registerFcmToken() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }

    fun unregisterFcmToken() {
        with(FirebaseMessaging.getInstance()){
            isAutoInitEnabled = false
            deleteToken()
        }
    }

    companion object {
        const val TAG= "SLUGFirebaseMessagingService"
        const val CHANNEL_ID = "SLUG_GRINITE"
    }
}