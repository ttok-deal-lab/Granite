package com.warehouseinhand.slug.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.data.network.search.RemoteSearchRepository
import com.warehouseinhand.slug.domain.search.AuctionSearchItem
import com.warehouseinhand.slug.home.bottomsheet.location.Location
import com.warehouseinhand.slug.home.bottomsheet.sorting.SortingType
import com.warehouseinhand.slug.home.component.FilterButtonState
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import com.warehouseinhand.slug.ui.component.label.SlugLabelUiModel
import com.warehouseinhand.slug.util.calculateDaysLeft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteSearchRepository: RemoteSearchRepository
) : ViewModel() {
    init {
        //2
        requestNewItemList()
    }

    private val _selectedSortingType: MutableStateFlow<SortingType> =
        MutableStateFlow(SortingType.NEWEST)
    val selectedSortingType = _selectedSortingType.asStateFlow()

    private val _stateList: MutableStateFlow<List<FilterButtonState>> =
        MutableStateFlow(FilterButtonState.defaultStateList)
    val stateList get() = _stateList.asStateFlow()

    val productUiModelList: Flow<PagingData<ProductItemUiModel>> =
        remoteSearchRepository.getProductListPaging(
            onSizeReturn = { totalCount:Long ->
                viewModelScope.launch {
                    _numberOfProduct.emit(totalCount)
                }
            }
        )
            .map { paging ->
                paging.map { domain ->
                    domain.toUiModel()
                }
            }
            .cachedIn(viewModelScope)

    //1
//    private val _productUiModelList: MutableStateFlow<List<ProductItemUiModel>> =
//        MutableStateFlow(emptyList())
//    val productUiModelList: StateFlow<List<ProductItemUiModel>> =
//        _productUiModelList
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = emptyList()
//            )
    //2
//    val productUiModelList: StateFlow<List<ProductItemUiModel>> =
//        _productUiModelList
//            .onStart { requestNewItemList() }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = emptyList()
//            )

    private val _numberOfProduct: MutableStateFlow<Long> = MutableStateFlow(0L)
    val numberOfProduct = _numberOfProduct.asStateFlow()

    private val _tempProductSize: MutableStateFlow<Long> = MutableStateFlow(0L)
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

//    private var lastestCursor: String? = ""

    private var lastJob: Job? = Job().apply { complete() }
    private fun requestNewItemList() {
        //debounce 어떻게? 목록 그냥 요청은 계속 받아야하나, 필터변경시에는 force 처리해야하는데?
        //TODO : REQUEST API to new item List
        //TODO : viewmodel에서 커서를 가지는게 맞나?
        lastJob?.cancel()
        lastJob = viewModelScope.launch {
            remoteSearchRepository.getProductListByCursor(nextCursor = "")
                .onSuccess { it ->
//                    lastestCursor = it.nextCursor

//                    _productUiModelList.emit(
//                        it.items.map { it.toUiModel() }
//                    )
//                    _numberOfProduct.emit(it.totalCount)
                    Log.d("TESTTEST", "requestNewItemList: SUCCESS")
                }.onFailure {
                    Log.d("TESTTEST", "requestNewItemList: FAIL $it")

                    //TODO
                }
        }
    }

    fun requestNextItemList() {
        //debounce 어떻게? 목록 그냥 요청은 계속 받아야하나, 필터변경시에는 force 처리해야하는데?
        //TODO : REQUEST API to new item List
        //TODO : viewmodel에서 커서를 가지는게 맞나?
//        if (lastJob?.isCompleted?.not() ?: true) {
//            return
//        }
//        lastJob = viewModelScope.launch {
//            remoteSearchRepository.getProductListByCursor(nextCursor = lastestCursor)
//                .onSuccess { it ->
//                    lastestCursor = it.nextCursor
//                    val newList = it.items.map { it.toUiModel() }
////                    _productUiModelList.update { lastList -> lastList + newList }
//                    _numberOfProduct.emit(it.totalCount)
//                    Log.d("TESTTEST", "requestNewItemList: SUCCESS")
//                }.onFailure {
//                    Log.d("TESTTEST", "requestNewItemList: FAIL $it")
//
//                    //TODO
//                }
//        }
    }

    private fun AuctionSearchItem.toUiModel(
        nowMillis: Long = System.currentTimeMillis()
    ): ProductItemUiModel {

        val daysLeft = calculateDaysLeft(salesDateTime, nowMillis)

        return ProductItemUiModel(
            id = id,
            priceOfProduct = appraisalPrice,
            nameOfProduct = buildingName ?: caseNumber, // fallback
            location = address,
            daysLeft = daysLeft,
            buildingImage =
                if (salesPicture.isNotEmpty())
                    ImageResource.Url(salesPicture)
                else ImageResource.Id(R.drawable.logo_metaopo),
            isFavorite = isFavorite,
            favoritePersons = zzimCount,
            infoChipList = buildSearchInfoChips()
        )
    }

    private fun AuctionSearchItem.buildSearchInfoChips()
            : List<SlugLabelUiModel> {

        val chips = mutableListOf<SlugLabelUiModel>()

        // 1. 인증 매물
        if (verified) {
            chips += SlugLabelUiModel(SlugLabelStyle.GradientBackground.Verified , "인증매물")
        }

        // 2. 매각 상태
        if (soldOut) {
            chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State , "매각완료")
        } else if (failBidCount > 0) {
            chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State , "유찰 ${failBidCount}회")
        }

        // 3. 건물 카테고리 (대표 1개)
        salesCategories.firstOrNull()?.let { category ->
            chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.Apartment , category)
            // 실제로는 category → 스타일 매핑 함수로 분리 권장
        }

        return chips
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

//        requestNewItemList()
        //TODO : filter초기화
        requestNewItemList()
    }
    //HOME location


}
