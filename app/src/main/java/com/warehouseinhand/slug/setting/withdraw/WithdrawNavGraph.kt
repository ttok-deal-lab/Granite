package com.warehouseinhand.slug.setting.withdraw

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.warehouseinhand.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.withdrawNavGraph(onBackClick: () -> Unit) {
    composable<RouteWithdraw> {
        WithdrawRoute(onBackClick)
    }
}

@Serializable
data object RouteWithdraw : Route
