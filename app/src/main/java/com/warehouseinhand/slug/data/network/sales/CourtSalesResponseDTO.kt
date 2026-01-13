package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName
import com.warehouseinhand.slug.data.network.MapperToDomain
import com.warehouseinhand.slug.domain.court.CourtSalesItem
import com.warehouseinhand.slug.domain.court.SalesCategory

data class CourtSalesResponseDTO(
    @SerializedName("id")
    val id: Long,

    @SerializedName("salesBuildingName")
    val salesBuildingName: String,

    @SerializedName("salesAddress")
    val salesAddress: String,

    @SerializedName("salesCategories")
    val salesCategories: List<SalesCategory>,

    @SerializedName("salesDateTime")
    val salesDateTime: String,

    @SerializedName("appraisalPrice")
    val appraisalPrice: Long,

    @SerializedName("salesPicture")
    val salesPicture: String?,

    @SerializedName("failBidCount")
    val failBidCount: Int,

    @SerializedName("zzimCount")
    val zzimCount: Int,

    @SerializedName("soldOut")
    val soldOut: Boolean
) : MapperToDomain<CourtSalesItem> {

    override fun toDomain(): CourtSalesItem = CourtSalesItem(
        id = id,
        salesBuildingName = salesBuildingName,
        salesAddress = salesAddress,
        salesCategories = salesCategories,
        salesDateTime = salesDateTime,
        appraisalPrice = appraisalPrice,
        salesPicture = salesPicture,
        failBidCount = failBidCount,
        zzimCount = zzimCount,
        soldOut = soldOut
    )
}