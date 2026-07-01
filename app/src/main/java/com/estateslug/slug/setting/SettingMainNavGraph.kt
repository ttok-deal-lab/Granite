package com.estateslug.slug.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.estateslug.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.settingMainNavGraph(onBackClick: () -> Unit, onNavigate: (Route) -> Unit) {
    composable<RouteSettingMain> {
        SettingMainRoute(onBackClick = onBackClick, onNavigate = onNavigate)
    }
}

@Serializable
data object RouteSettingMain : Route

