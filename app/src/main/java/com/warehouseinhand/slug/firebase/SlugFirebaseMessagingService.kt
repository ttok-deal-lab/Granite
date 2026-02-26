package com.warehouseinhand.slug.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build

import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.warehouseinhand.slug.main.MainActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SlugFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: send your new token to the server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload. : 데이터 메시지
        if (remoteMessage.data.isNotEmpty()) {
            val body = remoteMessage.data["body"]
            requestNotification(body = body)
        }

        // Check if message contains a notification payload. : 알림 메시지
        remoteMessage.notification?.let {
            val body = remoteMessage.notification!!.body
            requestNotification(body = body)
        }
    }

    private fun requestNotification(body: String?) {
        val id = System.currentTimeMillis().toInt()

        // notification 클릭 시 이동하는 액티비티
        val notiIntent = Intent(this, MainActivity::class.java)
        notiIntent.putExtra("ExtraFragment", "Notification")

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
//            .setSmallIcon(R.drawable.img_splash_logo)
//            .setContentTitle("SLUG") //TODO : localization need
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