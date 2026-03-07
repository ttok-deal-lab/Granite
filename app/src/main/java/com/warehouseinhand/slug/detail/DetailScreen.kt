package com.warehouseinhand.slug.detail

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.warehouseinhand.slug.detail.subpage.TitleAnalysisPage
import com.warehouseinhand.slug.detail.subpage.auction.AuctionInfoPage
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.SlugTheme
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: CourtSaleDetailUiState,
    onBackButtonClicked: () -> Unit,
    likeClicked: () -> Unit
) {

    var selectedRoute: DetailedRoute by remember { mutableStateOf(DetailedRoute.AuctionInfo) }

    val navController = rememberNavController()
//    val buildingInfoUiModel: BuildingInfoUiModel = BuildingInfoUiModel.preview

    val startDestination: DetailedRoute = DetailedRoute.AuctionInfo

    var selectedBottomSheetType: DetailBottomSheetType by remember {
        mutableStateOf(DetailBottomSheetType.EMPTY)
    }

    val showBottomSheet: (type: DetailBottomSheetType) -> Unit = { type ->
        selectedBottomSheetType = type
    }

    val onShareClicked: () -> Unit = {
        selectedBottomSheetType = DetailBottomSheetType.Share
    }


    var userScrollEnabled by remember { mutableStateOf(true) }
    val onMapFocused: (focused: Boolean) -> Unit = {
        userScrollEnabled = !it
    }

    Scaffold(modifier = Modifier.systemBarsPadding(), topBar = {
        DetailTopBar(
            onBackButtonClicked,
            onShareClicked,
            uiState.detailSimpleInformation.topTitle,
            isLoading = uiState.isLoading
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            Crossfade(
                targetState = uiState.isLoading,
                label = "detailLoadingCrossfade"
            ) { isLoading ->
                if (isLoading) {
                    DetailScreenSkeleton()
                } else {
                    Column {
                        LazyColumn(
                            modifier = Modifier
                                .background(NeutralInverted),
                            userScrollEnabled = userScrollEnabled
                        ) {
                            item {
                                DetailSimpleInformation(
                                    uiModel = uiState.detailSimpleInformation,
                                    likeClicked = likeClicked
                                )
                                HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
                            }
                            stickyHeader {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = NeutralInverted)
                                ) {
                                    DetailedTabRow(
                                        selectedRoute = selectedRoute,
                                        onTabClicked = {
                                            if (selectedRoute != it) {
                                                selectedRoute = it
                                                navController.navigate(it)
                                            }
                                        }
                                    )
                                }
                            }
                            item {
                                Box(
                                    modifier = Modifier
                                        .animateContentSize()
                                        .fillMaxWidth()
                                ) {
                                    NavHost(
                                        navController = navController,
                                        startDestination = startDestination,
                                    ) {
                                        composable<DetailedRoute.AuctionInfo> {
                                            AuctionInfoPage(
                                                uiModel = uiState.auctionInfo,
                                                requestBottomSheet = showBottomSheet,
                                                onMapFocused = onMapFocused
                                            )
                                        }
                                        composable<DetailedRoute.TitleAnalysis> {
                                            TitleAnalysisPage(uiState.listOfLessees)
                                        }
//                        composable<DetailedRoute.BuildingInfo> {
//                            BuildingInfoPage(
//                                data = buildingInfoUiModel,
//                                onMapFocused = onMapFocused
//                            )
//                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            DetailedBottomSheet(
                detailBottomSheetType = selectedBottomSheetType,
                onDismiss = { selectedBottomSheetType = DetailBottomSheetType.EMPTY }
            )
        }
    }


}

sealed class DetailedRoute {
    abstract val period: Int
    abstract val name: String


    @Serializable
    data object TitleAnalysis : DetailedRoute() {
        override val period: Int = 1
        override val name: String = "권리분석"

    }

    @Serializable
    data object BuildingInfo : DetailedRoute() {
        override val period: Int = 2
        override val name: String = "건물정보"
    }

    @Serializable
    data object AuctionInfo : DetailedRoute() {
        override val period: Int = 0
        override val name: String = "경매정보"
    }

    companion object {
        val entries: List<DetailedRoute> by lazy {
            listOf(
                AuctionInfo,
                TitleAnalysis,
//                BuildingInfo // MVP 미적용!
            )
        }
    }
}

@Preview(heightDp = 2000)
@Composable
private fun PreviewDetailPage() {

    SlugTheme {
        DetailScreen(
            uiState = CourtSaleDetailUiState.preview.copy(isLoading = false),
            onBackButtonClicked = {},
            likeClicked = {},
        )
    }
}