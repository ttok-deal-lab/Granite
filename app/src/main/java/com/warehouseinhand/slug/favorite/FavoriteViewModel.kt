package com.warehouseinhand.slug.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
import com.warehouseinhand.slug.domain.sales.FavoriteSaleSummary
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.home.ProductItemUiModel.Companion.toUiModel
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import com.warehouseinhand.slug.ui.component.label.SlugLabelUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    val productUiModelList: Flow<PagingData<ProductItemUiModel>> =
        remoteUserDataRepository.getUserFavoriteProductList(userId = userId)
            .map { paging ->
                paging.map { domain ->
                    domain.toUiModel()
                }
            }
            .cachedIn(viewModelScope)
}
