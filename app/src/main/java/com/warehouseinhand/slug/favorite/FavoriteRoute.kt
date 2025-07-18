package com.warehouseinhand.slug.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.warehouseinhand.slug.home.HomeScreen
import com.warehouseinhand.slug.home.HomeViewModel


@Composable
internal fun FavoriteRoute(
    padding: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Box(Modifier.fillMaxSize()){
        Text("FAVORITE")
    }
}
