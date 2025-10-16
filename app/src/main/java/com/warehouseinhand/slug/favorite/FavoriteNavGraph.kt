package com.warehouseinhand.slug.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.warehouseinhand.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.favoriteNavGraph(
    padding: PaddingValues,
) {
    composable<RouteFavorite> {
        FavoriteRoute(
            padding = padding
        )
    }
}

@Serializable
data object RouteFavorite: Route

