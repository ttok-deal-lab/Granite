package com.estateslug.slug.data.local.device

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.estateslug.slug.data.local.DataStoreModule
import com.estateslug.slug.data.local.getStoredData
import com.estateslug.slug.data.local.removeAllData
import com.estateslug.slug.data.local.storeData
import com.estateslug.slug.login.sns.SocialLoginType
import com.estateslug.slug.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
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

    /** 로그아웃/탈퇴 시 호출 — 재로그인 진입 시(권한 미보유면) 인트로를 다시 노출하기 위함 */
    suspend fun resetNotificationPermissionIntroShown(): Result<Unit> =
        deviceDataStore.storeData(key = NOTIFICATION_PERMISSION_INTRO_SHOWN, value = false)

    suspend fun wasNotificationPermissionIntroShown(): Result<Boolean> =
        runCatching {
            deviceDataStore.getStoredData(key = NOTIFICATION_PERMISSION_INTRO_SHOWN).getOrThrow()
        }.recoverCatching {
            // 에러 발생 시 기본값
            false
        }

    /** 화면 테마. 저장값이 없거나 읽기에 실패하면 [ThemeMode.DEFAULT](시스템 따름). 값이 바뀌면 다시 방출한다. */
    val themeMode: Flow<ThemeMode> = deviceDataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { preferences -> ThemeMode.fromStoredName(preferences[THEME_MODE]) }

    suspend fun setThemeMode(mode: ThemeMode): Result<Unit> =
        deviceDataStore.storeData(key = THEME_MODE, value = mode.storedName)


    companion object {
        private val LOGIN_LAST_TYPE = stringPreferencesKey("login_last_type")
        private val NOTIFICATION_PERMISSION_INTRO_SHOWN =
            booleanPreferencesKey("notification_permission_intro_shown")
        private val THEME_MODE = stringPreferencesKey("theme_mode")

    }
}