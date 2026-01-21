package com.warehouseinhand.slug.domain.sales

data class FavoriteSaleSummary(
    val id: Long,
    val isSoldOut: Boolean,
    val salesBuildingName: String,
    val salesAddress: String,
    val salesCategories: List<String>,
    val salesDateTime: String,
    val appraisalPrice: Long,
    val salesPicture: String?,
    val failBidCount: Int,
    val zzimCount: Long,
    val isFavorite: Boolean,
)