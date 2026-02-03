package com.warehouseinhand.slug.mypage.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.data.local.recent.RecentItemRepository
import com.warehouseinhand.slug.data.network.sales.RemoteSalesDataRepository
import com.warehouseinhand.slug.domain.user.GetFavoriteStatusUseCase
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.home.ProductItemUiModel.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
internal class RecentItemsViewModel @Inject constructor(
    private val recentItemRepository: RecentItemRepository,
    private val remoteSalesDataRepository: RemoteSalesDataRepository,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase

) : ViewModel() {

    val uiState = recentItemRepository
        .getRecentItemIds()
        .flatMapLatest { ids ->
            fetchItems(ids)
                .onStart { emit(RecentItemsLoadingState.Loading) }
        }
        .map { loadingState -> RecentItemsUiState(loadingState = loadingState) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = RecentItemsUiState()
        )

    suspend fun onItemViewed(id: String) {
        recentItemRepository.addRecentItem(id)
    }

    suspend fun clearRecentItems() {
        recentItemRepository.clearRecentItems()
    }

    private fun fetchItems(ids: List<String>): kotlinx.coroutines.flow.Flow<RecentItemsLoadingState> =
        flow {
            if (ids.isEmpty()) {
                emit(RecentItemsLoadingState.Success(emptyList()))
                return@flow
            }
            remoteSalesDataRepository.getCourtSales(ids = ids.toTypedArray())
                .fold(
                    onSuccess = { items ->
                        getFavoriteStatusUseCase(ids)
                            .fold(
                                onSuccess = { favoriteStatusMap ->
                                    val itemMap = items.associateBy { it.id }
                                    val sortedItems = ids.mapNotNull { id -> itemMap[id] }
                                    val uiModelList = sortedItems.map { item ->
                                        item.toUiModel(isFavorite = favoriteStatusMap[item.id] ?: false)
                                    }
                                    emit(RecentItemsLoadingState.Success(uiModelList))
                                },
                                onFailure = {
                                    emit(RecentItemsLoadingState.Error(it))
                                }
                            )
                    },
                    onFailure = { emit(RecentItemsLoadingState.Error(it)) }
                )

        }
}


sealed class RecentItemsLoadingState {
    object Loading : RecentItemsLoadingState()
    data class Success(val items: List<ProductItemUiModel>) : RecentItemsLoadingState()
    data class Error(val exception: Throwable) : RecentItemsLoadingState()
}

data class RecentItemsUiState(
    val loadingState: RecentItemsLoadingState = RecentItemsLoadingState.Loading
)
