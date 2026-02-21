package com.warehouseinhand.slug.home

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.domain.court.CourtSalesItem
import com.warehouseinhand.slug.domain.court.SalesCategory
import com.warehouseinhand.slug.domain.search.AuctionSearchItem
import com.warehouseinhand.slug.ui.component.SlugText
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import com.warehouseinhand.slug.ui.component.label.SlugLabelUiModel
import com.warehouseinhand.slug.util.calculateDaysLeft
import kotlinx.coroutines.flow.MutableStateFlow

data class ProductItemUiModel(
    val id: String,
    val priceOfProduct: Long,
    val nameOfProduct: String,
    val location: String,
    val daysLeft: Int,
    val buildingImage: ImageResource,
    val isFavorite: Boolean,
    val favoritePersons: Long,
    val infoChipList: List<SlugLabelUiModel>
) {
    companion object {

        fun CourtSalesItem.toUiModel(
            nowMillis: Long = System.currentTimeMillis(),
            isFavorite: Boolean = false
        ): ProductItemUiModel {

            val daysLeft = calculateDaysLeft(
                salesDateTime = salesDateTime,
                nowMillis = nowMillis
            )

            return ProductItemUiModel(
                id = id,
                priceOfProduct = appraisalPrice,
                nameOfProduct = salesBuildingName,
                location = salesAddress,
                daysLeft = daysLeft,
                buildingImage =
                    salesPicture?.let { ImageResource.Url(it) }
                        ?: ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = isFavorite,
                favoritePersons = zzimCount,
                infoChipList = buildInfoChips()
            )
        }

        private fun CourtSalesItem.buildInfoChips(): List<SlugLabelUiModel> {
            val chips = mutableListOf<SlugLabelUiModel>()

            // 1) 매각 상태
            when {
                soldOut -> {
                    chips += SlugLabelUiModel(
                        labelStyle = SlugLabelStyle.BuildingInfo.State,
                        text = SlugText.Text("매각완료")
                    )
                }

                failBidCount > 0 -> {
                    chips += SlugLabelUiModel(
                        labelStyle = SlugLabelStyle.BuildingInfo.State,
                        text = SlugText.Text("유찰 ${failBidCount}회")
                    )
                }
            }

            // 2) 건물 타입 (대표 1개)
            salesCategories.firstOrNull()?.let { category ->
                chips += category.toLabelUiModel()
            }

            return chips
        }

        private fun SalesCategory.toLabelUiModel(): SlugLabelUiModel =
            when (this) {
                SalesCategory.APARTMENT ->
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Id(R.string.building_type_apartment)
                    )

                SalesCategory.VILLA ->
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Id(R.string.building_type_villa)
                    )

                SalesCategory.OFFICETEL ->
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Id(R.string.building_type_officetel)
                    )

                SalesCategory.HOUSING ->
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Id(R.string.building_type_house)
                    )

                SalesCategory.SHOP_HOUSE ->
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Id(R.string.building_type_commercial_house)
                    )

                else ->
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text(name))
            }


        fun AuctionSearchItem.toUiModel(
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

        fun AuctionSearchItem.buildSearchInfoChips(): List<SlugLabelUiModel> {

            val chips = mutableListOf<SlugLabelUiModel>()

            // 1. 인증 매물
            if (verified) {
                chips += SlugLabelUiModel(
                    SlugLabelStyle.GradientBackground.Verified,
                    SlugText.Text("인증매물")
                )
            }

            // 2. 매각 상태
            if (soldOut) {
                chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("매각완료"))
            } else if (failBidCount > 0) {
                chips += SlugLabelUiModel(
                    SlugLabelStyle.BuildingInfo.State,
                    SlugText.Text("유찰 ${failBidCount}회")
                )
            }

            // 3. 건물 카테고리 (대표 1개)
            salesCategories.firstOrNull()?.let { category ->
                chips += SlugLabelUiModel(
                    SlugLabelStyle.BuildingInfo.BuildingType,
                    getStringFromCategory(category)
                )
                // 실제로는 category → 스타일 매핑 함수로 분리 권장
            }
            return chips
        }

        fun getStringFromCategory(text: String): SlugText =
            when (text) {
                "VILLA" -> SlugText.Id(R.string.building_type_villa)
                "APARTMENT" -> SlugText.Id(R.string.building_type_apartment)
                "OFFICETEL" -> SlugText.Id(R.string.building_type_officetel)
                "SHOP_HOUSE" -> SlugText.Id(R.string.building_type_commercial_house)
                "HOUSING" -> SlugText.Id(R.string.building_type_house)
                else -> SlugText.Text(text)
            }


        val testList = listOf(
            ProductItemUiModel(
                id = "1",
                priceOfProduct = 132100000L,
                nameOfProduct = "시가정 센트럴 아이파크",
                location = "서울특별시 서초구 방배동",
                daysLeft = 3,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 100,
                infoChipList = listOf(
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Text("아파트")
                    ),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("유찰 2회"))
                )
            ),
            ProductItemUiModel(
                id = "2",
                priceOfProduct = 85000000L,
                nameOfProduct = "롯데캐슬 골드파크",
                location = "경기도 성남시 분당구",
                daysLeft = 7,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = false,
                favoritePersons = 0,
                infoChipList = listOf(
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Text("아파트")
                    ),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("유찰 2회"))
                )
            ),
            ProductItemUiModel(
                id = "3",
                priceOfProduct = 245000000L,
                nameOfProduct = "한강 리버파크 오피스텔",
                location = "서울특별시 용산구 이촌동",
                daysLeft = 1,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 234,
                infoChipList = listOf(
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Text("아파트")
                    ),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("유찰 2회"))
                )
            ),
            ProductItemUiModel(
                id = "4",
                priceOfProduct = 67500000L,
                nameOfProduct = "대우 푸르지오 단지",
                location = "인천광역시 남동구 구월동",
                daysLeft = 12,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = false,
                favoritePersons = 78,
                infoChipList = listOf(
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Text("아파트")
                    ),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("유찰 3회"))
                )
            ),
            ProductItemUiModel(
                id = "5",
                priceOfProduct = 189000000L,
                nameOfProduct = "삼성 래미안 타워",
                location = "서울특별시 강남구 청담동",
                daysLeft = 5,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 156,
                infoChipList = listOf(
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Text("아파트")
                    ),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("유찰 2회"))
                )
            ),
            ProductItemUiModel(
                id = "6",
                priceOfProduct = 189000000L,
                nameOfProduct = "삼성 래미안 타워",
                location = "서울특별시 강남구 청담동",
                daysLeft = 5,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 156,
                infoChipList = listOf(
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Text("아파트")
                    ),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("유찰 3회"))
                )
            ),
            ProductItemUiModel(
                id = "7",
                priceOfProduct = 189000000L,
                nameOfProduct = "삼성 래미안 타워",
                location = "서울특별시 강남구 청담동",
                daysLeft = 5,
                buildingImage = ImageResource.Id(R.drawable.logo_metaopo),
                isFavorite = true,
                favoritePersons = 156,
                infoChipList = listOf(
                    SlugLabelUiModel(
                        SlugLabelStyle.BuildingInfo.BuildingType,
                        SlugText.Text("아파트")
                    ),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("유찰 2회"))
                )
            )
        )

        @Composable
        fun pagingItems() = MutableStateFlow(PagingData.from(ProductItemUiModel.testList))
            .collectAsLazyPagingItems()
    }
}