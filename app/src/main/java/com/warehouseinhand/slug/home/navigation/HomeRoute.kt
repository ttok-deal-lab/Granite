package com.warehouseinhand.slug.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.warehouseinhand.slug.home.HomeScreen
import com.warehouseinhand.slug.home.HomeViewModel


@Composable
internal fun HomeRoute(
    padding: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreen(
        padding = padding
    )
}
