package com.estateslug.slug.main

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
import androidx.navigation.compose.rememberNavController
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
    mainViewModel: MainViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedItem = navBackStackEntry?.destination?.route?.let { currentRoute ->
        BottomBarItemUiModel.entries.find { item ->
            currentRoute == item.route::class.qualifiedName
        }
    } ?: BottomBarItemUiModel.HOME
    val onClick: (BottomBarItemUiModel) -> Unit = {
        navController.navigate(it.route)
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
            MainBottomBar(
                selectedItem = selectedItem,
                onClick = onClick
            )
        }
    )
}

@Composable
fun MainNavHost(
//    navigator: MainNavigator,
    padding: PaddingValues,
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            homeNavGraph(padding = padding)

            favoriteNavGraph(padding = padding)

            myPageNavGraph(padding = padding)
        }
    }
}
