package com.estateslug.slug.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.detail.navigation.RouteDetail
import com.estateslug.slug.detail.navigation.detailNavGraph
import com.estateslug.slug.favorite.favoriteNavGraph
import com.estateslug.slug.home.bottomsheet.HomeBottomSheetContent
import com.estateslug.slug.home.navigation.RouteHome
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

    val navController = rememberNavController()

    // 딥링크/FCM으로 전달된 상세 id를 1회만 소비 (rememberSaveable 가드로 재컴포지션·복원 시 이중 push 방지)
    var isStartProductConsumed by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (startProductId != null && !isStartProductConsumed) {
            isStartProductConsumed = true
            navController.navigate(RouteDetail(startProductId)) { launchSingleTop = true }
        }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // data class 라우트(RouteDetail 등)는 qualifiedName 문자열 비교가 깨지므로 hasRoute로 판별
    val matchedTab = navBackStackEntry?.destination?.let { destination ->
        BottomBarItemUiModel.entries.find { item -> destination.hasRoute(item.route::class) }
    }
    // 상세 위에서도(매치 없음) 마지막 탭 하이라이트를 유지 — 바 퇴장 애니메이션 중 HOME으로 튀지 않게
    var lastTab by rememberSaveable { mutableStateOf(startItem) }
    if (matchedTab != null && matchedTab != lastTab) lastTab = matchedTab
    val isTabDestination = matchedTab != null
    val onClick: (BottomBarItemUiModel) -> Unit = {
        navController.navigate(it.route)
    }
    val onProductClick: (String) -> Unit = { productId ->
        // 전환 중 다른 매물을 연타하면 launchSingleTop이 top 엔트리의 인자만 교체해
        // 이전 VM(이전 매물 데이터)이 재사용된다 — 상세가 이미 최상단이면 무시
        if (navController.currentBackStackEntry?.destination?.hasRoute<RouteDetail>() != true) {
            navController.navigate(RouteDetail(productId)) { launchSingleTop = true }
        }
    }
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

    Scaffold(
        modifier = modifier.navigationBarsPadding(),
        content = { paddingValues ->
            //MainNavHost
            MainNavHost(
                padding = paddingValues,
                navController = navController,
                startDestination = startItem.route,
                onProductClick = onProductClick,
            )
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
        },
        bottomBar = {
            // 상세 등 탭 외 destination에서는 바텀바 숨김.
            // entry가 null인 첫 프레임(회전·process death 복원 직후)은 렌더하지 않아
            // 상세 위에서 바가 헛돌며 퇴장 애니메이션되는 현상을 막는다
            if (navBackStackEntry != null) {
                AnimatedVisibility(
                    visible = isTabDestination,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    MainBottomBar(
                        selectedItem = lastTab,
                        onClick = onClick
                    )
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
