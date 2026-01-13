package com.warehouseinhand.slug.data.network.sales

data class CourtSaleDetail(
    val id: Long,
    val salesNumber: String,
    val verified: Boolean,
    val salesBuildingName: String,
    val salesAddress: String,
    val exclusiveArea: Double,
    val caseName: String,
    val creditorCount: Int,
    val itemTypes: List<SalesItemType>,
    val appraisalPrice: Long,
    val lowestSalesPrice: Long,
    val recentTransactionPrice: Long,
    val recentTransactionDate: String,
    val bidType: String,
    val salesDateTime: String,
    val salesLocation: String,
    val salesNote: String?,
    val failBidCount: Int,
    val zzimCount: Int,
    val soldOut: Boolean,
)