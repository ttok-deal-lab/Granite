package com.estateslug.slug.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.estateslug.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.favoriteNavGraph(
    padding: PaddingValues,
    onProductClick: (String) -> Unit,
) {
    composable<RouteFavorite> {
        FavoriteRoute(
            padding = padding,
            onProductClick = onProductClick,
        )
    }
}

@Serializable
data object RouteFavorite: Route

