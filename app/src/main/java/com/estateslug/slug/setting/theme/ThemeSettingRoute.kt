package com.estateslug.slug.setting.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.setting.SettingViewModel

@Composable
internal fun ThemeSettingRoute(
    onBackClick: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner),
) {
    val selected by viewModel.themeMode.collectAsStateWithLifecycle()
    ThemeSettingPage(
        selected = selected,
        onSelect = viewModel::setThemeMode,
        onBackClick = onBackClick,
    )
}
