package com.warehouseinhand.slug.home.bottomsheet.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.AuctionStatusFilterType
import com.warehouseinhand.slug.home.HomeViewModel


@Composable
fun AuctionStateFilterContent(
    requestHideBottomSheet: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val filterName = stringResource(R.string.filter_header_auction_status)
    val selectedOptions: List<AuctionStatusFilterType> by homeViewModel.auctionStateFilterSelectedList.collectAsStateWithLifecycle() //from viewModel

    val options = AuctionStatusFilterType.entries
    val valueOfItem by homeViewModel.tempProductSize.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { homeViewModel.fetchTempCountFromTotal() }

    FilterContent(
        filterName = filterName,
        selectedOptions = selectedOptions,
        options = options,
        buttonText = "${valueOfItem}개 매물보기",
        onConfirmClicked = { list ->
            homeViewModel.changeAuctionFilterSelectList(list)
            requestHideBottomSheet()
        },
        onSelectionChanged = { homeViewModel.updateTempAuctionFilter(it) },
    )
}


@Preview
@Composable
fun PreviewAuctionStateFilterContent() {
    AuctionStateFilterContent({})
}
