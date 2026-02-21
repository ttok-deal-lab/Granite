package com.warehouseinhand.slug.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.home.ProductItemUiModel.Companion.toUiModel
import com.warehouseinhand.slug.util.CursorPage
import com.warehouseinhand.slug.util.CursorPaginationState
import com.warehouseinhand.slug.util.CursorPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    val remoteUserDataRepository: RemoteUserDataRepository,
    val localUserDataRepository: LocalUserDataRepository
) : ViewModel() {
    val userId: String = runBlocking {
        localUserDataRepository.getUserId().getOrNull() ?: ""
        //TODO : 에러 처리하던가 useCase로 보내던가.
    }
    private val _paginationState = MutableStateFlow(CursorPaginationState<ProductItemUiModel>())
    val paginationState: StateFlow<CursorPaginationState<ProductItemUiModel>> =
        _paginationState.asStateFlow()

    private val paginator = CursorPaginator(
        state = _paginationState,
        fetchPage = { cursor ->
            val page = remoteUserDataRepository.getFavoriteProductsByCursor(
                userId = userId,
                cursor = cursor,
            )
            CursorPage(
                items = page.items.map { it.toUiModel().copy(isFavorite = true)/*Favorite 목록이기에 항상 true*/ },
                nextCursor = page.nextCursor,
                totalCount = page.totalCount,
            )
        }
    )

    init {
        viewModelScope.launch { paginator.loadInitial() }
    }

    fun refresh() {
        viewModelScope.launch { paginator.loadInitial() }
    }

    fun loadMore() {
        viewModelScope.launch { paginator.loadMore() }
    }

    fun removeFavorite(productId: String) {
        viewModelScope.launch {
            remoteUserDataRepository.removeProductFavorite(userId, productId)
                .onSuccess {
                    paginator.removeItem { it.id == productId }
                }
        }
    }
}
