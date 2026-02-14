package com.warehouseinhand.slug.data.network.update

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.warehouseinhand.slug.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class UpdateInfo(
    val latestVersion: String,
    val minVersion: String,
    val updateMsgTitle: String,
    val updateMsgDescription: String,
)

@Singleton
class RemoteConfigRepository @Inject constructor() {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    suspend fun fetchUpdateInfo(): Result<UpdateInfo> {
        return runCatching {
            remoteConfig.fetchAndActivate().await()
            UpdateInfo(
                latestVersion = remoteConfig.getString("latest_version"),
                minVersion = remoteConfig.getString("min_version"),
                updateMsgTitle = remoteConfig.getString("update_msg_title"),
                updateMsgDescription = remoteConfig.getString("update_msg_description"),
            )
        }
    }

    fun isForceUpdateRequired(updateInfo: UpdateInfo): Boolean {
        return compareVersions(BuildConfig.VERSION_NAME, updateInfo.minVersion) < 0
    }

    private fun compareVersions(current: String, min: String): Int {
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
        val minParts = min.split(".").map { it.toIntOrNull() ?: 0 }
        val maxLength = maxOf(currentParts.size, minParts.size)
        for (i in 0 until maxLength) {
            val c = currentParts.getOrElse(i) { 0 }
            val m = minParts.getOrElse(i) { 0 }
            if (c != m) return c.compareTo(m)
        }
        return 0
    }
}
