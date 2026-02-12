package com.warehouseinhand.slug.search.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.BuildingFilterType
import com.warehouseinhand.slug.home.AuctionStatusFilterType
import com.warehouseinhand.slug.home.FilterOption
import com.warehouseinhand.slug.home.HomeFilterBar
import com.warehouseinhand.slug.home.Price
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.home.ProductList
import com.warehouseinhand.slug.home.ProductListSkeleton
import com.warehouseinhand.slug.home.ToggleFilterType
import com.warehouseinhand.slug.home.component.FilterButtonState
import com.warehouseinhand.slug.search.SearchViewModel
import com.warehouseinhand.slug.search.bottomsheet.SearchBottomSheetType
import com.warehouseinhand.slug.ui.component.ProductListEmpty
import com.warehouseinhand.slug.ui.theme.NeutralInverted

@Composable
fun SearchResultRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    onShowBottomSheet: (SearchBottomSheetType) -> Unit,
) {
    val numberOfProduct by viewModel.numberOfProduct.collectAsStateWithLifecycle()
    val stateList by viewModel.stateList.collectAsStateWithLifecycle()
    val selectedSortingType by viewModel.selectedSortingType.collectAsStateWithLifecycle()
    val productList = viewModel.productList.collectAsLazyPagingItems()

    SearchResultScreen(
        numberOfProduct = numberOfProduct,
        stateList = stateList,
        sortTypeName = stringResource(selectedSortingType.localizedText),
        productList = productList,
        onItemClick = { item ->
            onItemClick(item.id)
        },
        onFilterClick = { filterOption ->
            when (filterOption) {
                ToggleFilterType.VERIFIED -> viewModel.changeVerifiedSelected()
                is BuildingFilterType -> onShowBottomSheet(SearchBottomSheetType.BuildingType)
                is AuctionStatusFilterType -> onShowBottomSheet(SearchBottomSheetType.AuctionState)
                is Price -> onShowBottomSheet(SearchBottomSheetType.PriceRange)
                ToggleFilterType.FINISHED_PRODUCT -> viewModel.changeFinishedSelected()

            }
        },
        onSortingClick = { onShowBottomSheet(SearchBottomSheetType.ListSorting) }
    )
}

@Composable
fun SearchResultScreen(
    numberOfProduct: Long,
    stateList: List<FilterButtonState>,
    sortTypeName: String,
    productList: LazyPagingItems<ProductItemUiModel>,
    onItemClick: (ProductItemUiModel) -> Unit,
    onFilterClick: (FilterOption) -> Unit,
    onSortingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralInverted)
    ) {
        HomeFilterBar(
            verifiedProductExist = numberOfProduct > 0,
            numberOfProduct = numberOfProduct,
            sortTypeName = sortTypeName,
            stateList = stateList,
            onSortingClick = onSortingClick,
            onFilterClick = onFilterClick,
            isLoading = productList.loadState.refresh is LoadState.Loading,
        )

        when {
            productList.loadState.refresh is LoadState.Loading -> {
                ProductListSkeleton()
            }

            productList.loadState.refresh is LoadState.NotLoading && productList.itemCount == 0 -> {
                ProductListEmpty(stringResource(R.string.home_product_list_empty_title))
            }

            else -> {
                ProductList(
                    uiModelList = productList,
                    onItemClicked = onItemClick
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchResultScreen() {
    Surface {
        SearchResultScreen(
            numberOfProduct = 13894,
            stateList = FilterButtonState.defaultStateList,
            sortTypeName = "최신 등록순",
            productList = ProductItemUiModel.pagingItems(),
            onItemClick = {},
            onFilterClick = {},
            onSortingClick = {},
        )
    }
}
