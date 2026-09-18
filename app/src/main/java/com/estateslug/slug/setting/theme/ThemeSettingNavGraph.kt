package com.estateslug.slug.setting.theme

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.estateslug.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.themeSettingNavGraph(onBackClick: () -> Unit) {
    composable<RouteThemeSetting> { ThemeSettingRoute(onBackClick) }
}

@Serializable
data object RouteThemeSetting : Route
