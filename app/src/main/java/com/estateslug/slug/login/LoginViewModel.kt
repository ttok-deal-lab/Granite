package com.estateslug.slug.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estateslug.slug.data.local.device.LocalDeviceSettingDataRepository
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.update.RemoteConfigRepository
import com.estateslug.slug.data.network.update.UpdateInfo
import com.estateslug.slug.data.network.user.RemoteUserDataRepository
import com.estateslug.slug.domain.user.RegisterFcmTokenUseCase
import com.estateslug.slug.login.sns.SocialLoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
    private val remoteConfigRepository: RemoteConfigRepository,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
) : ViewModel() {

    private val _lastLoginType = MutableStateFlow<SocialLoginType>(SocialLoginType.NEVER_LOGIN)
    val lastLoginType get() = _lastLoginType.asStateFlow()

    private val _isReadyToRemoveSplash = MutableStateFlow(false)
    val isReadyToRemoveSplash get() = _isReadyToRemoveSplash.asStateFlow()

    private val _forceUpdateInfo = MutableStateFlow<UpdateInfo?>(null)
    val forceUpdateInfo: StateFlow<UpdateInfo?> get() = _forceUpdateInfo.asStateFlow()


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
                // 로그인 완료(화면 전환)를 막지 않도록 비동기로 서버에 FCM 토큰 등록
                CoroutineScope(Dispatchers.IO).launch { registerFcmTokenUseCase() }
                localDeviceSettingDataRepository.setLastLoginType(type)
                doAfterSuccess.invoke()
            }.onFailure {
                Log.e("TESTTESST", "requestUserAuth: FAIL" + it.localizedMessage)
            }
        }
    }

    fun checkForceUpdate(onUpdateNotRequired: () -> Unit) {
        viewModelScope.launch {
            remoteConfigRepository.fetchUpdateInfo()
                .onSuccess { updateInfo ->
                    if (remoteConfigRepository.isForceUpdateRequired(updateInfo)) {
                        _forceUpdateInfo.emit(updateInfo)
                        _isReadyToRemoveSplash.emit(true)
                    } else {
                        onUpdateNotRequired()
                    }
                }
                .onFailure {
                    onUpdateNotRequired()
                }
        }
    }

    fun checkTokenValidate(doOnSuccess: () -> Unit) {
        networkWithProgress {
            withContext(Dispatchers.IO) {
               val currentAccessToken = localUserDataRepository.getUserAccessToken().getOrNull()
                if (currentAccessToken.isNullOrBlank()){
                    _isReadyToRemoveSplash.emit(true)
                    return@withContext
                    //TODO :실패 전달!!
                }

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
                // FCM 토큰이 회전됐을 수 있으므로 자동로그인 시에도 재등록 (스플래시 해제를 막지 않도록 비동기)
                CoroutineScope(Dispatchers.IO).launch { registerFcmTokenUseCase() }
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