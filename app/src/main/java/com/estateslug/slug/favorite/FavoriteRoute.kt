package com.estateslug.slug.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.util.startDetailActivity


@Composable
internal fun FavoriteRoute(
    padding: PaddingValues,
    viewModel: FavoriteViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
) {

    val paginationState by viewModel.paginationState.collectAsStateWithLifecycle()

    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = {
        startDetailActivity(currentContext, it.id)
    }
    val onNotificationClick: () -> Unit = {}

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    FavoriteScreen(
        padding = padding,
        paginationState = paginationState,
        onItemClicked = onItemClicked,
        onLoadMore = { viewModel.loadMore() },
        onNotificationClick = onNotificationClick,
        onRetry = { viewModel.refresh() },
    )

}
