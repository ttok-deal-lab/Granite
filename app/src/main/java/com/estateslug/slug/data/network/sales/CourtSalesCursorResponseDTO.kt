package com.estateslug.slug.data.network.sales

import com.google.gson.annotations.SerializedName
import com.estateslug.slug.data.network.MapperToDomain
import com.estateslug.slug.domain.court.CourtSalesCursorPage
import com.estateslug.slug.domain.court.CourtSalesSummary
import com.estateslug.slug.domain.court.SalesCategory

data class CourtSalesCursorResponseDTO(
    @SerializedName("items")
    val items: List<CourtSalesCursorItemDTO>,

    @SerializedName("nextCursor")
    val nextCursor: String?,

    @SerializedName("hasNext")
    val hasNext: Boolean
): MapperToDomain<CourtSalesCursorPage>{
    override fun toDomain(): CourtSalesCursorPage = CourtSalesCursorPage(
        items = items.map(CourtSalesCursorItemDTO::toDomain),
        nextCursor = nextCursor,
        hasNext = hasNext,
    )

}

data class CourtSalesCursorItemDTO(
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
    val soldOut: Boolean,

    @SerializedName("court")
    val court: CourtDTO?,
): MapperToDomain<CourtSalesSummary> {
    override fun toDomain(): CourtSalesSummary = CourtSalesSummary(
        id = id,
        buildingName = salesBuildingName,
        address = salesAddress,
        categories = salesCategories,
        salesDateTime = salesDateTime,
        appraisalPrice = appraisalPrice,
        salesPicture = salesPicture,
        failBidCount = failBidCount,
        zzimCount = zzimCount,
        soldOut = soldOut,
        isFavorite = null,
        court = court?.toDomain()
    )
}