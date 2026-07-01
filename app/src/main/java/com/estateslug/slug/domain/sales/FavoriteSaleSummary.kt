package com.estateslug.slug.domain.sales

data class FavoriteSaleSummary(
    val id: Long,
    val caseNumber: String,
    val soldOut: Boolean,
    val buildingName: String?,
    val salesAddress: String,
    val salesCategories: List<String>,
    val salesDateTime: String,
    val appraisalPrice: Long,
    val salesPicture: String?,
    val failBidCount: Int,
    val zzimCount: Long,
    val registerDate: String,
    val isFavorite: Boolean,
    val verified: Boolean,
)