package com.warehouseinhand.slug.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.warehouseinhand.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
) {
    composable<RouteHome> {
        HomeRoute(
            padding = padding
        )
    }
}

@Serializable
data object RouteHome: Route

