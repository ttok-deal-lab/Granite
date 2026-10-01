package com.estateslug.slug.detail.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldPredictiveBackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.estateslug.slug.R

/**
 * 매물 목록 | 상세 2-pane 레이아웃. 창이 2-pane을 허용하지 않으면(너비 600dp 미만 — 접힌 폴드·세로 폰)
 * 목록만 그린다.
 *
 * 창이 허용하는 동안은 화면이 한 페인을 원할 때도(검색어 입력 화면·메인의 마이페이지 탭) 이 scaffold를 유지하고
 * [ProductPaneState]의 directive로만 줄인다 — 분기를 바꾸면 목록 쪽 NavHost가 다른 자리에서 다시 만들어진다.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProductListDetailPaneScaffold(
    state: ProductPaneState,
    modifier: Modifier = Modifier,
    listContent: @Composable () -> Unit,
) {
    if (state.windowSupportsTwoPane) {
        // 공식 가이드 패턴: Navigable*이 pane 백스택과 predictive back을 내장 처리
        NavigableListDetailPaneScaffold(
            modifier = modifier,
            navigator = state.paneNavigator,
            // 양쪽 pane이 항상 떠 있는 구성에선 scaffoldValue가 변하지 않으므로
            // back이 "상세 내용 닫기"로 동작하려면 content 기준 pop이 필요하다
            defaultBackBehavior = BackNavigationBehavior.PopUntilContentChange,
            listPane = {
                AnimatedPane { listContent() }
            },
            detailPane = {
                AnimatedPane {
                    // 화면이 한 페인을 원하면(검색어 입력 화면·마이페이지 탭) 안내 문구를 그리지 않고 바탕만 둔다 —
                    // 상세가 닫히며 페인이 줄어들기 전 몇 프레임, 빈 페인이 빠져나가는 애니메이션 동안
                    // "매물을 선택하면…"이 번쩍이지 않게
                    val productId = state.paneProductId
                    if (productId != null) {
                        DetailPaneHost(
                            productId = productId,
                            onClose = state::closeDetailPane,
                        )
                    } else if (state.twoPaneAllowed) {
                        DetailPaneEmpty()
                    }
                }
            },
        )
        // back은 나중에 등록된 핸들러가 먼저 받는다. scaffold 내장 핸들러는 목록 페인 안의 NavHost보다
        // 먼저 등록돼, 목록 쪽 백스택이 2개 이상이면(검색 결과·메인의 관심 탭) back이 상세를 닫지 않고
        // 목록을 뒤로 보낸다. 상세가 열려 있을 때만 같은 핸들러를 하나 더 두어 가장 나중 핸들러가 되게
        // 한다 — predictive back 애니메이션도 그대로다. 등록은 한 프레임 늦춘다: Activity가 다시 만들어져
        // 상세가 처음부터 열려 있으면, 목록 쪽 NavHost가 Scaffold 서브컴포지션(측정 단계)에서 같은 프레임에
        // 더 늦게 등록돼 이 핸들러를 앞지른다(검색 화면)
        val detailOpen = state.paneProductId != null
        var detailBackArmed by remember { mutableStateOf(false) }
        LaunchedEffect(detailOpen) {
            detailBackArmed = false
            if (detailOpen) {
                withFrameNanos { }
                detailBackArmed = true
            }
        }
        if (detailOpen && detailBackArmed) {
            ThreePaneScaffoldPredictiveBackHandler(
                navigator = state.paneNavigator,
                backBehavior = BackNavigationBehavior.PopUntilContentChange,
            )
        }
    } else {
        Box(modifier = modifier) { listContent() }
    }
}

@Composable
private fun DetailPaneEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.detail_pane_empty_title),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
