package com.estateslug.slug.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.deeplink.DeepLinkKeys
import com.estateslug.slug.detail.navigation.ProductListDetailPaneScaffold
import com.estateslug.slug.detail.navigation.RouteDetail
import com.estateslug.slug.detail.navigation.detailNavGraph
import com.estateslug.slug.detail.navigation.rememberProductPaneState
import com.estateslug.slug.main.rememberInnerCameraSafePadding
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetContent
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetType
import com.estateslug.slug.search.component.SearchTopBar
import com.estateslug.slug.search.navigation.RouteSearchBridge
import com.estateslug.slug.search.navigation.RouteSearchResult
import com.estateslug.slug.search.navigation.searchNavGraph
import com.estateslug.slug.ui.theme.SlugTheme
import androidx.activity.enableEdgeToEdge
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
                val focusManager = LocalFocusManager.current
                val currentEntry by navController.currentBackStackEntryAsState()
                // entry가 null인 첫 프레임(백스택 복원 전)은 마지막으로 본 화면을 따른다 — 처음 열 때는
                // 시작 화면(최근 검색어)이고, 접기·펴기·다크 전환으로 Activity가 다시 만들어질 때는 보던
                // 결과 화면이다. 시작 화면으로 단정하면 아래 효과가 열려 있던 상세를 닫아 버린다
                var wasOnBridge by rememberSaveable { mutableStateOf(true) }
                val isOnBridge =
                    currentEntry?.destination?.hasRoute<RouteSearchBridge>() ?: wasOnBridge
                val isOnSearchResult =
                    currentEntry?.destination?.hasRoute<RouteSearchResult>() == true
                // 상세는 자체 TopBar를 가지므로 검색 TopBar를 숨긴다 (접힘에서 NavHost로 연 상세)
                val isOnDetail =
                    currentEntry?.destination?.hasRoute<RouteDetail>() == true

                // 펼친 화면에서는 결과 목록부터 목록 | 상세로 나눈다 — 최근 검색어 화면은 매물 목록이
                // 아니므로 빈 상세 페인을 두지 않고 전체 폭으로 쓴다.
                // 목록 페인 맨 위가 검색 입력칸이라, 페인 배치가 바뀔 때 scaffold가 포커스를 옮기면
                // 결과 화면에 들어갈 때마다 키보드가 올라온다 — 자동 포커스는 끈다
                val paneState = rememberProductPaneState(
                    navController,
                    twoPaneAllowed = !isOnBridge,
                    autoFocusPane = false,
                )
                // 최근 검색어 화면으로 돌아오면(뒤로·검색어 수정·지우기) 보던 결과의 상세를 닫는다
                LaunchedEffect(isOnBridge) {
                    wasOnBridge = isOnBridge
                    if (isOnBridge) paneState.closeDetailPane()
                }

                // 딥링크로 검색어가 넘어온 경우 결과 화면까지 바로 진입.
                // 결과 화면으로 넘어간 뒤에는 접기·펴기로 Activity가 다시 만들어져도 다시 하지 않는다 —
                // 다시 하면 보던 상세가 결과로 되돌아간다. 표시는 실제로 넘어가는 순간에 한다: 응답 전에
                // 다시 만들어지면 옛 화면의 콜백은 버려지므로(재요청이 이전 요청을 취소) 새로 요청해야 한다
                var isInitialKeywordConsumed by rememberSaveable { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    if (initialKeyword.isNotBlank() && !isInitialKeywordConsumed) {
                        searchViewModel.updateSearchKeyword(initialKeyword)
                        searchViewModel.searchWithCheck(initialKeyword) {
                            isInitialKeywordConsumed = true
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

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        // 펼친 폴더블을 가로로 들면 펀치홀이 측면으로 와 목록·상세 가장자리를 가린다 —
                        // MainScreen과 같은 처리(좌우 컷아웃 인셋 + 삼성 펼침 화면 카메라 여백)
                        .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
                        .padding(rememberInnerCameraSafePadding()),
                ) { innerPadding ->
                    ProductListDetailPaneScaffold(
                        state = paneState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    ) {
                        // 검색바는 목록 페인에만 둔다 — 2-pane의 상세 페인은 자체 상단바를 쓴다
                        Scaffold(
                            // 상세에서는 IME 인셋을 적용하지 않는다 — 키보드가 열린 채 진입해도
                            // 상세가 키보드 높이만큼 줄어든 채 그려지지 않게. 2-pane의 상세 페인은
                            // 이 Scaffold 밖이라 처음부터 영향이 없다
                            modifier = if (isOnDetail) Modifier else Modifier.imePadding(),
                            // 시스템 바 인셋은 바깥 Scaffold가 이미 처리했다
                            contentWindowInsets = WindowInsets(0),
                            topBar = {
                                // 구조적 제거 대신 AnimatedVisibility — listPadding.top이 전환과 함께
                                // 애니메이션되어 검색 결과가 위로 튀는 현상을 막는다
                                AnimatedVisibility(
                                    visible = !isOnDetail,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut(),
                                ) {
                                    SearchTopBar(
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
                                                // 결과 목록이 바뀌므로 이전 검색에서 연 상세는 닫는다
                                                paneState.closeDetailPane()
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
//                                        autoFocus = !isOnSearchResult
                                    )
                                }
                            },
                        ) { listPadding ->
                            Box(
                                modifier = Modifier
                                    .padding(listPadding)
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
                                        // 2-pane이면 상세 페인, 아니면 RouteDetail push (연타 방지 포함).
                                        // 한 페인에서는 상세로 넘어가며 검색바가 빠져 키보드가 내려가지만,
                                        // 2-pane에서는 검색바가 남아 키보드가 상세 페인을 가린다 — 포커스를 푼다
                                        onItemClick = { itemId ->
                                            focusManager.clearFocus()
                                            paneState.openProduct(itemId)
                                        },
                                        onShowBottomSheet = { type ->
                                            bottomSheetType = type
                                            isBottomSheetShowing = true
                                        }
                                    )

                                    detailNavGraph(onBack = { navController.popBackStack() })
                                }
                            }
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
