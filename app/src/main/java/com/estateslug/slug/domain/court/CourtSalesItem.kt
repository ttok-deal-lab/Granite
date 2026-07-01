package com.estateslug.slug.domain.court

data class CourtSalesItem(
    val id: String,
    val salesBuildingName: String,
    val salesAddress: String,
    val salesCategories: List<SalesCategory>,
    val salesDateTime: String,
    val appraisalPrice: Long,
    val salesPicture: String?,
    val failBidCount: Int,
    val zzimCount: Long,
    val soldOut: Boolean,
    val court: Court? = null,
)