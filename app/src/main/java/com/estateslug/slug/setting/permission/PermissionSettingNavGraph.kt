package com.estateslug.slug.setting.permission

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.estateslug.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.permissionSettingNavGraph(onBackClick: () -> Unit) {
    composable<RoutePermissionSetting> { PermissionSettingRoute(onBackClick) }
}

@Serializable
data object RoutePermissionSetting : Route
