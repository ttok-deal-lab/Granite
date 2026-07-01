package com.estateslug.slug.home.bottomsheet

import androidx.compose.runtime.Composable
import com.estateslug.slug.home.bottomsheet.filter.AuctionStateFilterContent
import com.estateslug.slug.home.bottomsheet.filter.BuildingFilterContent
import com.estateslug.slug.home.bottomsheet.filter.PriceRangeFilterContent
import com.estateslug.slug.home.bottomsheet.location.LocationSelectorContent
import com.estateslug.slug.home.bottomsheet.sorting.SortBottomSheetContent
import com.estateslug.slug.main.MainBottomSheetType.HomeBottomSheetType

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