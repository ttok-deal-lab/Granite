package com.warehouseinhand.slug.home.bottomsheet

import androidx.compose.runtime.Composable
import com.warehouseinhand.slug.home.bottomsheet.filter.AuctionStateFilterContent
import com.warehouseinhand.slug.home.bottomsheet.filter.BuildingFilterContent
import com.warehouseinhand.slug.home.bottomsheet.filter.PriceRangeFilterContent
import com.warehouseinhand.slug.home.bottomsheet.location.LocationSelectorContent
import com.warehouseinhand.slug.home.bottomsheet.sorting.SortBottomSheetContent
import com.warehouseinhand.slug.main.MainBottomSheetType.HomeBottomSheetType

@Composable
fun HomeBottomSheetContent(bottomSheetType: HomeBottomSheetType, requestHideBottomSheet: () -> Unit) {
    when(bottomSheetType){
        HomeBottomSheetType.LocationSelect -> {
            LocationSelectorContent(requestHideBottomSheet = requestHideBottomSheet)
        }

        HomeBottomSheetType.ListSorting -> {
            SortBottomSheetContent(requestHideBottomSheet = requestHideBottomSheet)
        }

        HomeBottomSheetType.BuildingType -> {
            BuildingFilterContent(requestHideBottomSheet = requestHideBottomSheet)
        }

        HomeBottomSheetType.AuctionState -> {
            AuctionStateFilterContent(requestHideBottomSheet = requestHideBottomSheet)
        }

        HomeBottomSheetType.PriceRange -> {
            PriceRangeFilterContent(requestHideBottomSheet = requestHideBottomSheet)
        }
    }
}