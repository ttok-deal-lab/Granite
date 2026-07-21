package com.estateslug.slug.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.main.Route
import com.estateslug.slug.setting.permission.permissionSettingNavGraph
import com.estateslug.slug.setting.terms.termsNavGraph
import com.estateslug.slug.setting.withdraw.withdrawNavGraph
import com.estateslug.slug.ui.component.ProgressCover
import com.estateslug.slug.util.moveToLoginWithBackStackClear

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    settingViewModel: SettingViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val dialogVisibility by settingViewModel.isNeedToShowLogOutDialog.collectAsStateWithLifecycle()
    val withdrawDialogVisibility by settingViewModel.isNeedToShowWithdrawDialog.collectAsStateWithLifecycle()

    val isNeedToShowProgress by settingViewModel.isNeedToShowProgress.collectAsStateWithLifecycle()

    val onLogoutConfirmClicked: () -> Unit = {
        settingViewModel.requestLogout(doAfterSuccess = { moveToLoginWithBackStackClear(context) })
    }
    val onWithdrawConfirmClicked: () -> Unit = {
        settingViewModel.requestWithdraw(doAfterSuccess = { moveToLoginWithBackStackClear(context) })
    }
    val onWithdrawDismissClicked: () -> Unit = {
        settingViewModel.changeWithdrawDialogVisibility(false)
    }
    val navController = rememberNavController()
    val onLogoutDismissClicked: () -> Unit = {
        settingViewModel.changeLogoutDialogVisibility(false)
    }
    val navTo: (Route) -> Unit = {
        navController.navigate(it)
    }

    val onBackClick: () -> Unit = {
        if (!navController.popBackStack())
            onBackClick()
    }


    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            modifier = modifier.navigationBarsPadding(),
            content = { paddingValues ->
                //MainNavHost
                SettingNavHost(
                    padding = paddingValues,
                    navController = navController,
                    startDestination = RouteSettingMain,
                    onBackClick = onBackClick,
                    onNavigate = navTo
                )

                LogoutDialog(
                    dialogVisibility = dialogVisibility,
                    onLogOutConfirmClicked = onLogoutConfirmClicked,
                    onDismissRequest = onLogoutDismissClicked
                )

                WithdrawDialog(
                    dialogVisibility = withdrawDialogVisibility,
                    onWithdrawConfirmClicked = onWithdrawConfirmClicked,
                    onDismissRequest = onWithdrawDismissClicked
                )
            }
        )
        ProgressCover(isVisible = isNeedToShowProgress)
    }

}


@Composable
fun SettingNavHost(
//    navigator: MainNavigator,
    padding: PaddingValues,
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNavigate: (Route) -> Unit,
) {

    Box(
        modifier = modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            settingMainNavGraph(onBackClick, onNavigate)

            termsNavGraph(onBackClick)

            withdrawNavGraph(onBackClick)

            permissionSettingNavGraph(onBackClick)
        }
    }
}
