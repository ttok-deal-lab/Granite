package com.warehouseinhand.slug.detail

import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.component.SlugText
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.label.SlugLabelBackground
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import com.warehouseinhand.slug.ui.component.label.SlugLabelUiModel
import com.warehouseinhand.slug.ui.theme.Critical
import com.warehouseinhand.slug.ui.theme.CriticalWeak
import kotlin.collections.List

data class DetailSimpleInformationUiModel(
    val topTitle: String,
    val imageList: List<ImageResource>,
    val isFavorite: Boolean,
    val numberOfFavorite: Int,
    val nameOfProduct: String,
    val numberOfProduct: String,
    val typeDisplayName: String,
    val size: String,
    val labelModels: List<SlugLabelUiModel>,
    val lowestPrice: Long,
    val appraisalPrice: Long,
    val priceDiff: Long,
    val recentDealPrice: Long,
    val recentDealDate: String,
    val lastSaleDate: String,
) {
    companion object {

        val preview: DetailSimpleInformationUiModel =
            DetailSimpleInformationUiModel(
               topTitle = "관천로 22길 52",
                isFavorite = false,
                numberOfFavorite = 100,
                nameOfProduct = "신촌금호2단지",
                numberOfProduct = "2023타경 102411",
                typeDisplayName = "아파트",
                size = "공급 110.52㎡ (33평)",
                labelModels = listOf(
                    SlugLabelUiModel(SlugLabelStyle.GradientBackground.Verified , SlugText.Text("인증매물")),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State , SlugText.Text("유찰 2회")),
                    SlugLabelUiModel(SlugLabelStyle.Dynamic(
                        background = SlugLabelBackground.Solid(CriticalWeak),
                        textColor = Critical
                    ) , SlugText.Text("매각 D-2")),
                    SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State , SlugText.Text("매각 D-4")),
                ),
                lowestPrice = 183_200_000L,
                appraisalPrice = 230_000_000L,
                priceDiff = -48_000_000L,
                recentDealPrice = 554_210_000L,
                recentDealDate = "25.03.16",
                lastSaleDate = "2025.04.08 10:00",
                imageList = listOf(
                    ImageResource.Id(R.drawable.logo_metaopo),
                    ImageResource.Id(R.drawable.logo_metaopo),
                    ImageResource.Id(R.drawable.logo_metaopo),
                )
            )
    }
}
