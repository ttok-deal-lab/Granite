package com.estateslug.slug.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.detail.navigation.ProductPaneState
import com.estateslug.slug.detail.navigation.rememberProductPaneState

/**
 * MainScreen의 네비게이션 UI 로직 홀더.
 *
 * 탭 이동을 맡고, 상세의 두 진입 경로(접힘 NavHost push / 펼침 2-pane)는 [ProductPaneState]에 맡긴다 —
 * 검색·최근 본 화면도 같은 상태 홀더를 쓴다.
 */
@Stable
class MainAppState(
    val navController: NavHostController,
    val productPane: ProductPaneState,
) {

    val isTwoPane: Boolean
        get() = productPane.isTwoPane

    /** data class 라우트(RouteDetail 등)는 qualifiedName 문자열 비교가 깨지므로 hasRoute로 판별 */
    fun matchTab(destination: NavDestination?): BottomBarItemUiModel? =
        destination?.let { d ->
            BottomBarItemUiModel.entries.find { d.hasRoute(it.route::class) }
        }

    /** 매물 상세 열기 — 펼침이면 pane 주입, 접힘이면 RouteDetail push */
    fun openProduct(productId: String) = productPane.openProduct(productId)

    fun navigateToTab(item: BottomBarItemUiModel) {
        // 2-pane에서 다른 탭으로 옮기면 열려 있던 상세 페인을 닫는다 — 페인 상태는 탭과 무관한
        // 앱 단위라 그대로 두면 홈에서 연 상세가 관심 탭 옆에 남는다 (2026-09-16 실기기 QA 관찰).
        // 같은 탭 재탭은 기존처럼 no-op(페인 유지). 접힘 상태의 상세는 NavHost 위에 있어 아래
        // popUpTo가 함께 걷어낸다
        val currentTab = matchTab(navController.currentBackStackEntry?.destination)
        if (isTwoPane && currentTab != item) {
            productPane.closeDetailPane()
        }
        navController.navigate(item.route) {
            // 탭당 인스턴스 1개 유지(multiple back stacks 패턴):
            // back은 시작 탭으로 수렴해 2회 종료 로직과 맞물리고,
            // 떠난 탭은 saveState/restoreState로 백스택·스크롤·VM까지 보존된다
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun rememberMainAppState(): MainAppState {
    val navController = rememberNavController()
    val productPane = rememberProductPaneState(navController)

    return remember(navController, productPane) {
        MainAppState(
            navController = navController,
            productPane = productPane,
        )
    }
}
