package com.warehouseinhand.slug.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.warehouseinhand.slug.home.AuctionStatusFilterType
import com.warehouseinhand.slug.home.BuildingFilterType
import com.warehouseinhand.slug.home.FilterOption
import com.warehouseinhand.slug.home.HomeScreen
import com.warehouseinhand.slug.home.HomeViewModel
import com.warehouseinhand.slug.home.Price
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.home.ToggleFilterType
import com.warehouseinhand.slug.home.bottomsheet.location.Location
import com.warehouseinhand.slug.home.component.FilterButtonState
import com.warehouseinhand.slug.main.MainBottomSheetType.HomeBottomSheetType
import com.warehouseinhand.slug.main.MainViewModel
import com.warehouseinhand.slug.util.startDetailActivity


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

    val productUiModelList = homeViewModel.productUiModelList.collectAsLazyPagingItems()
    val stateList: List<FilterButtonState> by homeViewModel.stateList.collectAsStateWithLifecycle()

    val sectionName by
    remember { derivedStateOf { "${mainLocation.getShortLocationString()} ${subLocation.getLocationString()}" } }
    val onLocationSelectClick: () -> Unit = {
        mainViewModel.requestToShowBottomSheet(HomeBottomSheetType.LocationSelect)
    }
    val onSearchClick: () -> Unit = {}
    val onNotificationClick: () -> Unit = {}
    val verifiedProductExist: Boolean = true

    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = { model ->

        //TODO :API연결 이후 재확인
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
        productUiModelList = productUiModelList,
        onLocationSelectClick = onLocationSelectClick,
        onNotificationClick = onNotificationClick,
        onSortingClick = onSortingClick,
        onFilterClick = onFilterClick,
        onItemClicked = onItemClicked,
    )
}
