package com.estateslug.slug.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.deeplink.DeepLinkKeys
import com.estateslug.slug.detail.navigation.RouteDetail
import com.estateslug.slug.detail.navigation.detailNavGraph
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetContent
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetType
import com.estateslug.slug.search.component.SearchTopBar
import com.estateslug.slug.search.navigation.RouteSearchBridge
import com.estateslug.slug.search.navigation.RouteSearchResult
import com.estateslug.slug.search.navigation.searchNavGraph
import com.estateslug.slug.ui.theme.SlugTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialKeyword = intent?.getStringExtra(DeepLinkKeys.SEARCH_KEYWORD).orEmpty()
        setContent {
            val searchViewModel: SearchViewModel = hiltViewModel()
            SlugTheme {
                val navController = rememberNavController()

                // 딥링크로 검색어가 넘어온 경우 결과 화면까지 바로 진입
                LaunchedEffect(Unit) {
                    if (initialKeyword.isNotBlank()) {
                        searchViewModel.updateSearchKeyword(initialKeyword)
                        searchViewModel.searchWithCheck(initialKeyword) {
                            navController.navigate(RouteSearchResult(initialKeyword)) {
                                popUpTo<RouteSearchBridge> { inclusive = false }
                            }
                        }
                    }
                }

                var isBottomSheetShowing by remember { mutableStateOf(false) }
                var bottomSheetType by remember {
                    mutableStateOf<SearchBottomSheetType>(
                        SearchBottomSheetType.ListSorting
                    )
                }
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                val coroutineScope = rememberCoroutineScope()

                val requestHideBottomSheet: () -> Unit = {
                    coroutineScope.launch {
                        sheetState.hide()
                        isBottomSheetShowing = false
                    }
                }

                val searchKeyword by searchViewModel.searchKeyword.collectAsStateWithLifecycle()
                val currentEntry by navController.currentBackStackEntryAsState()
                val isOnSearchResult =
                    currentEntry?.destination?.route?.contains("RouteSearchResult") == true
                // 상세는 자체 TopBar를 가지므로 검색 TopBar를 숨긴다
                val isOnDetail =
                    currentEntry?.destination?.hasRoute<RouteDetail>() == true

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        // 상세에서는 IME 인셋을 적용하지 않는다 — 키보드가 열린 채 진입해도
                        // 상세가 키보드 높이만큼 줄어든 채 그려지지 않게
                        .then(if (isOnDetail) Modifier else Modifier.imePadding())
                        .systemBarsPadding(),
                    topBar = {
                        // 구조적 제거 대신 AnimatedVisibility — innerPadding.top이 전환과 함께
                        // 애니메이션되어 검색 결과가 위로 튀는 현상을 막는다
                        AnimatedVisibility(
                            visible = !isOnDetail,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut(),
                        ) { SearchTopBar(
                            searchText = searchKeyword,
                            onSearchTextChange = { keyword ->
                                searchViewModel.updateSearchKeyword(keyword)
                                if (isOnSearchResult) {
                                    navController.popBackStack()
                                }
                            },
                            onBackClick = {
                                if (isOnSearchResult) {
                                    navController.popBackStack()
                                    searchViewModel.clearSearchKeyword()
                                } else {
                                    finish()
                                }
                            },
                            onKeywordClearClick = {
                                searchViewModel.clearSearchKeyword()
                                if (isOnSearchResult) {
                                    navController.popBackStack()
                                }
                            },
                            onSearch = { keyword ->
                                if (isOnSearchResult) {
                                    searchViewModel.search(keyword)
                                } else {
                                    searchViewModel.searchWithCheck(keyword) {
                                        navController.navigate(RouteSearchResult(keyword)) {
                                            popUpTo<RouteSearchBridge> { inclusive = false }
                                        }
                                    }
                                }
                            },
                            onCloseClick = { finish() },
//                            autoFocus = !isOnSearchResult
                        )
                        }

                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = RouteSearchBridge,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            searchNavGraph(
                                searchViewModel = searchViewModel,
                                navController = navController,
                                onItemClick = { itemId ->
                                    // 전환 중 연타 시 인자 교체로 잘못된 매물이 뜨는 것 방지
                                    if (navController.currentBackStackEntry?.destination
                                            ?.hasRoute<RouteDetail>() != true
                                    ) {
                                        navController.navigate(RouteDetail(itemId)) {
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onShowBottomSheet = { type ->
                                    bottomSheetType = type
                                    isBottomSheetShowing = true
                                }
                            )

                            detailNavGraph(onBack = { navController.popBackStack() })
                        }
                    }

                    if (isBottomSheetShowing) {
                        ModalBottomSheet(
                            onDismissRequest = { isBottomSheetShowing = false },
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
                                SearchBottomSheetContent(
                                    searchViewModel = searchViewModel,
                                    bottomSheetType = bottomSheetType,
                                    requestHideBottomSheet = requestHideBottomSheet
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}
