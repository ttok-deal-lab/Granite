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


@Composable
internal fun FavoriteRoute(
    padding: PaddingValues,
    onProductClick: (String) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
) {

    val paginationState by viewModel.paginationState.collectAsStateWithLifecycle()

    val onItemClicked: (ProductItemUiModel) -> Unit = {
        onProductClick(it.id)
    }
    val onNotificationClick: () -> Unit = {}

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // 관심 상태는 FavoriteStateStore가 즉시 동기화하므로 무조건 refresh하지 않는다.
                // 스냅샷 폴백이 필요했던 경우에만 1회 리프레시.
                viewModel.consumePendingRefresh()
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
