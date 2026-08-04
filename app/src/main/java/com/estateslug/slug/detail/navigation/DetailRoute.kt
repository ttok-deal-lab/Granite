package com.estateslug.slug.detail.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.detail.DetailScreen
import com.estateslug.slug.detail.DetailedViewModel

@Composable
internal fun DetailRoute(
    onBack: () -> Unit,
    // NavBackStackEntry 스코프여야 한다 — Activity 스코프(LocalContext as ViewModelStoreOwner)를
    // 쓰면 상세 A→B 진입 시 A의 데이터가 재사용된다
    viewModel: DetailedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreen(
        uiState = uiState,
        onBackButtonClicked = onBack,
        likeClicked = { viewModel.onLikeChangeRequest() },
        onRetry = { viewModel.retry() },
    )
}
