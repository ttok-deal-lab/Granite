package com.warehouseinhand.slug.mypage.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.data.local.recent.RecentItemRepository
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.sales.RemoteSalesDataRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
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
    private val remoteUserDataRepository: RemoteUserDataRepository,
    private val localUserDataRepository: LocalUserDataRepository
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

                        val userId = localUserDataRepository.getUserId().getOrNull()

                        // userId를 못 가져온 경우 처리
                        if (userId == null) {
                            emit(RecentItemsLoadingState.Error(IllegalStateException("User not logged in")))
                            return@fold
                        }

                        val favoriteStatusMap = remoteUserDataRepository
                            .getFavoriteStatusMap(userId, ids)
                            .getOrElse { emptyList() } // 즐겨찾기 조회 실패 시 빈 리스트로
                            .associate { (it.productId to it.isFavorite) }

                        val itemMap = items.associateBy { it.id }
                        val sortedItems = ids.mapNotNull { id -> itemMap[id] }

                        val uiModelList = sortedItems.map { item ->
                            item.toUiModel(isFavorite = favoriteStatusMap[item.id] ?: false)
                        }

                        emit(RecentItemsLoadingState.Success(uiModelList))

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
