package com.warehouseinhand.slug.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.util.startDetailActivity


@Composable
internal fun FavoriteRoute(
    padding: PaddingValues,
    viewModel: FavoriteViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
) {

    val favoriteList by viewModel.favoriteUiModelList.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = {
        startDetailActivity(currentContext)
    }
    val onNotificationClick: () -> Unit = {}
    //TODO : 아래 영역에서 favorite 버튼 눌러도 사라지지 않게 만들고, 상세화면에서만 해제 가능하게 할것!
    LaunchedEffect(Unit) {
        viewModel.requestFavoriteList()
    }
    FavoriteScreen(
        padding = padding,
        productUiModelList = favoriteList,
        onItemClicked = onItemClicked,
        onNotificationClick = onNotificationClick
    )

}
