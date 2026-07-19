package com.estateslug.slug.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import com.estateslug.slug.BuildConfig
import com.estateslug.slug.data.local.user.LocalUserDataRepository
import com.estateslug.slug.data.network.user.RemoteUserDataRepository
import com.estateslug.slug.domain.user.UnregisterFcmTokenUseCase
import com.estateslug.slug.firebase.SlugFirebaseMessagingService
import com.estateslug.slug.login.sns.sns.SocialLoginModule
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val localUserDataRepository: LocalUserDataRepository,
    private val remoteUserDataRepository: RemoteUserDataRepository,
    private val unregisterFcmTokenUseCase: UnregisterFcmTokenUseCase,
) : ViewModel() {
    private val _isNeedToShowLogOutDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNeedToShowLogOutDialog get() = _isNeedToShowLogOutDialog.asStateFlow()

    private val _isNeedToShowWithdrawDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNeedToShowWithdrawDialog get() = _isNeedToShowWithdrawDialog.asStateFlow()

    private val _isNeedToShowProgress = MutableStateFlow<Boolean>(false)
    val isNeedToShowProgress = _isNeedToShowProgress.asStateFlow()


    val currentVersion = BuildConfig.VERSION_NAME

    fun changeLogoutDialogVisibility(needToShow: Boolean) {
        _isNeedToShowLogOutDialog.update { needToShow }
    }

    fun changeWithdrawDialogVisibility(needToShow: Boolean) {
        _isNeedToShowWithdrawDialog.update { needToShow }
    }

    fun requestLogout(doAfterSuccess: () -> Unit) {//TODO : 해당작업들을 viewmodel이 하는게 맞나?
        CoroutineScope(Dispatchers.IO).launch {
            changeLogoutDialogVisibility(false)
            _isNeedToShowProgress.emit(true)
            runCatching {
                unregisterFcmTokenUseCase() // 액세스 토큰이 유효할 때 서버에서 FCM 토큰을 먼저 제거
                SlugFirebaseMessagingService().unregisterFcmToken()
                SocialLoginModule.requestLogOut(appContext)
                remoteUserDataRepository.requestLogout()
            }
            localUserDataRepository.setUserAccessToken("") // 유저 토큰을 없애버림.
            _isNeedToShowProgress.update { false }
            // 토큰 제거까지 끝난 뒤 화면 전환해야 LogInActivity 자동로그인이
            // 아직 살아있는 토큰으로 성공해 메인으로 복귀하는 레이스가 없다
            withContext(Dispatchers.Main) { doAfterSuccess() }
        }
    }

    fun requestWithdraw(doAfterSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            changeWithdrawDialogVisibility(false)
            _isNeedToShowProgress.emit(true)
            runCatching {
                val userId = localUserDataRepository.getUserId().getOrThrow()
                unregisterFcmTokenUseCase() // 액세스 토큰이 유효할 때 서버에서 FCM 토큰을 먼저 제거
                SlugFirebaseMessagingService().unregisterFcmToken()
                SocialLoginModule.requestLogOut(appContext)
                remoteUserDataRepository.deleteUser(userId)
            }
            localUserDataRepository.removeAllData()
            _isNeedToShowProgress.update { false }
            // 로컬 데이터 제거까지 끝난 뒤 화면 전환 (requestLogout과 동일한 레이스 방지)
            withContext(Dispatchers.Main) { doAfterSuccess() }
        }
    }

}