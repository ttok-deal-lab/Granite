package com.warehouseinhand.slug.home

import androidx.lifecycle.ViewModel
import com.warehouseinhand.slug.home.bottomsheet.location.Location
import com.warehouseinhand.slug.home.bottomsheet.sorting.SortingType
import com.warehouseinhand.slug.home.component.FilterButtonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private val _selectedSortingType: MutableStateFlow<SortingType> =
        MutableStateFlow(SortingType.NEWEST)
    val selectedSortingType get() = _selectedSortingType.asStateFlow()

    private val _stateList: MutableStateFlow<List<FilterButtonState>> =
        MutableStateFlow(FilterButtonState.defaultStateList)
    val stateList get() = _stateList.asStateFlow()

    private val _productUiModelList: MutableStateFlow<List<ProductItemUiModel>> =
        MutableStateFlow(ProductItemUiModel.testList)
    val productUiModelList get() = _productUiModelList.asStateFlow()

    private val _numberOfProduct: MutableStateFlow<Long> = MutableStateFlow(12_00L)
    val numberOfProduct get() = _numberOfProduct.asStateFlow()

    private val _tempProductSize: MutableStateFlow<Long> = MutableStateFlow(1_200L)
    val tempProductSize get() = _tempProductSize.asStateFlow()

    private var isFinishedProductFilterSelected = false
    private var isVerifiedFilterSelected = false
    private val _buildingFilterSelectedList: MutableStateFlow<List<BuildingFilterType>> =
        MutableStateFlow(listOf())
    val buildingFilterSelectedList get() = _buildingFilterSelectedList.asStateFlow()

    private val _auctionStateFilterSelectedList: MutableStateFlow<List<AuctionStatusFilterType>> =
        MutableStateFlow(listOf())
    val auctionStateFilterSelectedList get() = _auctionStateFilterSelectedList.asStateFlow()

    private val _minPrice: MutableStateFlow<Long> = MutableStateFlow(0L)
    val minPrice get() = _minPrice.asStateFlow()
    private val _maxPrice: MutableStateFlow<Long> = MutableStateFlow(200_000_000L)
    val maxPrice get() = _maxPrice.asStateFlow()

    private val _priceRange: MutableStateFlow<Price> =
        MutableStateFlow(Price.Range(minPrice.value, maxPrice.value))
    val priceRange get() = _priceRange.asStateFlow()

    //HOME location

    private val _selectedMainLocation: MutableStateFlow<Location.LocationMain> =
        MutableStateFlow(Location.LocationMain.ALL)
    val selectedMainLocation get() = _selectedMainLocation.asStateFlow()

    private val _selectedSubLocation: MutableStateFlow<Location.LocationSub> =
        MutableStateFlow(Location.LocationSub.ALL.ALL)
    val selectedSubLocation get() = _selectedSubLocation.asStateFlow()

    //HOME location


    private fun updateFilterState() {// TODO 정리하기
        val buildingList = buildingFilterSelectedList.value
        val isBuildingListNotEmpty = !buildingList.isEmpty()

        val auctionStateList = auctionStateFilterSelectedList.value
        val isAuctionStateListNotEmpty = !auctionStateList.isEmpty()

        val priceRange = _priceRange.value
        val isRangeSelected =
            if (priceRange is Price.Range) priceRange.start != minPrice.value && priceRange.end != maxPrice.value else true

        _stateList.update {
            listOf(
                FilterButtonState(
                    isFilterSelected = isVerifiedFilterSelected,
                    filterOption = ToggleFilterType.VERIFIED,
                ),
                FilterButtonState(
                    isFilterSelected = isBuildingListNotEmpty,
                    filterOptions = if (isBuildingListNotEmpty) buildingList else listOf(
                        BuildingFilterType.APARTMENT
                    ),
                ),
                FilterButtonState(
                    isFilterSelected = isAuctionStateListNotEmpty,
                    filterOptions = if (isAuctionStateListNotEmpty) auctionStateList else listOf(
                        AuctionStatusFilterType.NEW
                    ),
                ),
                FilterButtonState(
                    isFilterSelected = isRangeSelected,
                    filterOption = _priceRange.value,
                ),
                FilterButtonState(
                    isFilterSelected = isFinishedProductFilterSelected,
                    filterOption = ToggleFilterType.FINISHED_PRODUCT,
                ),
            )
        }


    }

    private fun requestNewItemList() {
        //TODO : REQUEST API to new item List
        //debounce 필요!!
    }

    fun changeFinishedSelected() {
        isFinishedProductFilterSelected = !isFinishedProductFilterSelected
        updateFilterState()
        requestNewItemList()
    }

    fun changeAuctionFilterSelectList(list: List<AuctionStatusFilterType>) {
        //TODO : 더나은 sorting이 필요 할 것으로 예상.
        _auctionStateFilterSelectedList.update {
            AuctionStatusFilterType.entries.filter { list.contains(it) }
        }
        updateFilterState()
        requestNewItemList()
    }

    fun changeBuildingFilterSelectList(list: List<BuildingFilterType>) {
        //TODO : 더나은 sorting이 필요 할 것으로 예상.
        _buildingFilterSelectedList.update {
            BuildingFilterType.entries.filter { list.contains(it) }
        }
        updateFilterState()
        requestNewItemList()
    }

    fun changeVerifiedSelected() {
        isVerifiedFilterSelected = !isVerifiedFilterSelected
        updateFilterState()
        requestNewItemList()
    }

    fun changePriceRange(price: Price) {
        _priceRange.update { price }
        updateFilterState()
        requestNewItemList()
    }

    fun requestChangeSortingType(type: SortingType) {
        _selectedSortingType.value = type
        requestNewItemList()
    }

    //HOME location
    fun requestChangeLocationChanged(
        mainLocation: Location.LocationMain,
        subLocation: Location.LocationSub
    ) {
        _selectedMainLocation.update { mainLocation }
        _selectedSubLocation.update { subLocation }

        requestNewItemList()
        //TODO : filter초기화
        requestNewItemList()
    }
    //HOME location


}
