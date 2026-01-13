package com.warehouseinhand.slug.domain.court

data class CourtSalesItem(
    val id: Long,
    val salesBuildingName: String,
    val salesAddress: String,
    val salesCategories: List<SalesCategory>,
    val salesDateTime: String,
    val appraisalPrice: Long,
    val salesPicture: String?,
    val failBidCount: Int,
    val zzimCount: Int,
    val soldOut: Boolean,
)