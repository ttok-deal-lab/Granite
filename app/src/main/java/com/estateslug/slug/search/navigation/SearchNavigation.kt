package com.estateslug.slug.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.estateslug.slug.search.SearchViewModel
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetType
import com.estateslug.slug.search.bridge.SearchBridgeRoute
import com.estateslug.slug.search.result.SearchResultRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteSearchBridge

@Serializable
data class RouteSearchResult(val keyword: String)

fun NavGraphBuilder.searchNavGraph(
    searchViewModel: SearchViewModel,
    navController: NavHostController,
    onItemClick: (String) -> Unit,
    onShowBottomSheet: (SearchBottomSheetType) -> Unit,
) {
    composable<RouteSearchBridge> {
        SearchBridgeRoute(
            viewModel = searchViewModel,
            onSearch = { keyword ->
                searchViewModel.searchWithCheck(keyword) {
                    navController.navigate(RouteSearchResult(keyword)) {
                        popUpTo<RouteSearchBridge> { inclusive = false }
                    }
                }
            }
        )
    }

    composable<RouteSearchResult> { backStackEntry ->
        SearchResultRoute(
            viewModel = searchViewModel,
            onItemClick = onItemClick,
            onShowBottomSheet = onShowBottomSheet,
        )
    }
}
