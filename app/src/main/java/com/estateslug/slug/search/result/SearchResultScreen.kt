package com.estateslug.slug.search.result

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
import com.estateslug.slug.R
import com.estateslug.slug.home.BuildingFilterType
import com.estateslug.slug.home.AuctionStatusFilterType
import com.estateslug.slug.home.FilterOption
import com.estateslug.slug.home.HomeFilterBar
import com.estateslug.slug.home.Price
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.home.ProductList
import com.estateslug.slug.home.ProductListSkeleton
import com.estateslug.slug.home.ToggleFilterType
import com.estateslug.slug.home.component.FilterButtonState
import com.estateslug.slug.search.SearchViewModel
import com.estateslug.slug.search.bottomsheet.SearchBottomSheetType
import com.estateslug.slug.ui.component.ProductListEmpty
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.util.CursorPaginationState

@Composable
fun SearchResultRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    onShowBottomSheet: (SearchBottomSheetType) -> Unit,
) {
    val numberOfProduct by viewModel.numberOfProduct.collectAsStateWithLifecycle()
    val stateList by viewModel.stateList.collectAsStateWithLifecycle()
    val selectedSortingType by viewModel.selectedSortingType.collectAsStateWithLifecycle()
    val paginationState by viewModel.paginationState.collectAsStateWithLifecycle()

    SearchResultScreen(
        numberOfProduct = numberOfProduct,
        stateList = stateList,
        sortTypeName = stringResource(selectedSortingType.localizedText),
        paginationState = paginationState,
        onItemClick = { item ->
            onItemClick(item.id)
        },
        onLoadMore = { viewModel.loadMore() },
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
    paginationState: CursorPaginationState<ProductItemUiModel>,
    onItemClick: (ProductItemUiModel) -> Unit,
    onLoadMore: () -> Unit,
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
            isLoading = paginationState.isInitialLoading,
        )

        when {
            paginationState.isInitialLoading -> {
                ProductListSkeleton()
            }

            paginationState.isEmpty -> {
                ProductListEmpty(stringResource(R.string.home_product_list_empty_title))
            }

            else -> {
                ProductList(
                    uiModelList = paginationState.items,
                    onItemClicked = onItemClick,
                    onLoadMore = onLoadMore,
                    isLoadingMore = paginationState.isLoadingMore,
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
            paginationState = CursorPaginationState(items = ProductItemUiModel.testList),
            onItemClick = {},
            onLoadMore = {},
            onFilterClick = {},
            onSortingClick = {},
        )
    }
}
