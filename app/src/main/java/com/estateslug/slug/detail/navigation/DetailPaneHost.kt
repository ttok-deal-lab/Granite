package com.estateslug.slug.detail.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.detail.DetailScreen
import com.estateslug.slug.detail.DetailedViewModel

/**
 * 2-pane(폴더블 펼침)에서 상세 pane에 꽂는 호스트.
 * 백스택 엔트리가 없으므로 route 인자 대신 requestData(id)로 진입한다 — DetailRoute와 대칭.
 * VM은 고정 key의 단일 인스턴스를 재사용하고 매물 전환은 requestData로 처리한다
 * (매물마다 key를 바꾸면 Activity 스코프에 VM이 누적됨).
 */
@Composable
internal fun DetailPaneHost(
    productId: String,
    onClose: () -> Unit,
    viewModel: DetailedViewModel = hiltViewModel(key = "detail-pane"),
) {
    // back 처리는 호스트(ProductListDetailPaneScaffold)가 담당 — scaffold 내장 핸들러와 상세가 열렸을 때
    // 추가하는 핸들러(predictive back). 여기서 따로 등록하지 않는다
    LaunchedEffect(productId) {
        viewModel.requestData(productId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreen(
        uiState = uiState,
        onBackButtonClicked = onClose,
        likeClicked = { viewModel.onLikeChangeRequest() },
        onRetry = { viewModel.retry() },
    )
}
