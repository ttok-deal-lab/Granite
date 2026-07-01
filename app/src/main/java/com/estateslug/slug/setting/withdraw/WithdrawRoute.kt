package com.estateslug.slug.setting.withdraw

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.estateslug.slug.setting.SettingViewModel

@Composable
internal fun WithdrawRoute(onBackClick: () -> Unit) {
    val settingViewModel: SettingViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
    val onWithDrawClick: () -> Unit = {
        settingViewModel.changeWithdrawDialogVisibility(true)
    }
    WithdrawPage(
        onBackClick = onBackClick,
        onWithDrawClick = onWithDrawClick,
    )
}
