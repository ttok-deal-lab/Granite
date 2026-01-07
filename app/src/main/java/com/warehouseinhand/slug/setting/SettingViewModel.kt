package com.warehouseinhand.slug.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import com.warehouseinhand.slug.BuildConfig
import com.warehouseinhand.slug.firebase.SlugFirebaseMessagingService
import com.warehouseinhand.slug.login.sns.sns.SocialLoginModule
import com.warehouseinhand.slug.main.MainBottomSheetType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext val appContext: Context
) : ViewModel() {
    private val _isNeedToShowLogOutDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNeedToShowLogOutDialog get() = _isNeedToShowLogOutDialog.asStateFlow()

    val currentVersion = BuildConfig.VERSION_NAME

    fun changeLogoutDialogVisibility(needToShow: Boolean) {
        _isNeedToShowLogOutDialog.update { needToShow }
    }

    fun requestLogout(doAfterSuccess: () -> Unit) {//TODO : 해당작업들을 viewmodel이 하는게 맞나?
        CoroutineScope(Dispatchers.IO).launch {
            SlugFirebaseMessagingService().unregisterFcmToken()
            SocialLoginModule.requestLogOut(appContext)
            changeLogoutDialogVisibility(false)
            doAfterSuccess()
        }
    }

}