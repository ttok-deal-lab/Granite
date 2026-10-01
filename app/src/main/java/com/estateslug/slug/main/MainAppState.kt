package com.estateslug.slug.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
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
    lastTabState: State<BottomBarItemUiModel>,
) {

    /**
     * 마지막으로 머문 탭. 상세 위(탭 아님)나 엔트리가 없는 첫 프레임에서도 그대로라
     * 레일·하단 바 하이라이트와 상세 페인 사용 여부가 이 값을 따른다
     */
    val lastTab: BottomBarItemUiModel by lastTabState

    /**
     * 하단 바 대신 레일 + 목록 | 상세 scaffold를 쓰는 넓은 창인가 — 창 너비로만 정한다.
     * 탭이 한 페인을 쓰는지([ProductPaneState.isTwoPane])로 정하면 마이페이지에서 레이아웃이 하단 바로 바뀌고
     * 목록 쪽 NavHost가 다른 자리에서 다시 만들어진다
     */
    val isWideLayout: Boolean
        get() = productPane.windowSupportsTwoPane

    /** 매물 상세 열기 — 펼침이면 pane 주입, 접힘이면 RouteDetail push */
    fun openProduct(productId: String) = productPane.openProduct(productId)

    fun navigateToTab(item: BottomBarItemUiModel) {
        // 넓은 화면에서 다른 탭으로 옮기면 열려 있던 상세 페인을 닫는다 — 페인 상태는 탭과 무관한
        // 앱 단위라 그대로 두면 홈에서 연 상세가 관심 탭 옆에 남는다 (2026-09-16 실기기 QA 관찰).
        // 같은 탭 재탭은 기존처럼 no-op(페인 유지). 접힘 상태의 상세는 NavHost 위에 있어 아래
        // popUpTo가 함께 걷어낸다
        val currentTab = matchTab(navController.currentBackStackEntry?.destination)
        if (isWideLayout && currentTab != item) {
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

/** data class 라우트(RouteDetail 등)는 qualifiedName 문자열 비교가 깨지므로 hasRoute로 판별 */
internal fun matchTab(destination: NavDestination?): BottomBarItemUiModel? =
    destination?.let { d ->
        BottomBarItemUiModel.entries.find { d.hasRoute(it.route::class) }
    }

@Composable
fun rememberMainAppState(startItem: BottomBarItemUiModel): MainAppState {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 상세 위(매치 없음)에서도 마지막 탭을 유지 — 바 퇴장 애니메이션 중 HOME으로 튀지 않게.
    // 엔트리가 없는 첫 프레임(회전·process death 복원 직후)도 이 값을 써서 상세 페인 사용 여부가 흔들리지 않는다
    val lastTabState = rememberSaveable { mutableStateOf(startItem) }
    val matchedTab = matchTab(navBackStackEntry?.destination)
    if (matchedTab != null && matchedTab != lastTabState.value) lastTabState.value = matchedTab

    // 매물 목록 탭만 목록 | 상세로 나눈다. 마이페이지처럼 매물이 없는 탭은 펼친 화면에서도
    // 레일 옆을 한 페인으로 쓴다 — 오른쪽에 "매물을 선택하면…" 빈 페인이 붙지 않게
    val productPane = rememberProductPaneState(
        navController = navController,
        twoPaneAllowed = lastTabState.value.showsProductDetail,
    )

    return remember(navController, productPane, lastTabState) {
        MainAppState(
            navController = navController,
            productPane = productPane,
            lastTabState = lastTabState,
        )
    }
}
