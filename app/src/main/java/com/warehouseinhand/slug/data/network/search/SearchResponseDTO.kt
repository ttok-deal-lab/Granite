package com.warehouseinhand.slug.data.network.search

import com.google.gson.annotations.SerializedName
import com.warehouseinhand.slug.data.network.MapperToDomain
import com.warehouseinhand.slug.data.network.MapperToDomain.Companion.toDomain
import com.warehouseinhand.slug.domain.search.AuctionSearchItem
import com.warehouseinhand.slug.domain.search.SearchResultPage

data class SearchResponseDTO(
    @SerializedName("searchHitCount")
    val searchHitCount: Long,

    @SerializedName("auctionItemResponses")
    val auctionItemResponses: List<AuctionItemResponseDTO>,

    @SerializedName("nextCursor")
    val nextCursor: String?
) : MapperToDomain<SearchResultPage> {
    override fun toDomain(): SearchResultPage =
        SearchResultPage(
            totalCount = searchHitCount,
            items = auctionItemResponses.toDomain(),
            nextCursor = nextCursor
        )

}

data class AuctionItemResponseDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("caseNumber")
    val caseNumber: String,

    @SerializedName("salesAddress")
    val salesAddress: String,

    @SerializedName("salesBuildingName")
    val salesBuildingName: String,

    @SerializedName("salesCategories")
    val salesCategories: List<String>,

    @SerializedName("salesDateTime")
    val salesDateTime: String,

    @SerializedName("appraisalPrice")
    val appraisalPrice: Long,

    @SerializedName("salesPicture")
//    val salesPicture: List<SalesPictureDTO>?,
    val salesPicture: String?,

    @SerializedName("failBidCount")
    val failBidCount: Long,

    @SerializedName("zzimCount")
    val zzimCount: Long,

    @SerializedName("registerDate")
    val registerDate: String,

    @SerializedName("verified")
    val verified: Boolean,

    @SerializedName("isSoldOut")
    val soldOut: Boolean
):MapperToDomain<AuctionSearchItem>{
    override fun toDomain(): AuctionSearchItem = AuctionSearchItem(
        id = id,
        caseNumber = caseNumber,
        buildingName = salesBuildingName,
        address = salesAddress,
        salesCategories = salesCategories,
        salesDateTime = salesDateTime,
        registerDate = registerDate,
        appraisalPrice = appraisalPrice,
        salesPicture = salesPicture?:"",
        failBidCount = failBidCount,
        zzimCount = zzimCount,
        verified = verified,
        soldOut = soldOut,
        isFavorite = false //TODO : 데이터 내려 오면 처리해야함
    )

}