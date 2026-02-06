package com.warehouseinhand.slug.search.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.AuctionStatusFilterType
import com.warehouseinhand.slug.home.BuildingFilterType
import com.warehouseinhand.slug.home.Price
import com.warehouseinhand.slug.home.bottomsheet.filter.FilterContent
import com.warehouseinhand.slug.home.bottomsheet.filter.PriceRangeFilter
import com.warehouseinhand.slug.home.bottomsheet.sorting.SortingTypeItemList
import com.warehouseinhand.slug.search.SearchViewModel

sealed interface SearchBottomSheetType {
    object ListSorting : SearchBottomSheetType
    object BuildingType : SearchBottomSheetType
    object AuctionState : SearchBottomSheetType
    object PriceRange : SearchBottomSheetType
}

@Composable
fun SearchBottomSheetContent(
    searchViewModel: SearchViewModel,
    bottomSheetType: SearchBottomSheetType,
    requestHideBottomSheet: () -> Unit,
) {

    when (bottomSheetType) {
        SearchBottomSheetType.ListSorting -> {
            val lastSelected by searchViewModel.selectedSortingType.collectAsStateWithLifecycle()
            SortingTypeItemList(
                lastSelected = lastSelected,
                onClick = { type ->
                    searchViewModel.requestChangeSortingType(type)
                    requestHideBottomSheet()
                }
            )
        }

        SearchBottomSheetType.BuildingType -> {
            val selectedOptions by searchViewModel.buildingFilterSelectedList.collectAsStateWithLifecycle()
            val valueOfItem by searchViewModel.tempProductSize.collectAsStateWithLifecycle()
            FilterContent(
                filterName = stringResource(R.string.filter_header_building),
                selectedOptions = selectedOptions,
                options = BuildingFilterType.entries,
                buttonText = "${valueOfItem}개 매물보기",
                onConfirmClicked = { list ->
                    searchViewModel.changeBuildingFilterSelectList(list)
                    requestHideBottomSheet()
                },
            )
        }

        SearchBottomSheetType.AuctionState -> {
            val selectedOptions by searchViewModel.auctionStateFilterSelectedList.collectAsStateWithLifecycle()
            val valueOfItem by searchViewModel.tempProductSize.collectAsStateWithLifecycle()
            FilterContent(
                filterName = stringResource(R.string.filter_header_auction_status),
                selectedOptions = selectedOptions,
                options = AuctionStatusFilterType.entries,
                buttonText = "${valueOfItem}개 매물보기",
                onConfirmClicked = { list ->
                    searchViewModel.changeAuctionFilterSelectList(list)
                    requestHideBottomSheet()
                },
            )
        }

        SearchBottomSheetType.PriceRange -> {
            val valueOfItem by searchViewModel.tempProductSize.collectAsStateWithLifecycle()
            val minValue by searchViewModel.minPrice.collectAsStateWithLifecycle()
            val maxValue by searchViewModel.maxPrice.collectAsStateWithLifecycle()
            val lastPrice by searchViewModel.priceRange.collectAsStateWithLifecycle()
            var price: Price by remember { mutableStateOf(lastPrice) }

            fun onRangeChanged(value: Pair<Long, Long>) {
                val result = when {
                    value.first == minValue && value.second != maxValue ->
                        Price.Below(value.second)

                    value.second == maxValue && value.first != minValue ->
                        Price.Above(value.first)

                    else ->
                        Price.Range(value.first, value.second)
                }
                price = result
            }

            PriceRangeFilter(
                valueOfItem = valueOfItem,
                minValue = minValue,
                maxValue = maxValue,
                price = price,
                onRangeChanged = ::onRangeChanged,
                onConfirmClicked = {
                    searchViewModel.changePriceRange(price)
                    requestHideBottomSheet()
                }
            )
        }
    }
}
