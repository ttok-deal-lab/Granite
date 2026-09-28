package com.estateslug.slug.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.estateslug.slug.detail.navigation.ProductListDetailPaneScaffold
import com.estateslug.slug.detail.navigation.detailNavGraph
import com.estateslug.slug.favorite.favoriteNavGraph
import com.estateslug.slug.home.bottomsheet.HomeBottomSheetContent
import com.estateslug.slug.home.navigation.homeNavGraph
import com.estateslug.slug.mypage.myPageNavGraph
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    startItem: BottomBarItemUiModel = BottomBarItemUiModel.HOME,
    startProductId: String? = null,
    mainViewModel: MainViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
) {
    // 네비게이션 UI 로직(상세 두 진입 경로 분기, 접힘↔펼침 이관, 탭 이동)은 MainAppState가 담당
    val appState = rememberMainAppState()

    // 딥링크/FCM으로 전달된 상세 id를 1회만 소비 (rememberSaveable 가드로 재컴포지션·복원 시 이중 push 방지)
    var isStartProductConsumed by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (startProductId != null && !isStartProductConsumed) {
            isStartProductConsumed = true
            appState.openProduct(startProductId)
        }
    }

    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val matchedTab = appState.matchTab(navBackStackEntry?.destination)
    // 상세 위에서도(매치 없음) 마지막 탭 하이라이트를 유지 — 바 퇴장 애니메이션 중 HOME으로 튀지 않게
    var lastTab by rememberSaveable { mutableStateOf(startItem) }
    if (matchedTab != null && matchedTab != lastTab) lastTab = matchedTab
    val isTabDestination = matchedTab != null

    Scaffold(
        modifier = modifier
            .navigationBarsPadding()
            // targetSdk 35+는 컷아웃 모드가 ALWAYS로 고정돼 시스템이 레터박스를 넣지 않는다.
            // 세로에선 상태바가 펀치홀을 덮지만(좌우 인셋 0 → 변화 없음), 펼친 폴더블을 가로로 들면
            // 구멍이 측면으로 와 레일·상세 페인 가장자리를 가리므로 좌우만 피한다. 상단은 상태바
            // 인셋이 이미 처리하니 포함하면 이중 패딩이 된다 (2026-09-16 Z Fold 실측)
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
            // 삼성 폴더블 펼침 화면은 카메라 구멍을 컷아웃으로 선언하지 않아 위 인셋이 0 — 가로에서 구멍 쪽에
            // 같은 모양의 여백을 직접 준다 (UDC가 아닌 실제 구멍 모델만, InnerCameraSafePadding.kt)
            .padding(rememberInnerCameraSafePadding()),
        content = { paddingValues ->
            if (appState.isTwoPane) {
                // 확장 너비에선 하단 바 대신 좌측 레일 — 세로 공간을 목록/상세에 온전히 양보
                Row(modifier = Modifier.fillMaxSize()) {
                    MainNavRail(
                        selectedItem = lastTab,
                        onClick = appState::navigateToTab,
                    )
                    ProductListDetailPaneScaffold(
                        state = appState.productPane,
                        modifier = Modifier.weight(1f),
                    ) {
                        MainNavHost(
                            padding = paddingValues,
                            navController = appState.navController,
                            startDestination = startItem.route,
                            onProductClick = appState::openProduct,
                        )
                    }
                }
            } else {
                MainNavHost(
                    padding = paddingValues,
                    navController = appState.navController,
                    startDestination = startItem.route,
                    onProductClick = appState::openProduct,
                )
            }
            MainBottomSheetHost(mainViewModel = mainViewModel)
        },
        bottomBar = {
            // 2-pane(확장 너비)에서는 좌측 레일이 대신하므로 하단 바를 렌더하지 않는다.
            // 상세 등 탭 외 destination에서는 바텀바 숨김.
            // entry가 null인 첫 프레임(회전·process death 복원 직후)은 렌더하지 않아
            // 상세 위에서 바가 헛돌며 퇴장 애니메이션되는 현상을 막는다
            if (!appState.isTwoPane && navBackStackEntry != null) {
                AnimatedVisibility(
                    visible = isTabDestination,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    MainBottomBar(
                        selectedItem = lastTab,
                        onClick = appState::navigateToTab
                    )
                }
            }
        }
    )
}

/** MainViewModel이 제어하는 공용 ModalBottomSheet — 표시 상태·sheetState·숨김 요청을 한 곳에 묶는다 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainBottomSheetHost(
    mainViewModel: MainViewModel,
) {
    val bottomSheetType by mainViewModel.isNeedToShowBottomSheet.collectAsStateWithLifecycle(
        MainBottomSheetType.EMPTY
    )

    var isBottomSheetShowing: Boolean by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(bottomSheetType) {
        isBottomSheetShowing = bottomSheetType != MainBottomSheetType.EMPTY
    }
    val coroutineScope = rememberCoroutineScope()
    val requestHideBottomSheet: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
            mainViewModel.requestToShowBottomSheet(MainBottomSheetType.EMPTY)
        }
    }

    if (isBottomSheetShowing)
        ModalBottomSheet(
            onDismissRequest = {
                isBottomSheetShowing = false
                mainViewModel.requestToShowBottomSheet(MainBottomSheetType.EMPTY)
            },
            sheetState = sheetState,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )
            },
            content = {
                when (val sheetType = bottomSheetType) {
                    is MainBottomSheetType.HomeBottomSheetType -> {
                        HomeBottomSheetContent(
                            bottomSheetType = sheetType,
                            requestHideBottomSheet = requestHideBottomSheet
                        )
                    }

                    MainBottomSheetType.EMPTY -> {

                    }

                }
            }
        )
}

@Composable
fun MainNavHost(
//    navigator: MainNavigator,
    padding: PaddingValues,
    navController: NavHostController,
    startDestination: Route,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            homeNavGraph(padding = padding, onProductClick = onProductClick)

            favoriteNavGraph(padding = padding, onProductClick = onProductClick)

            myPageNavGraph(padding = padding)

            detailNavGraph(onBack = { navController.popBackStack() })
        }
    }
}
