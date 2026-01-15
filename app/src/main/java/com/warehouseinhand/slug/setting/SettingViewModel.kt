package com.warehouseinhand.slug.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import com.warehouseinhand.slug.BuildConfig
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
import com.warehouseinhand.slug.firebase.SlugFirebaseMessagingService
import com.warehouseinhand.slug.login.sns.sns.SocialLoginModule
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val localUserDataRepository: LocalUserDataRepository,
    private val remoteUserDataRepository: RemoteUserDataRepository,
) : ViewModel() {
    private val _isNeedToShowLogOutDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNeedToShowLogOutDialog get() = _isNeedToShowLogOutDialog.asStateFlow()

    private val _isNeedToShowProgress = MutableStateFlow<Boolean>(false)
    val isNeedToShowProgress = _isNeedToShowProgress.asStateFlow()


    val currentVersion = BuildConfig.VERSION_NAME

    fun changeLogoutDialogVisibility(needToShow: Boolean) {
        _isNeedToShowLogOutDialog.update { needToShow }
    }

    fun requestLogout(doAfterSuccess: () -> Unit) {//TODO : 해당작업들을 viewmodel이 하는게 맞나?
        runCatching {
            CoroutineScope(Dispatchers.IO).launch {
                changeLogoutDialogVisibility(false)
                _isNeedToShowProgress.emit(true)
                SlugFirebaseMessagingService().unregisterFcmToken()
                SocialLoginModule.requestLogOut(appContext)
                remoteUserDataRepository.requestLogout()
                localUserDataRepository.setUserAccessToken("") // 유저 토큰을 없애버림.
                _isNeedToShowProgress.update { false }
            }
        }.onSuccess {
            doAfterSuccess()
        }.onFailure {
            _isNeedToShowProgress.update { false }
        }

    }

}