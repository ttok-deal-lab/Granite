package com.warehouseinhand.slug.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.data.local.device.LocalDeviceSettingDataRepository
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
import com.warehouseinhand.slug.login.sns.SocialLoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localUserDataRepository: LocalUserDataRepository,
    private val localDeviceSettingDataRepository: LocalDeviceSettingDataRepository,
    private val remoteUserDataRepository: RemoteUserDataRepository,
) : ViewModel() {

    private val _lastLoginType = MutableStateFlow<SocialLoginType>(SocialLoginType.NEVER_LOGIN)
    val lastLoginType get() = _lastLoginType.asStateFlow()

    private val _isReadyToRemoveSplash = MutableStateFlow(false)
    val isReadyToRemoveSplash get() = _isReadyToRemoveSplash.asStateFlow()


    fun requestLastLoginType() {//TODO : Lazy한 방법 적용
        viewModelScope.launch {
            localDeviceSettingDataRepository.getLastLoginType().onSuccess {
                _lastLoginType.emit(it)
            }.onFailure {
                //TODO : log 적용 부분
            }

        }
    }

    fun requestUserAuth(
        snsAccessToken: String,
        type: SocialLoginType,
        doAfterSuccess: (() -> Unit)
    ) {
        networkWithProgress {
            withContext(Dispatchers.IO) {
                remoteUserDataRepository.postStartAuth(
                    snsAccessToken = snsAccessToken,
                    provider = type.typeName.lowercase()
                )
            }.onSuccess { (token, userProfile) ->
                localUserDataRepository.setUserSlugToken(token)
                localUserDataRepository.setUserProfile(userProfile)
                localDeviceSettingDataRepository.setLastLoginType(type)
                doAfterSuccess.invoke()
            }.onFailure {
                Log.e("TESTTESST", "requestUserAuth: FAIL" + it.localizedMessage)
            }
        }
    }

    fun checkTokenValidate(doOnSuccess: () -> Unit) {
        networkWithProgress {
            withContext(Dispatchers.IO) {
                val userId = localUserDataRepository.getUserId().getOrNull()
                if (userId == null) {
                    _isReadyToRemoveSplash.emit(true)
                    return@withContext
                    //TODO :실패 전달!!
                }
                val userProfile = remoteUserDataRepository.getUserInfo(userId).getOrNull()
                if (userProfile == null || userProfile.status == "INACTIVE") {
                    //TODO :실패 전달!!
                    _isReadyToRemoveSplash.emit(true)
                    return@withContext
                }
                localUserDataRepository.setUserProfile(userProfile)
                _isReadyToRemoveSplash.emit(true)
                doOnSuccess()
                //TODO :성공 전달!!
            }
        }
    }

    private val _isNeedToShowProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNeedToShowProgress get() = _isNeedToShowProgress.asStateFlow()

    private fun networkWithProgress(doWithAsyncBlock: suspend () -> Unit) {
        viewModelScope.launch {
            _isNeedToShowProgress.emit(true)

            doWithAsyncBlock.invoke()

            _isNeedToShowProgress.emit(false)
        }
    }


    fun <T> Flow<T>.stateInWhileSubscribed(initialValue: T): StateFlow<T> {
        return stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialValue,
        )
    }

}