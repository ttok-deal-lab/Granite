package com.estateslug.slug.mypage.recent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.R
import com.estateslug.slug.detail.navigation.RouteDetail
import com.estateslug.slug.detail.navigation.detailNavGraph
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.home.ProductList
import com.estateslug.slug.home.ProductListSkeleton
import com.estateslug.slug.main.Route
import com.estateslug.slug.ui.component.ProductListEmpty
import com.estateslug.slug.ui.component.topbar.ArrowTopBar
import com.estateslug.slug.ui.theme.SlugTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable


@Serializable
data object RouteRecentItems : Route

@AndroidEntryPoint
class RecentItemsActivity : ComponentActivity() {
    private val viewmodel: RecentItemsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
            SlugTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = RouteRecentItems,
                ) {
                    composable<RouteRecentItems> {
                        RecentItemsScreen(
                            uiState = uiState,
                            onBackClick = { finish() },
                            onItemClicked = { model ->
                                // 전환 중 연타 시 인자 교체로 잘못된 매물이 뜨는 것 방지
                                if (navController.currentBackStackEntry?.destination
                                        ?.hasRoute<RouteDetail>() != true
                                ) {
                                    navController.navigate(RouteDetail(model.id)) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                        )
                    }

                    detailNavGraph(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}

@Composable
fun RecentItemsScreen(
    onBackClick: () -> Unit,
    onItemClicked: (ProductItemUiModel) -> Unit,
    uiState: RecentItemsUiState
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            ArrowTopBar(
                text = "최근 본 매물",
                onBackClick = onBackClick
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val state = uiState.loadingState) {
                RecentItemsLoadingState.Loading -> {
                    ProductListSkeleton()
                }
                is RecentItemsLoadingState.Success -> {
                    if (state.items.isNotEmpty()) {
                        ProductList(uiModelList = state.items, onItemClicked = onItemClicked)
                    } else {
                        ProductListEmpty(stringResource(R.string.recent_product_list_empty_title))
                    }
                }

                is RecentItemsLoadingState.Error -> {
                    Text("error")
                }
            }
        }
    }
}
