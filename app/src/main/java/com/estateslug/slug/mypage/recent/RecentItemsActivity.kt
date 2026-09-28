package com.estateslug.slug.mypage.recent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.estateslug.slug.R
import com.estateslug.slug.detail.navigation.ProductListDetailPaneScaffold
import com.estateslug.slug.detail.navigation.detailNavGraph
import com.estateslug.slug.detail.navigation.rememberProductPaneState
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.home.ProductList
import com.estateslug.slug.home.ProductListSkeleton
import com.estateslug.slug.main.Route
import com.estateslug.slug.main.rememberInnerCameraSafePadding
import com.estateslug.slug.ui.component.ProductListEmpty
import com.estateslug.slug.ui.component.topbar.ArrowTopBar
import com.estateslug.slug.ui.theme.SlugTheme
import androidx.activity.enableEdgeToEdge
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
                // 펼친 화면에서는 목록 | 상세 2-pane — 메인·검색과 같은 상태 홀더
                val paneState = rememberProductPaneState(navController)

                ProductListDetailPaneScaffold(
                    state = paneState,
                    modifier = Modifier
                        .fillMaxSize()
                        // 시스템 바 인셋을 여기서 먼저 소비한다 — 목록·상세 화면의 systemBarsPadding은
                        // 0이 되어 결과는 같고, 아래 바탕색이 시스템 바 뒤(창 배경)까지 번지지 않는다
                        .systemBarsPadding()
                        // 펼친 폴더블을 가로로 들면 펀치홀이 측면으로 온다 — MainScreen과 같은 처리
                        .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
                        .padding(rememberInnerCameraSafePadding())
                        // 두 페인 사이 여백도 화면 바탕으로 칠한다
                        .background(SlugTheme.colors.neutralInverted),
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = RouteRecentItems,
                    ) {
                        composable<RouteRecentItems> {
                            RecentItemsScreen(
                                uiState = uiState,
                                onBackClick = { finish() },
                                // 2-pane이면 상세 페인, 아니면 RouteDetail push (연타 방지 포함)
                                onItemClicked = { model -> paneState.openProduct(model.id) },
                            )
                        }

                        detailNavGraph(onBack = { navController.popBackStack() })
                    }
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
