package com.estateslug.slug.detail.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

/**
 * 매물 목록에서 상세를 여는 화면(메인·검색·최근 본)의 상세 진입 경로 홀더.
 *
 * 상세는 두 진입 경로를 가진다 — 한 페인: NavHost RouteDetail push, 2-pane: paneNavigator contentKey.
 * 그 분기(openProduct)와 창 너비가 바뀔 때의 이관(transferOpenDetail)을 이 클래스가 한 곳에서 책임진다.
 * 레이아웃은 [ProductListDetailPaneScaffold]가 이 상태를 받아 그린다.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Stable
class ProductPaneState(
    internal val navController: NavHostController,
    internal val paneNavigator: ThreePaneScaffoldNavigator<String>,
    /** 창 너비가 2-pane을 허용하는가(600dp 이상 — 펼친 폴더블·태블릿, 가로로 든 폰도 포함). 이관의 기준 */
    val windowSupportsTwoPane: Boolean,
    /** 지금 목록 | 상세로 나눠 보여 주는가. 창이 허용해도 화면이 한 페인을 원할 수 있다(검색어 입력 화면) */
    val isTwoPane: Boolean,
    private val coroutineScope: CoroutineScope,
) {

    /** 상세 pane 내용의 단일 소스 (공식 list-detail 가이드 패턴) */
    val paneProductId: String?
        get() = paneNavigator.currentDestination?.contentKey

    /** 매물 상세 열기 — 2-pane이면 pane 주입, 아니면 RouteDetail push */
    fun openProduct(productId: String) {
        if (isTwoPane) {
            coroutineScope.launch { showInPane(productId) }
        } else if (navController.currentBackStackEntry?.destination?.hasRoute<RouteDetail>() != true) {
            // 전환 중 다른 매물을 연타하면 launchSingleTop이 top 엔트리의 인자만 교체해
            // 이전 VM(이전 매물 데이터)이 재사용된다 — 상세가 이미 최상단이면 무시
            navController.navigate(RouteDetail(productId)) { launchSingleTop = true }
        }
    }

    /**
     * 상세 페인에 매물을 띄운다. 히스토리는 항상 [목록, 상세] 한 쌍으로 유지한다.
     * - 상세를 쌓지 않고 바꿔 끼운다 — 쌓으면 닫기·back·이관이 한 단계만 빼서 이전 상세가 남는다
     *   (새 검색·탭 전환 뒤에도). 2-pane에서는 앞뒤 배치가 같아 빈 페인이 끼지 않는다
     * - 연속 back으로 navigator가 히스토리를 통째로 비우면([List]에서 한 번 더 navigateBack) 바닥(목록)을
     *   다시 깐다 — 없으면 이후 연 상세는 되돌아갈 곳이 없어 닫히지 않는다
     */
    private suspend fun showInPane(productId: String) {
        if (paneNavigator.currentDestination?.contentKey != null) {
            paneNavigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
        }
        if (paneNavigator.currentDestination == null) {
            paneNavigator.navigateTo(ListDetailPaneScaffoldRole.List)
        }
        paneNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, productId)
    }

    /** 열려 있는 상세 pane을 닫는다. 비어 있으면 아무것도 하지 않는다 */
    fun closeDetailPane() {
        if (!paneNavigator.canNavigateBack(BackNavigationBehavior.PopUntilContentChange)) return
        coroutineScope.launch {
            paneNavigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
        }
    }

    /** 창 너비가 바뀔 때(접기·펴기 등) 열려 있는 상세를 반대편 진입 경로로 이관 — 보던 상세가 끊기지 않는다 */
    internal suspend fun transferOpenDetail() {
        if (windowSupportsTwoPane) {
            val entry = navController.currentBackStackEntry
            if (entry?.destination?.hasRoute<RouteDetail>() == true) {
                val productId = entry.toRoute<RouteDetail>().productId
                navController.popBackStack()
                showInPane(productId)
            }
        } else {
            paneNavigator.currentDestination?.contentKey?.let { id ->
                // 다음 펼침에서 이중 표시되지 않도록 pane의 상세를 빼고 NavHost로 이관
                // (openProduct가 상세를 쌓지 않으므로 한 단계면 목록까지 돌아간다)
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

/**
 * @param twoPaneAllowed 이 화면이 지금 목록 | 상세로 나뉘어도 되는가. 창 너비와 별개로
 * 화면 쪽 사정(매물 목록이 아닌 검색어 입력 화면 등)으로 한 페인만 쓰고 싶을 때 false
 * @param autoFocusPane 페인 배치가 바뀔 때 scaffold가 현재 페인으로 포커스를 옮길지.
 * 목록 페인 맨 위에 입력칸이 있는 화면(검색)은 false — 포커스가 입력칸으로 가서 키보드가 올라온다.
 * 화면마다 고정 값으로 넘길 것: directive의 equals가 이 값을 비교하지 않아 도중에 바꾸면 반영되지 않는다
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun rememberProductPaneState(
    navController: NavHostController,
    twoPaneAllowed: Boolean = true,
    autoFocusPane: Boolean = true,
): ProductPaneState {
    val coroutineScope = rememberCoroutineScope()

    // 창 너비 600dp 이상에서만 2-pane, 그보다 좁으면 기존 NavHost 흐름 유지.
    // WindowSizeClass 직접 분기 대신 directive로 판별 — androidx.window 버전 차이에 흔들리지 않는다
    // 기본 directive는 840dp(EXPANDED)부터 2-pane이라 Galaxy Z Fold 내부 화면(세로 ~690dp,
    // 가로 ~829dp — MEDIUM)에서 영영 분할되지 않는다. 국내 주력 폴더블이 전부 MEDIUM 구간이므로
    // 600dp부터 2-pane을 허용하는 variant를 사용한다. 가로로 든 폰·접힌 폴드 커버 화면 가로도
    // 600dp를 넘어 2-pane이 된다 — 메인과 같은 현상 유지 방침(백로그 C)
    val windowDirective =
        calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(currentWindowAdaptiveInfoV2())
    val windowSupportsTwoPane = windowDirective.maxHorizontalPartitions > 1

    // 화면이 한 페인을 원해도 상세가 열려 있는 동안은 나눈 채로 둔다 — 먼저 한 페인으로 줄이면
    // 그 프레임에 열려 있던 상세가 전체 폭으로 번쩍인다. 호출 측이 상세를 닫으면 그다음에 줄어든다
    var paneHasContent by remember { mutableStateOf(false) }
    val isTwoPane = windowSupportsTwoPane && (twoPaneAllowed || paneHasContent)
    val scaffoldDirective = windowDirective.adjusted(
        maxHorizontalPartitions = if (isTwoPane) windowDirective.maxHorizontalPartitions else 1,
        shouldAutoFocusCurrentDestination =
            autoFocusPane && windowDirective.shouldAutoFocusCurrentDestination,
    )

    // 상세 pane 내용의 단일 소스는 navigator의 contentKey.
    // 한 페인 상태에서도 pane 상태를 읽어 이관해야 하므로 2-pane 분기 밖(여기)에서 생성한다
    val paneNavigator =
        rememberListDetailPaneScaffoldNavigator<String>(scaffoldDirective = scaffoldDirective)
    LaunchedEffect(paneNavigator) {
        snapshotFlow { paneNavigator.currentDestination?.contentKey != null }
            .collect { paneHasContent = it }
    }
    // navigator는 directive를 받아도 화면에 보이는 scaffold 상태를 다음 이동(navigateTo/Back) 때까지
    // 옛 배치로 둔다(adaptive 1.3.0 소스 확인) — 창 크기가 바뀌면 Activity가 다시 만들어져 문제가 없지만,
    // 화면이 한 페인↔두 페인을 바꾸면(검색어 입력 화면 → 결과) 결과 목록이 전체 폭에 남는다.
    // directive가 바뀔 때만 현재 배치로 맞춘다. seekBack(fraction = 0f)는 "뒤로 0%" — 기본 navigator
    // 구현에서는 현재 목적지의 배치로 애니메이션한다(인터페이스 문서가 보장하는 동작은 아니다)
    LaunchedEffect(paneNavigator) {
        snapshotFlow { paneNavigator.scaffoldDirective }
            .drop(1)
            .collect {
                val scaffoldState = paneNavigator.scaffoldState
                if (scaffoldState.isPredictiveBackInProgress ||
                    scaffoldState.targetState == paneNavigator.scaffoldValue
                ) return@collect
                try {
                    paneNavigator.seekBack(fraction = 0f)
                } catch (e: CancellationException) {
                    // 같은 상태를 움직이는 다른 애니메이션(상세 열기·predictive back)에 밀려 끊긴 것이면
                    // 수집을 이어 간다 — 그대로 던지면 이 효과가 끝나 다음 전환부터 배치가 따라오지 않는다.
                    // 효과 자체가 취소된 경우(화면을 떠남)만 다시 던진다
                    currentCoroutineContext().ensureActive()
                }
            }
    }

    val state = remember(navController, paneNavigator, windowSupportsTwoPane, isTwoPane, coroutineScope) {
        ProductPaneState(
            navController = navController,
            paneNavigator = paneNavigator,
            windowSupportsTwoPane = windowSupportsTwoPane,
            isTwoPane = isTwoPane,
            coroutineScope = coroutineScope,
        )
    }
    // 창 너비가 바뀔 때(접기·펴기, 회전) 보던 상세를 반대편 진입 경로로 옮긴다
    LaunchedEffect(windowSupportsTwoPane) {
        state.transferOpenDetail()
    }
    return state
}

/** copy()는 자동 포커스 여부를 true로 되돌리므로 전체 생성자로 다시 만든다 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun PaneScaffoldDirective.adjusted(
    maxHorizontalPartitions: Int,
    shouldAutoFocusCurrentDestination: Boolean,
): PaneScaffoldDirective =
    PaneScaffoldDirective(
        maxHorizontalPartitions = maxHorizontalPartitions,
        horizontalPartitionSpacerSize = horizontalPartitionSpacerSize,
        maxVerticalPartitions = maxVerticalPartitions,
        verticalPartitionSpacerSize = verticalPartitionSpacerSize,
        defaultPanePreferredWidth = defaultPanePreferredWidth,
        defaultPanePreferredHeight = defaultPanePreferredHeight,
        excludedBounds = excludedBounds,
        shouldAutoFocusCurrentDestination = shouldAutoFocusCurrentDestination,
    )
