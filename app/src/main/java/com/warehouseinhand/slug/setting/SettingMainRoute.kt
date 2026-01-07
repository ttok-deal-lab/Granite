package com.warehouseinhand.slug.setting

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.warehouseinhand.slug.main.Route
import com.warehouseinhand.slug.setting.terms.RouteTerms
import com.warehouseinhand.slug.setting.withdraw.RouteWithdraw
import com.warehouseinhand.slug.util.moveToStore

@Composable
internal fun SettingMainRoute(
    viewModel: SettingViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner),
    onBackClick: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    val context = LocalContext.current
    val appVersion: String = viewModel.currentVersion

    val isRecentVersion: Boolean = true //TODO remote Config보고 할것!

    val openSourceListActivityTitle = "오픈소스 라이선스"

    val onTOSClick: () -> Unit = {
        onNavigate(RouteTerms)
    }
    val onWithDrawClick: () -> Unit = {
        onNavigate(RouteWithdraw)
    }

    val onMoveToStoreClick: () -> Unit = { moveToStore(context) }

    val onOpenSourceClick: () -> Unit = {
        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        OssLicensesMenuActivity.setActivityTitle(openSourceListActivityTitle)
    }

    val onLogoutClick: () -> Unit = { viewModel.changeLogoutDialogVisibility(true) }

    SettingMainPage(
        onBackClick = onBackClick,
        appVersion = appVersion,
        isRecentVersion = isRecentVersion,
        onTOSClick = onTOSClick,
        onMoveToStoreClick = onMoveToStoreClick,
        onOpenSourceClick = onOpenSourceClick,
        onLogoutClick = onLogoutClick,
        onWithDrawClick = onWithDrawClick
    )

}
