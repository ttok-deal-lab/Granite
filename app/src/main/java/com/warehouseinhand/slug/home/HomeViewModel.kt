package com.warehouseinhand.slug.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.data.local.recent.RecentItemRepository
import com.warehouseinhand.slug.data.network.search.RemoteSearchRepository
import com.warehouseinhand.slug.data.network.search.SoldOutStatus
import com.warehouseinhand.slug.data.network.search.Sort
import com.warehouseinhand.slug.data.network.search.VerificationStatus
import com.warehouseinhand.slug.home.AuctionStatusFilterType.Companion.toAuctionFailCount
import com.warehouseinhand.slug.home.BuildingFilterType.Companion.toBuildType
import com.warehouseinhand.slug.home.ProductItemUiModel.Companion.toUiModel
import com.warehouseinhand.slug.home.bottomsheet.location.Location
import com.warehouseinhand.slug.home.bottomsheet.location.Location.Companion.toRegion
import com.warehouseinhand.slug.home.bottomsheet.sorting.SortingType
import com.warehouseinhand.slug.home.component.FilterButtonState
import com.warehouseinhand.slug.util.CursorPaginationState
import com.warehouseinhand.slug.util.CursorPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteSearchRepository: RemoteSearchRepository,
    private val recentItemRepository: RecentItemRepository,
) : ViewModel() {

    private val _queryState = MutableStateFlow(SearchQuery())
//    val queryState = _queryState.asStateFlow()

    private val _selectedSortingType: MutableStateFlow<SortingType> =
        MutableStateFlow(SortingType.NEWEST)
    val selectedSortingType = _selectedSortingType.asStateFlow()

    private val _stateList: MutableStateFlow<List<FilterButtonState>> =
        MutableStateFlow(FilterButtonState.defaultStateList)
    val stateList get() = _stateList.asStateFlow()
    private val _paginationState = MutableStateFlow(CursorPaginationState<ProductItemUiModel>())
    val paginationState: StateFlow<CursorPaginationState<ProductItemUiModel>> =
        _paginationState.asStateFlow()

    private val paginator = CursorPaginator(
        state = _paginationState,
        fetchPage = { cursor ->
            val query = _queryState.value
            val page = remoteSearchRepository.getProductListByCursorWithFavorites(cursor, query)
            com.warehouseinhand.slug.util.CursorPage(
                items = page.items.map { it.toUiModel() },
                nextCursor = page.nextCursor,
                totalCount = page.totalCount,
            )
        }
    )

    private var _tempCountJob: Job? = null

    init {
        viewModelScope.launch {
            _queryState
                .debounce(250)
                .distinctUntilChanged()
                .collectLatest {
                    _numberOfProduct.emit(0L)
                    paginator.loadInitial()
                    val totalCount = _paginationState.value.totalCount
                    _numberOfProduct.emit(totalCount)
                    _tempProductSize.emit(totalCount)
                }
        }
    }

    fun loadMore() {
        viewModelScope.launch { paginator.loadMore() }
    }

    private val _numberOfProduct: MutableStateFlow<Long> = MutableStateFlow(0L)
    val numberOfProduct = _numberOfProduct.asStateFlow()

    private val _tempProductSize: MutableStateFlow<Long> = MutableStateFlow(0L)
    val tempProductSize get() = _tempProductSize.asStateFlow()

    private val _isTempCountLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTempCountLoading: StateFlow<Boolean> = _isTempCountLoading.asStateFlow()


    private var isFinishedProductFilterSelected = false
    private var isVerifiedFilterSelected = false
    private val _buildingFilterSelectedList: MutableStateFlow<List<BuildingFilterType>> =
        MutableStateFlow(listOf())
    val buildingFilterSelectedList get() = _buildingFilterSelectedList.asStateFlow()

    private val _auctionStateFilterSelectedList: MutableStateFlow<List<AuctionStatusFilterType>> =
        MutableStateFlow(listOf())
    val auctionStateFilterSelectedList get() = _auctionStateFilterSelectedList.asStateFlow()

    private val MIN_PRICE = 0L
    private val MAX_PRICE = 200_000_000L

    private val _minPrice: MutableStateFlow<Long> = MutableStateFlow(MIN_PRICE)
    val minPrice get() = _minPrice.asStateFlow()
    private val _maxPrice: MutableStateFlow<Long> = MutableStateFlow(MAX_PRICE)
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

    fun addRecentItem(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            recentItemRepository.addRecentItem(id)
        }
    }

    private fun refreshPagingByCurrentFilters() {
        _queryState.value = buildQueryFromCurrentStates()
    }

    private fun buildQueryFromCurrentStates(): SearchQuery {
        val verificationStatus =
            if (isVerifiedFilterSelected) VerificationStatus.VERIFIED else VerificationStatus.ALL

        val buildType =
            buildingFilterSelectedList.value.toBuildType()

        val auctionFailCount = auctionStateFilterSelectedList.value.toAuctionFailCount()

        val (minPrice, maxPrice) = when (val pr = priceRange.value) {
            is Price.Range -> {
                if (pr.start == MIN_PRICE && pr.end == MAX_PRICE)
                    -1L to -1L
                else
                    pr.start to pr.end
            }

            is Price.Above -> pr.value to -1L
            is Price.Below -> -1L to pr.value
        }

        val sort = selectedSortingType.value.toSort()
        val soldOutStatus = if (isFinishedProductFilterSelected) SoldOutStatus.SOLD_OUT else SoldOutStatus.ALL

        // 지역 예시 (네 Location enum에 맞춰서 변환 함수 만들면 됨)
        val region = selectedMainLocation.value.toRegion()
        val district: String = selectedSubLocation.value.toDistrict()

        return SearchQuery(
            keyword = "unknown",//TODO : 검색에서는 처리 되어야함!
            region = region,
            district = district,
            buildType = buildType,
            auctionFailCount = auctionFailCount,
            verificationStatus = verificationStatus,
            soldOutStatus = soldOutStatus,
            minimumPrice = minPrice,
            maximumPrice = maxPrice,
            sort = sort
        )
    }

    fun changeFinishedSelected() {
        isFinishedProductFilterSelected = !isFinishedProductFilterSelected
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun changeAuctionFilterSelectList(list: List<AuctionStatusFilterType>) {
        //TODO : 더나은 sorting이 필요 할 것으로 예상.
        _auctionStateFilterSelectedList.update {
            AuctionStatusFilterType.entries.filter { list.contains(it) }
        }
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun changeBuildingFilterSelectList(list: List<BuildingFilterType>) {
        //TODO : 더나은 sorting이 필요 할 것으로 예상.
        _buildingFilterSelectedList.update {
            BuildingFilterType.entries.filter { list.contains(it) }
        }
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun changeVerifiedSelected() {
        isVerifiedFilterSelected = !isVerifiedFilterSelected
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun changePriceRange(price: Price) {
        _priceRange.update { price }
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun requestChangeSortingType(type: SortingType) {
        _selectedSortingType.value = type
        refreshPagingByCurrentFilters()
    }

    //HOME location
    fun requestChangeLocationChanged(
        mainLocation: Location.LocationMain,
        subLocation: Location.LocationSub
    ) {
        _selectedMainLocation.update { mainLocation }
        _selectedSubLocation.update { subLocation }

        //TODO : filter초기화
        refreshPagingByCurrentFilters()
    }
    //HOME location

    fun updateTempBuildingFilter(tempList: List<BuildingFilterType>) {
        fetchTempCount(_queryState.value.copy(buildType = tempList.toBuildType()))
    }

    fun updateTempAuctionFilter(tempList: List<AuctionStatusFilterType>) {
        fetchTempCount(_queryState.value.copy(auctionFailCount = tempList.toAuctionFailCount()))
    }

    fun updateTempPriceRange(price: Price) {
        val (min, max) = when (price) {
            is Price.Range -> {
                if (price.start == MIN_PRICE && price.end == MAX_PRICE) -1L to -1L
                else price.start to price.end
            }

            is Price.Above -> price.value to -1L
            is Price.Below -> -1L to price.value
        }
        fetchTempCount(_queryState.value.copy(minimumPrice = min, maximumPrice = max))
    }

    private fun fetchTempCount(query: SearchQuery) {
        _tempCountJob?.cancel()
        _tempCountJob = viewModelScope.launch {
            _isTempCountLoading.value = true
            delay(300)
            remoteSearchRepository.getProductListByCursor("", query)
                .onSuccess { _tempProductSize.emit(it.totalCount) }
            _isTempCountLoading.value = false
        }
    }

    fun fetchTempCountFromTotal() {
        viewModelScope.launch {
            _tempProductSize.emit(_numberOfProduct.value)
        }
    }


    fun clearTempQuery() {
        _tempCountJob?.cancel()
        _isTempCountLoading.value = false
        viewModelScope.launch { _tempProductSize.emit(0L) }
    }
}


data class SearchQuery(
    val keyword: String = "unknown",
    val region: com.warehouseinhand.slug.data.network.search.Region = com.warehouseinhand.slug.data.network.search.Region.ALL,
    val district: String = "unkwon",
    val buildType: List<com.warehouseinhand.slug.data.network.search.BuildType> = listOf(com.warehouseinhand.slug.data.network.search.BuildType.ALL),
    val auctionFailCount: List<com.warehouseinhand.slug.data.network.search.AuctionFailCount> = listOf(
        com.warehouseinhand.slug.data.network.search.AuctionFailCount.ALL
    ),
    val verificationStatus: VerificationStatus = VerificationStatus.ALL,
    val minimumPrice: Long = -1,
    val maximumPrice: Long = -1,
    val soldOutStatus: SoldOutStatus = SoldOutStatus.ALL,
    val sort: Sort = Sort.LATEST_REGISTERED,
)
