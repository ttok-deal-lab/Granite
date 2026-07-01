package com.estateslug.slug.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetContent
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetType
import com.estateslug.slug.search.component.SearchTopBar
import com.estateslug.slug.search.navigation.RouteSearchBridge
import com.estateslug.slug.search.navigation.RouteSearchResult
import com.estateslug.slug.search.navigation.searchNavGraph
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.util.startDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val searchViewModel: SearchViewModel = hiltViewModel()
            SlugTheme {
                val navController = rememberNavController()

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

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .systemBarsPadding(),
                    topBar = {
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
                                    startDetailActivity(this@SearchActivity, itemId)
                                },
                                onShowBottomSheet = { type ->
                                    bottomSheetType = type
                                    isBottomSheetShowing = true
                                }
                            )
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
