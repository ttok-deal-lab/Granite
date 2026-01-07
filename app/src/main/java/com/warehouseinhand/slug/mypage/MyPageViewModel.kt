package com.warehouseinhand.slug.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.data.local.device.LocalDeviceSettingDataRepository
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.login.sns.SocialLoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val localUserDataRepository: LocalUserDataRepository,
    private val localDeviceSettingDataRepository: LocalDeviceSettingDataRepository,
) : ViewModel() {

    val lastLoginType: StateFlow<SocialLoginType> = flow {
        localDeviceSettingDataRepository.getLastLoginType()
            .onSuccess { emit(it) }
            .onFailure {
                emit(SocialLoginType.NEVER_LOGIN)
                /*TODO : log 적용 부분*/
            }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SocialLoginType.NEVER_LOGIN
    )

    val userName: StateFlow<String> = flow {
        localUserDataRepository.getUserName()
            .onSuccess { emit(it) }
            .onFailure {
                emit("FAIL_TO_GET")
                /*TODO : log 적용 부분*/
            }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "FAIL_TO_GET"
    )

}