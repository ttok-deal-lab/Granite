package com.estateslug.slug.data.local.device

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.estateslug.slug.data.local.DataStoreModule
import com.estateslug.slug.data.local.getStoredData
import com.estateslug.slug.data.local.removeAllData
import com.estateslug.slug.data.local.storeData
import com.estateslug.slug.login.sns.SocialLoginType
import javax.inject.Inject

//TODO : 레이어 모듈 분리시 interface 로
class LocalDeviceSettingDataRepository @Inject constructor(
    @DataStoreModule.DataStoreSettings private val deviceDataStore: DataStore<Preferences>
) {
    suspend fun removeAllData(): Result<Unit> =
        runCatching { deviceDataStore.removeAllData() }


    suspend fun setLastLoginType(userStatus: SocialLoginType): Result<Unit> =
        deviceDataStore.storeData(key = LOGIN_LAST_TYPE, value = userStatus.typeName)

    suspend fun getLastLoginType(): Result<SocialLoginType> =
        runCatching {
            val typeName = deviceDataStore.getStoredData(key = LOGIN_LAST_TYPE).getOrThrow()
            SocialLoginType.fromTypeName(typeName)
        }.recoverCatching {
            // 에러 발생 시 기본값
            SocialLoginType.NEVER_LOGIN
        }

    suspend fun setNotificationPermissionIntroShown(): Result<Unit> =
        deviceDataStore.storeData(key = NOTIFICATION_PERMISSION_INTRO_SHOWN, value = true)

    suspend fun wasNotificationPermissionIntroShown(): Result<Boolean> =
        runCatching {
            deviceDataStore.getStoredData(key = NOTIFICATION_PERMISSION_INTRO_SHOWN).getOrThrow()
        }.recoverCatching {
            // 에러 발생 시 기본값
            false
        }


    companion object {
        private val LOGIN_LAST_TYPE = stringPreferencesKey("login_last_type")
        private val NOTIFICATION_PERMISSION_INTRO_SHOWN =
            booleanPreferencesKey("notification_permission_intro_shown")

    }
}