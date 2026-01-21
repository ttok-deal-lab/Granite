package com.warehouseinhand.slug.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.bottomsheet.sorting.SortingType
import com.warehouseinhand.slug.home.component.FilterButtonState
import com.warehouseinhand.slug.ui.component.EmptyScreen


@Composable
fun HomeScreen(
    padding: PaddingValues, //TODO : 더 상위에서 처리할것
    lastSelectedSortType: SortingType,
    sectionName: String,
    productUiModelList: LazyPagingItems<ProductItemUiModel>,
    stateList: List<FilterButtonState>,
    numberOfProduct: Long,
    verifiedProductExist: Boolean,
    onItemClicked: (ProductItemUiModel) -> Unit,
    onSortingClick: () -> Unit,
    onFilterClick: (FilterOption) -> Unit,
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
    onLocationSelectClick: () -> Unit
) {
    val isListEmpty = productUiModelList.itemCount == 0
    Column(modifier = Modifier.padding(padding)) {
        HomeTopBar(
            sectionName = sectionName,
            onLocationSelectClick = onLocationSelectClick,
            onSearchClick = onSearchClick,
            onNotificationClick = onNotificationClick,
        )
        HomeFilterBar(
            verifiedProductExist = verifiedProductExist,
            numberOfProduct = numberOfProduct,
            sortTypeName = stringResource(lastSelectedSortType.localizedText),
            stateList = stateList,
            onSortingClick = onSortingClick,
            onFilterClick = onFilterClick,
        )
        if (isListEmpty) {
            EmptyScreen(stringResource(R.string.home_product_list_empty_title))
        } else
            HomeProductList(productUiModelList, onItemClicked)
    }
}

@Preview(device = PIXEL_7_PRO)
@Composable
fun PreviewHomeScreen() {
    val sectionName = "서울 관악구"
    val onSectionSelectClick: () -> Unit = {}
    val onSearchClick: () -> Unit = {}
    val onNotificationClick: () -> Unit = {}
    val onFilterClick: (FilterOption) -> Unit = {}
    val verifiedProductExist: Boolean = true
    val numberOfProduct: Long = 123123

    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = { model ->
        Toast.makeText(currentContext, model.nameOfProduct, Toast.LENGTH_SHORT).show()
    }
    val stateList: List<FilterButtonState> = listOf(
        FilterButtonState(
            isFilterSelected = true,
            filterOption = ToggleFilterType.VERIFIED,
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = BuildingFilterType.APARTMENT,
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = AuctionStatusFilterType.NEW,
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = Price.Range(820_000_000, 820_000_000),
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = ToggleFilterType.FINISHED_PRODUCT,
        ),
    )
    val productUiModelList = ProductItemUiModel.pagingItems()

    HomeScreen(
        padding = PaddingValues(),
        onSortingClick = {},
        lastSelectedSortType = SortingType.NEWEST,
        sectionName = sectionName,
        onLocationSelectClick = onSectionSelectClick,
        onSearchClick = onSearchClick,
        onNotificationClick = onNotificationClick,
        verifiedProductExist = verifiedProductExist,
        numberOfProduct = numberOfProduct,
        onItemClicked = onItemClicked,
        stateList = stateList,
        productUiModelList = productUiModelList,
        onFilterClick = onFilterClick
    )
}