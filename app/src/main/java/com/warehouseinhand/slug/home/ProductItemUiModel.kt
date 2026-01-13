package com.warehouseinhand.slug.home

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import kotlinx.coroutines.flow.MutableStateFlow

data class ProductItemUiModel(
    val id:Long,
    val priceOfProduct: Long,
    val nameOfProduct: String,
    val location: String,
    val daysLeft: Int,
    val buildingImage: ImageResource,
    val isFavorite: Boolean,
    val favoritePersons: Long,
    val infoChipList: List<Pair<SlugLabelStyle, String>>
) {
    companion object {

        val testList = listOf(
            ProductItemUiModel(
                id = 1,
                priceOfProduct = 132100000L,
                nameOfProduct = "시가정 센트럴 아이파크",
                location = "서울특별시 서초구 방배동",
                daysLeft = 3,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 100,
                infoChipList = listOf(
                    (SlugLabelStyle.BuildingInfo.Apartment to "아파트"),
                    SlugLabelStyle.BuildingInfo.State to "유찰 2회"
                )
            ),
            ProductItemUiModel(
                id = 2,
                priceOfProduct = 85000000L,
                nameOfProduct = "롯데캐슬 골드파크",
                location = "경기도 성남시 분당구",
                daysLeft = 7,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = false,
                favoritePersons = 0,
                infoChipList = listOf(
                    (SlugLabelStyle.BuildingInfo.Apartment to "아파트"),
                    SlugLabelStyle.BuildingInfo.State to "유찰 2회"
                )
            ),
            ProductItemUiModel(
                id = 3,
                priceOfProduct = 245000000L,
                nameOfProduct = "한강 리버파크 오피스텔",
                location = "서울특별시 용산구 이촌동",
                daysLeft = 1,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 234,
                infoChipList = listOf(
                    (SlugLabelStyle.BuildingInfo.Apartment to "아파트"),
                    SlugLabelStyle.BuildingInfo.State to "유찰 2회"
                )
            ),
            ProductItemUiModel(
                id = 4,
                priceOfProduct = 67500000L,
                nameOfProduct = "대우 푸르지오 단지",
                location = "인천광역시 남동구 구월동",
                daysLeft = 12,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = false,
                favoritePersons = 78,
                infoChipList = listOf(
                    (SlugLabelStyle.BuildingInfo.Apartment to "아파트"),
                    SlugLabelStyle.BuildingInfo.State to "유찰 3회"
                )
            ),
            ProductItemUiModel(
                id = 5,
                priceOfProduct = 189000000L,
                nameOfProduct = "삼성 래미안 타워",
                location = "서울특별시 강남구 청담동",
                daysLeft = 5,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 156,
                infoChipList = listOf(
                    (SlugLabelStyle.BuildingInfo.Apartment to "아파트"),
                    SlugLabelStyle.BuildingInfo.State to "유찰 2회"
                )
            ),
            ProductItemUiModel(
                id = 6,
                priceOfProduct = 189000000L,
                nameOfProduct = "삼성 래미안 타워",
                location = "서울특별시 강남구 청담동",
                daysLeft = 5,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 156,
                infoChipList = listOf(
                    (SlugLabelStyle.BuildingInfo.Apartment to "아파트"),
                    SlugLabelStyle.BuildingInfo.State to "유찰 3회"
                )
            ),
            ProductItemUiModel(
                id = 7,
                priceOfProduct = 189000000L,
                nameOfProduct = "삼성 래미안 타워",
                location = "서울특별시 강남구 청담동",
                daysLeft = 5,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 156,
                infoChipList = listOf(
                    (SlugLabelStyle.BuildingInfo.Apartment to "아파트"),
                    SlugLabelStyle.BuildingInfo.State to "유찰 2회"
                )
            )
        )
        @Composable
        fun pagingItems() = MutableStateFlow(PagingData.from(ProductItemUiModel.testList))
            .collectAsLazyPagingItems()
    }
}