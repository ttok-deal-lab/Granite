package com.warehouseinhand.slug.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.warehouseinhand.slug.home.ProductItemUiModel


@Composable
internal fun FavoriteRoute(
    padding: PaddingValues,
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    val favoriteList by viewModel.favoriteUiModelList.collectAsStateWithLifecycle()
    val onItemClicked: (ProductItemUiModel) -> Unit = {}
    val onNotificationClick: () -> Unit = {}
    //TODO : 아래 영역에서 favorite 버튼 눌러도 사라지지 않게 만들고, 상세화면에서만 해제 가능하게 할것!
    SideEffect {
        viewModel.requestFavoriteList()
    }
    FavoriteScreen(
        padding = padding,
        productUiModelList = favoriteList,
        onItemClicked = onItemClicked,
        onNotificationClick = onNotificationClick
    )

}
