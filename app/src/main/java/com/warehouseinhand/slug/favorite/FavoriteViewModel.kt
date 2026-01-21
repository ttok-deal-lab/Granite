package com.warehouseinhand.slug.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.search.RemoteSearchRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
import com.warehouseinhand.slug.domain.sales.FavoriteSaleSummary
import com.warehouseinhand.slug.domain.search.AuctionSearchItem
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import com.warehouseinhand.slug.ui.component.label.SlugLabelUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
//    private val _favoriteUiModelList: MutableStateFlow<List<ProductItemUiModel>> =
//        MutableStateFlow(emptyList())
//    val favoriteUiModelList get() = _favoriteUiModelList.asStateFlow()

    val productUiModelList: Flow<PagingData<ProductItemUiModel>> =
        remoteUserDataRepository.getUserFavoriteProductList(userId = userId)
            .map { paging ->
                paging.map { domain ->
                    domain.toUiModel()
                }
            }
            .cachedIn(viewModelScope)


//    fun requestFavoriteList(){
//        networkWithProgress {
//            //TODO : getList!
//            delay(500)
//            _favoriteUiModelList.emit(ProductItemUiModel.testList)
//        }
//    }

    private val _isNeedToShowProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNeedToShowProgress get() = _isNeedToShowProgress.asStateFlow()

    private fun networkWithProgress(doWithAsyncBlock: suspend () -> Unit) {
        viewModelScope.launch {
            _isNeedToShowProgress.emit(true)

            doWithAsyncBlock.invoke()

            _isNeedToShowProgress.emit(false)
        }
    }


    fun FavoriteSaleSummary.toUiModel(
        now: LocalDateTime = LocalDateTime.now()
    ): ProductItemUiModel =
        ProductItemUiModel(
            priceOfProduct = appraisalPrice,
            nameOfProduct = salesBuildingName,
            location = salesAddress,
            daysLeft = getDaysLeftFromSalesDateTime(salesDateTime, now),
            buildingImage = salesPicture?.let(ImageResource::Url)
                ?: ImageResource.Id(R.drawable.logo_metaopo),
            isFavorite = isFavorite,          // ✅ 항상 true
            favoritePersons = zzimCount,
            infoChipList = buildingInfoChips(),
            id = id.toString(),
        )

    private fun FavoriteSaleSummary.buildingInfoChips()
            : List<SlugLabelUiModel> {

        val chips = mutableListOf<SlugLabelUiModel>()

        // 2. 매각 상태
        if (isSoldOut) {
            chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, "매각완료")
        } else if (failBidCount > 0) {
            chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, "유찰 ${failBidCount}회")
        }

        // 3. 건물 카테고리 (대표 1개)
        salesCategories.firstOrNull()?.let { category ->
            chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.Apartment, category)
            // 실제로는 category → 스타일 매핑 함수로 분리 권장
        }

        return chips
    }

    fun getDaysLeftFromSalesDateTime(
        salesDateTime: String,
        now: LocalDateTime = LocalDateTime.now()
    ): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val target = LocalDateTime.parse(salesDateTime, formatter)
        val days = Duration.between(now, target).toDays().toInt()
        return days.coerceAtLeast(0)
    }
}
