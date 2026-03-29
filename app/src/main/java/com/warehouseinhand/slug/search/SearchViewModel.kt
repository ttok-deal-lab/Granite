package com.warehouseinhand.slug.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.warehouseinhand.slug.data.local.search.RecentSearchRepository
import com.warehouseinhand.slug.data.network.search.RemoteSearchRepository
import com.warehouseinhand.slug.data.network.search.SoldOutStatus
import com.warehouseinhand.slug.data.network.search.VerificationStatus
import com.warehouseinhand.slug.home.AuctionStatusFilterType
import com.warehouseinhand.slug.home.AuctionStatusFilterType.Companion.toAuctionFailCount
import com.warehouseinhand.slug.home.BuildingFilterType
import com.warehouseinhand.slug.home.BuildingFilterType.Companion.toBuildType
import com.warehouseinhand.slug.home.Price
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.home.ProductItemUiModel.Companion.toUiModel
import com.warehouseinhand.slug.home.SearchQuery
import com.warehouseinhand.slug.home.ToggleFilterType
import com.warehouseinhand.slug.home.bottomsheet.sorting.SortingType
import com.warehouseinhand.slug.home.component.FilterButtonState
import com.warehouseinhand.slug.search.navigation.RouteSearchResult
import com.warehouseinhand.slug.util.CursorPaginationState
import com.warehouseinhand.slug.util.CursorPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val remoteSearchRepository: RemoteSearchRepository,
    private val recentSearchRepository: RecentSearchRepository,
) : ViewModel() {

    private val route: RouteSearchResult? = runCatching {
        savedStateHandle.toRoute<RouteSearchResult>()
    }.getOrNull()

    private val _searchKeyword = MutableStateFlow(route?.keyword ?: "")
    val searchKeyword = _searchKeyword.asStateFlow()

    val recentSearches: StateFlow<List<String>> =
        recentSearchRepository.getRecentSearchKeywords()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _autoCompleteResults = MutableStateFlow<List<String>>(emptyList())
    val autoCompleteResults = _autoCompleteResults.asStateFlow()

    private val _isSearchResultEmpty = MutableStateFlow(false)
    val isSearchResultEmpty = _isSearchResultEmpty.asStateFlow()

    private val _isSearchLoading = MutableStateFlow(false)
    val isSearchLoading = _isSearchLoading.asStateFlow()

    private val _numberOfProduct = MutableStateFlow(0L)
    val numberOfProduct = _numberOfProduct.asStateFlow()

    // 정렬
    private val _selectedSortingType = MutableStateFlow(SortingType.NEWEST)
    val selectedSortingType = _selectedSortingType.asStateFlow()

    // 필터 버튼 UI 상태
    private val _stateList: MutableStateFlow<List<FilterButtonState>> =
        MutableStateFlow(FilterButtonState.defaultStateList)
    val stateList get() = _stateList.asStateFlow()

    // 개별 필터 상태
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
        MutableStateFlow(Price.Range(MIN_PRICE, MAX_PRICE))
    val priceRange get() = _priceRange.asStateFlow()

    private var isFinishedProductFilterSelected = false


    // 바텀시트 내 매물 수 표시용
    private val _tempProductSize: MutableStateFlow<Long> = MutableStateFlow(0L)
    val tempProductSize get() = _tempProductSize.asStateFlow()

    private val _isTempCountLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTempCountLoading: StateFlow<Boolean> = _isTempCountLoading.asStateFlow()

    private var _tempCountJob: Job? = null

    private val _queryState = MutableStateFlow(
        SearchQuery(keyword = route?.keyword ?: "unknown")
    )

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

    private var autoCompleteJob: Job? = null

    fun updateSearchKeyword(keyword: String) {
        _searchKeyword.update { keyword }
        _isSearchResultEmpty.value = false
        autoCompleteJob?.cancel()
        if (keyword.isNotEmpty()) {
            autoCompleteJob = viewModelScope.launch {
                delay(300L)
                fetchAutoComplete(keyword)
            }
        } else {
            _autoCompleteResults.update { emptyList() }
        }
    }

    fun search(keyword: String) {
        viewModelScope.launch {
            if (keyword.isBlank()) return@launch
            addToRecentSearches(keyword)
            _searchKeyword.update { keyword }
            refreshPagingByCurrentFilters()
        }
    }

    private var lastJob: Job? = Job().apply { complete() }

    fun searchWithCheck(keyword: String, onHasResult: () -> Unit) {
        lastJob?.cancel()
        lastJob = viewModelScope.launch {
            if (keyword.isBlank()) return@launch
            _isSearchLoading.value = true
            _isSearchResultEmpty.value = false
            addToRecentSearches(keyword)
            _searchKeyword.update { keyword }

            remoteSearchRepository.getProductListByCursor(
                nextCursor = "",
                query = SearchQuery(keyword = keyword)
            ).onSuccess { result ->
                if (result.totalCount > 0) {
                    refreshPagingByCurrentFilters()
                    onHasResult()
                } else {
                    _isSearchResultEmpty.value = true
                }
            }.onFailure {
                _isSearchResultEmpty.value = true
            }

            _isSearchLoading.value = false
        }
    }

    fun clearSearchKeyword() {
        _searchKeyword.update { "" }
        _autoCompleteResults.update { emptyList() }
    }

    fun removeRecentSearch(keyword: String) {
        viewModelScope.launch {
            recentSearchRepository.removeRecentSearch(keyword)
        }
    }

    fun clearAllRecentSearches() {
        viewModelScope.launch {
            recentSearchRepository.clearRecentSearches()
        }
    }

    // --- 필터/정렬 ---

    fun changeVerifiedSelected() {
        isVerifiedFilterSelected = !isVerifiedFilterSelected
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun changeBuildingFilterSelectList(list: List<BuildingFilterType>) {
        _buildingFilterSelectedList.update {
            BuildingFilterType.entries.filter { list.contains(it) }
        }
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun changeAuctionFilterSelectList(list: List<AuctionStatusFilterType>) {
        _auctionStateFilterSelectedList.update {
            AuctionStatusFilterType.entries.filter { list.contains(it) }
        }
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun changePriceRange(price: Price) {
        _priceRange.update { price }
        updateFilterState()
        refreshPagingByCurrentFilters()
    }


    fun changeFinishedSelected() {
        isFinishedProductFilterSelected = !isFinishedProductFilterSelected
        updateFilterState()
        refreshPagingByCurrentFilters()
    }

    fun requestChangeSortingType(type: SortingType) {
        _selectedSortingType.value = type
        refreshPagingByCurrentFilters()
    }

    private fun updateFilterState() {
        val buildingList = buildingFilterSelectedList.value
        val isBuildingListNotEmpty = buildingList.isNotEmpty()

        val auctionStateList = auctionStateFilterSelectedList.value
        val isAuctionStateListNotEmpty = auctionStateList.isNotEmpty()

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

    private fun refreshPagingByCurrentFilters() {
        _queryState.value = buildQueryFromCurrentStates()
    }

    private fun buildQueryFromCurrentStates(): SearchQuery {
        val verificationStatus =
            if (isVerifiedFilterSelected) VerificationStatus.VERIFIED else VerificationStatus.ALL

        val buildType = buildingFilterSelectedList.value.toBuildType()
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

        return SearchQuery(
            keyword = _searchKeyword.value.ifBlank { "unknown" },
            buildType = buildType,
            auctionFailCount = auctionFailCount,
            verificationStatus = verificationStatus,
            soldOutStatus = soldOutStatus,
            minimumPrice = minPrice,
            maximumPrice = maxPrice,
            sort = sort
        )
    }

    private suspend fun addToRecentSearches(keyword: String) {
        recentSearchRepository.addRecentSearch(keyword)
    }

    private suspend fun fetchAutoComplete(keyword: String) {
        remoteSearchRepository.searchAutoComplete(keyword)
            .onSuccess { results -> _autoCompleteResults.update { results } }
            .onFailure { _autoCompleteResults.update { emptyList() } }
    }

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
        viewModelScope.launch { _tempProductSize.emit(_numberOfProduct.value) }
    }

    fun clearTempQuery() {
        _tempCountJob?.cancel()
        _isTempCountLoading.value = false
        viewModelScope.launch { _tempProductSize.emit(0L) }
    }
}
