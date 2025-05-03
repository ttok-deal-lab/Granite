package com.warehouseinhand.slug.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
import com.warehouseinhand.slug.login.sns.SocialLoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localUserDataRepository: LocalUserDataRepository,
    private val remoteUserDataRepository: RemoteUserDataRepository,
) : ViewModel() {

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
                doAfterSuccess.invoke()
            }.onFailure {
                Log.e("TESTTESST", "requestUserAuth: FAIL"+it.localizedMessage )
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

}