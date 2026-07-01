package com.estateslug.slug.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.home.AuctionStatusFilterType
import com.estateslug.slug.home.BuildingFilterType
import com.estateslug.slug.home.FilterOption
import com.estateslug.slug.home.HomeScreen
import com.estateslug.slug.home.HomeViewModel
import com.estateslug.slug.home.Price
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.home.ToggleFilterType
import com.estateslug.slug.home.bottomsheet.location.Location
import com.estateslug.slug.home.component.FilterButtonState
import com.estateslug.slug.main.MainBottomSheetType.HomeBottomSheetType
import com.estateslug.slug.main.MainViewModel
import com.estateslug.slug.util.startDetailActivity
import com.estateslug.slug.util.startSearchActivity


@Composable
internal fun HomeRoute(
    padding: PaddingValues,
    homeViewModel: HomeViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner),
    mainViewModel: MainViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner)
) {
    val lastSelectedSortType by homeViewModel.selectedSortingType.collectAsStateWithLifecycle()

    val numberOfProduct: Long by homeViewModel.numberOfProduct.collectAsStateWithLifecycle()

    val mainLocation: Location.LocationMain by homeViewModel.selectedMainLocation.collectAsStateWithLifecycle()
    val subLocation: Location.LocationSub by homeViewModel.selectedSubLocation.collectAsStateWithLifecycle()

    val paginationState by homeViewModel.paginationState.collectAsStateWithLifecycle()
    val stateList: List<FilterButtonState> by homeViewModel.stateList.collectAsStateWithLifecycle()

    val sectionName by
    remember { derivedStateOf { "${mainLocation.getShortLocationString()} ${subLocation.getLocationString()}" } }
    val onLocationSelectClick: () -> Unit = {
        mainViewModel.requestToShowBottomSheet(HomeBottomSheetType.LocationSelect)
    }
    val context = LocalContext.current
    val onSearchClick: () -> Unit = { startSearchActivity(context) }
    val onNotificationClick: () -> Unit = {}
    val verifiedProductExist: Boolean = true //TODO : 처리해야함!

    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = { model ->
        //TODO :API연결 이후 재확인
        // 최근 본 목록 추가는 상세 화면 진입(데이터 로드 성공) 시점에서 처리 (딥링크 진입도 커버)
        startDetailActivity(currentContext, model.id)

//        Toast.makeText(currentContext, model.nameOfProduct, Toast.LENGTH_SHORT).show()
    }

    //TODO : viewmodel에서 처리하게 하는건?
    val onFilterClick: (FilterOption) -> Unit = { option ->
        when (option) {
            ToggleFilterType.FINISHED_PRODUCT -> {
                homeViewModel.changeFinishedSelected()
            }

            ToggleFilterType.VERIFIED -> {
                homeViewModel.changeVerifiedSelected()
            }

            is BuildingFilterType -> mainViewModel.requestToShowBottomSheet(HomeBottomSheetType.BuildingType)

            is AuctionStatusFilterType -> mainViewModel.requestToShowBottomSheet(HomeBottomSheetType.AuctionState)

            is Price -> mainViewModel.requestToShowBottomSheet(HomeBottomSheetType.PriceRange)
        }
    }
    val onSortingClick: () -> Unit =
        { mainViewModel.requestToShowBottomSheet(HomeBottomSheetType.ListSorting) }
    HomeScreen(
        padding = padding,
        lastSelectedSortType = lastSelectedSortType,
        sectionName = sectionName,
        onSearchClick = onSearchClick,
        verifiedProductExist = verifiedProductExist,
        numberOfProduct = numberOfProduct,
        stateList = stateList,
        paginationState = paginationState,
        onLocationSelectClick = onLocationSelectClick,
        onNotificationClick = onNotificationClick,
        onSortingClick = onSortingClick,
        onFilterClick = onFilterClick,
        onItemClicked = onItemClicked,
        onLoadMore = { homeViewModel.loadMore() },
    )
}
