package com.warehouseinhand.slug.data.network.user

import com.google.gson.annotations.SerializedName
import com.warehouseinhand.slug.data.network.MapperToDomain
import com.warehouseinhand.slug.domain.sales.FavoriteSaleSummary

data class FavoriteSalesCursorResponseDTO(
    @SerializedName("items")
    val items: List<FavoriteSalesCursorItemDTO>,

    @SerializedName("nextCursor")
    val nextCursor: String?,

    @SerializedName("hasNext")
    val hasNext: Boolean
)

data class FavoriteSalesCursorItemDTO(
    @SerializedName("id")
    val id: Long,

    @SerializedName("isSoldOut")
    val isSoldOut: Boolean,

    @SerializedName("salesBuildingName")
    val salesBuildingName: String,

    @SerializedName("salesAddress")
    val salesAddress: String,

    @SerializedName("salesCategories")
    val salesCategories: List<String>,

    @SerializedName("salesDateTime")
    val salesDateTime: String,

    @SerializedName("appraisalPrice")
    val appraisalPrice: Long,

    @SerializedName("salesPicture")
    val salesPicture: String?,

    @SerializedName("failBidCount")
    val failBidCount: Int,

    @SerializedName("zzimCount")
    val zzimCount: Long,
): MapperToDomain<FavoriteSaleSummary>{
    override fun toDomain(): FavoriteSaleSummary =
        FavoriteSaleSummary(
            id = id,
            isSoldOut = isSoldOut,
            salesBuildingName = salesBuildingName,
            salesAddress = salesAddress,
            salesCategories = salesCategories,
            salesDateTime = salesDateTime,
            appraisalPrice = appraisalPrice,
            salesPicture = salesPicture,
            failBidCount = failBidCount,
            zzimCount = zzimCount,
            isFavorite = true
        )
}

