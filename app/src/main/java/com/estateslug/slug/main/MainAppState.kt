package com.estateslug.slug.main

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.estateslug.slug.detail.navigation.RouteDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * MainScreen의 네비게이션 UI 로직 홀더.
 *
 * 상세는 두 진입 경로를 가진다 — 접힘: NavHost RouteDetail push, 펼침(2-pane): paneNavigator contentKey.
 * 그 분기(openProduct)와 접힘↔펼침 이관(transferOpenDetail)을 이 클래스가 한 곳에서 책임진다.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Stable
class MainAppState(
    val navController: NavHostController,
    val paneNavigator: ThreePaneScaffoldNavigator<String>,
    val isTwoPane: Boolean,
    private val coroutineScope: CoroutineScope,
) {

    /** 상세 pane 내용의 단일 소스 (공식 list-detail 가이드 패턴) */
    val paneProductId: String?
        get() = paneNavigator.currentDestination?.contentKey

    /** data class 라우트(RouteDetail 등)는 qualifiedName 문자열 비교가 깨지므로 hasRoute로 판별 */
    fun matchTab(destination: NavDestination?): BottomBarItemUiModel? =
        destination?.let { d ->
            BottomBarItemUiModel.entries.find { d.hasRoute(it.route::class) }
        }

    /** 매물 상세 열기 — 펼침이면 pane 주입, 접힘이면 RouteDetail push */
    fun openProduct(productId: String) {
        if (isTwoPane) {
            coroutineScope.launch {
                paneNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, productId)
            }
        } else if (navController.currentBackStackEntry?.destination?.hasRoute<RouteDetail>() != true) {
            // 전환 중 다른 매물을 연타하면 launchSingleTop이 top 엔트리의 인자만 교체해
            // 이전 VM(이전 매물 데이터)이 재사용된다 — 상세가 이미 최상단이면 무시
            navController.navigate(RouteDetail(productId)) { launchSingleTop = true }
        }
    }

    fun closeDetailPane() {
        coroutineScope.launch {
            paneNavigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
        }
    }

    fun navigateToTab(item: BottomBarItemUiModel) {
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

    /** 접힘↔펼침 전환 시 열려 있는 상세를 반대편 진입 경로로 이관 — 보던 상세가 끊기지 않는다 */
    suspend fun transferOpenDetail() {
        if (isTwoPane) {
            val entry = navController.currentBackStackEntry
            if (entry?.destination?.hasRoute<RouteDetail>() == true) {
                val productId = entry.toRoute<RouteDetail>().productId
                navController.popBackStack()
                paneNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, productId)
            }
        } else {
            paneNavigator.currentDestination?.contentKey?.let { id ->
                // 다음 펼침에서 이중 표시되지 않도록 pane 백스택을 비우고 NavHost로 이관
                if (paneNavigator.canNavigateBack(BackNavigationBehavior.PopUntilContentChange)) {
                    paneNavigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
                }
                if (navController.currentBackStackEntry?.destination?.hasRoute<RouteDetail>() != true) {
                    navController.navigate(RouteDetail(id)) { launchSingleTop = true }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun rememberMainAppState(): MainAppState {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    // 확장 너비(펼친 폴더블·태블릿)에서만 2-pane, 접힘/일반 폰은 기존 NavHost 흐름 유지.
    // WindowSizeClass 직접 분기 대신 directive로 판별 — androidx.window 버전 차이에 흔들리지 않는다
    // 기본 directive는 840dp(EXPANDED)부터 2-pane이라 Galaxy Z Fold 내부 화면(세로 ~690dp,
    // 가로 ~829dp — MEDIUM)에서 영영 분할되지 않는다. 국내 주력 폴더블이 전부 MEDIUM 구간이므로
    // 600dp부터 2-pane을 허용하는 variant를 사용한다
    val scaffoldDirective =
        calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfoV2())
    val isTwoPane = scaffoldDirective.maxHorizontalPartitions > 1

    // 상세 pane 내용의 단일 소스는 navigator의 contentKey.
    // 접힘 상태에서도 pane 상태를 읽어 이관해야 하므로 2-pane 분기 밖(여기)에서 생성한다
    val paneNavigator =
        rememberListDetailPaneScaffoldNavigator<String>(scaffoldDirective = scaffoldDirective)

    return remember(navController, paneNavigator, isTwoPane, coroutineScope) {
        MainAppState(
            navController = navController,
            paneNavigator = paneNavigator,
            isTwoPane = isTwoPane,
            coroutineScope = coroutineScope,
        )
    }
}
