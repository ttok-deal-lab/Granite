package com.warehouseinhand.slug.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.compose.collectAsLazyPagingItems
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.util.startDetailActivity


@Composable
internal fun FavoriteRoute(
    padding: PaddingValues,
    viewModel: FavoriteViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
) {

    val favoriteList = viewModel.productUiModelList.collectAsLazyPagingItems()

    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = {
        startDetailActivity(currentContext, it.id)
    }
    val onNotificationClick: () -> Unit = {}

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                favoriteList.refresh() //
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }// TODO : 페이징 구현 직접 구현후 번쩍거림 없앨것!



    FavoriteScreen(
        padding = padding,
        productUiModelList = favoriteList,
        onItemClicked = onItemClicked,
        onNotificationClick = onNotificationClick
    )

}
