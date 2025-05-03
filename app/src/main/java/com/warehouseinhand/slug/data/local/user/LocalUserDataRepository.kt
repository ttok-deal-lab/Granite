package com.warehouseinhand.slug.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.warehouseinhand.slug.data.local.DataStoreModule
import com.warehouseinhand.slug.data.local.getStoredData
import com.warehouseinhand.slug.data.local.removeAllData
import com.warehouseinhand.slug.data.local.setDataStore
import com.warehouseinhand.slug.data.local.storeData
import com.warehouseinhand.slug.domain.SlugToken
import com.warehouseinhand.slug.domain.UserProfile
import javax.inject.Inject

//TODO : 현재 REFRESH 정보 없음 정책 확인 후 수정 할 것!
//TODO : 레이어 모듈 분리시 interface 로
class LocalUserDataRepository @Inject constructor(
    @DataStoreModule.DataStoreUser private val userDataStore: DataStore<Preferences>
) {
    suspend fun removeAllData(): Result<Unit> =
        kotlin.runCatching { userDataStore.removeAllData() }

    suspend fun setUserAccessToken(
        accessToken: String
    ): Result<Unit> =
        kotlin.runCatching {
            userDataStore.setDataStore(key = ACCESS_TOKEN, value = accessToken)
        }

    suspend fun setUserSlugToken(
        token: SlugToken
    ): Result<Unit> =
        kotlin.runCatching {
            userDataStore.setDataStore(key = ACCESS_TOKEN, value = token.accessToken)
//            userDataStore.setDataStore(key = REFRESH_TOKEN, value = token.refreshToken)
        }

    suspend fun getUserSlugToken(): Result<SlugToken> =
        kotlin.runCatching {
            SlugToken(
                accessToken = userDataStore.getStoredData(key = ACCESS_TOKEN).getOrThrow(),
//                refreshToken = userDataStore.getStoredData(key = REFRESH_TOKEN).getOrThrow()
            )
        }

    suspend fun getUserAccessToken(): Result<String> =
        userDataStore.getStoredData(key = ACCESS_TOKEN)


//    suspend fun setUserRefreshToken(
//        refreshToken: String
//    ): Result<Unit> =
//        kotlin.runCatching {
//            userDataStore.setDataStore(key = REFRESH_TOKEN, value = refreshToken)
//        }

//    suspend fun getUserRefreshToken(): Result<String> =
//        userDataStore.getStoredData(key = REFRESH_TOKEN)

    //USER DATA
    //TODO: 에러 처리 관련 확장 필요.
    suspend fun setUserProfile(userProfile: UserProfile): Result<Unit> =
        kotlin.runCatching {
            setUserId(userProfile.id).getOrThrow()
            setUserEmail(userProfile.email).getOrThrow()
            setUserName(userProfile.name).getOrThrow()
            setUserProvider(userProfile.provider).getOrThrow()
            setUserStatus(userProfile.status).getOrThrow()
        }

    suspend fun getUserProfile(): Result<UserProfile> =
        kotlin.runCatching {
            val userId = getUserId().getOrThrow()
            val userEmail = getUserEmail().getOrThrow()
            val userName = getUserName().getOrThrow()
            val userProvider = getUserProvider().getOrThrow()
            val userStatus = getUserStatus().getOrThrow()

            UserProfile(
                id = userId,
                email = userEmail,
                name = userName,
                provider = userProvider,
                status = userStatus
            )
        }

    suspend fun setUserId(userId: Long): Result<Unit> =
        userDataStore.storeData(key = USER_ID, value = userId)

    suspend fun getUserId(): Result<Long> =
        userDataStore.getStoredData(key = USER_ID)

    suspend fun setUserEmail(userEmail: String): Result<Unit> =
        userDataStore.storeData(key = USER_EMAIL, value = userEmail)

    suspend fun getUserEmail(): Result<String> =
        userDataStore.getStoredData(key = USER_EMAIL)

    suspend fun setUserName(userName: String): Result<Unit> =
        userDataStore.storeData(key = USER_NAME, value = userName)

    suspend fun getUserName(): Result<String> =
        userDataStore.getStoredData(key = USER_NAME)

    suspend fun setUserProvider(userProvider: String): Result<Unit> =
        userDataStore.storeData(key = USER_PROVIDER, value = userProvider)

    suspend fun getUserProvider(): Result<String> =
        userDataStore.getStoredData(key = USER_PROVIDER)

    suspend fun setUserStatus(userStatus: String): Result<Unit> =
        userDataStore.storeData(key = USER_STATUS, value = userStatus)

    suspend fun getUserStatus(): Result<String> =
        userDataStore.getStoredData(key = USER_STATUS)


    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        private val USER_ID = longPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_PROVIDER = stringPreferencesKey("user_provider")
        private val USER_STATUS = stringPreferencesKey("user_status")

    }
}