package com.estateslug.slug.home.bottomsheet.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.R
import com.estateslug.slug.home.BuildingFilterType
import com.estateslug.slug.home.HomeViewModel


@Composable
fun BuildingFilterContent(
    requestHideBottomSheet: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val filterName = stringResource(R.string.filter_header_building)
    val selectedOptions: List<BuildingFilterType> by homeViewModel.buildingFilterSelectedList.collectAsStateWithLifecycle() //from viewModel
    val options = BuildingFilterType.entries
    val valueOfItem by homeViewModel.tempProductSize.collectAsStateWithLifecycle()
    val isLoading by homeViewModel.isTempCountLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { homeViewModel.fetchTempCountFromTotal() }

    FilterContent(
        filterName = filterName,
        selectedOptions = selectedOptions,
        options = options,
        buttonText = "${valueOfItem}개 매물보기",
        onConfirmClicked = { list ->
            homeViewModel.changeBuildingFilterSelectList(list)
            requestHideBottomSheet()
        },
        onSelectionChanged = { homeViewModel.updateTempBuildingFilter(it) },
        isLoading = isLoading,
    )
}


@Preview
@Composable
fun PreviewBuildingFilterContent() {
    BuildingFilterContent({})
}
