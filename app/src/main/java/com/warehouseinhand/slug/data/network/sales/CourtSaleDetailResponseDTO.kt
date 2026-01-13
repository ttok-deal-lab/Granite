package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName
import com.warehouseinhand.slug.data.network.MapperToDomain
import com.warehouseinhand.slug.domain.court.SalesCategory

data class CourtSaleDetailResponseDTO(
    @SerializedName("id")
    val id: Long,

    @SerializedName("salesNumber")
    val salesNumber: String,

    @SerializedName("verified")
    val verified: Boolean,

    @SerializedName("salesBuildingName")
    val salesBuildingName: String,

    @SerializedName("salesAddress")
    val salesAddress: String,

    @SerializedName("exclusiveArea")
    val exclusiveArea: Double,

    @SerializedName("caseName")
    val caseName: String,

    @SerializedName("creditorCount")
    val creditorCount: Int,

    @SerializedName("itemTypes")
    val itemTypes: List<SalesItemType>,

    @SerializedName("appraisalPrice")
    val appraisalPrice: Long,

    @SerializedName("lowestSalesPrice")
    val lowestSalesPrice: Long,

    @SerializedName("recentTransactionPrice")
    val recentTransactionPrice: Long,

    @SerializedName("recentTransactionDate")
    val recentTransactionDate: String, // "2025-01-01"

    @SerializedName("bidType")
    val bidType: String, // 서버가 한글 문자열로 주는 형태라 enum 안 잡음

    @SerializedName("salesDateTime")
    val salesDateTime: String, // "yyyy-MM-dd HH:mm:ss"

    @SerializedName("salesLocation")
    val salesLocation: String,

    @SerializedName("salesNote")
    val salesNote: String?,

    @SerializedName("salesReceptionDate")
    val salesReceptionDate: String?,

    @SerializedName("salesOpenDate")
    val salesOpenDate: String?,

    @SerializedName("distributionRequiredDeadlineDate")
    val distributionRequiredDeadlineDate: String?,

    @SerializedName("salesCategories")
    val salesCategories: List<SalesCategory>,

    @SerializedName("failBidCount")
    val failBidCount: Int,

    @SerializedName("zzimCount")
    val zzimCount: Int,

    @SerializedName("courtCode")
    val courtCode: String,

    @SerializedName("courtTeam")
    val courtTeam: String,

    @SerializedName("salesDetails")
    val salesDetails: List<SalesDetailDTO>,

    @SerializedName("salesPictures")
    val salesPictures: List<SalesPictureDTO>,

    @SerializedName("salesBuildings")
    val salesBuildings: List<SalesBuildingDTO>,

    @SerializedName("salesItemDetails")
    val salesItemDetails: List<SalesItemDetailDTO>,

    @SerializedName("conditionReport")
    val conditionReport: ConditionReportDTO?,

    @SerializedName("appraisalDocumentUrl")
    val appraisalDocumentUrl: String?,

    @SerializedName("appraisalDocuments")
    val appraisalDocuments: List<AppraisalDocumentDTO>,

    @SerializedName("nearbySalesStats")
    val nearbySalesStats: List<NearbySalesStatDTO>,

    @SerializedName("rightsAnalysis")
    val rightsAnalysis: List<RightsAnalysisDTO>,

    @SerializedName("soldOut")
    val soldOut: Boolean
) : MapperToDomain<CourtSaleDetail> {

    override fun toDomain(): CourtSaleDetail =
        CourtSaleDetail(
            id = id,
            salesNumber = salesNumber,
            verified = verified,
            salesBuildingName = salesBuildingName,
            salesAddress = salesAddress,
            exclusiveArea = exclusiveArea,
            caseName = caseName,
            creditorCount = creditorCount,
            itemTypes = itemTypes,
            appraisalPrice = appraisalPrice,
            lowestSalesPrice = lowestSalesPrice,
            recentTransactionPrice = recentTransactionPrice,
            recentTransactionDate = recentTransactionDate,
            bidType = bidType,
            salesDateTime = salesDateTime,
            salesLocation = salesLocation,
            salesNote = salesNote,
            failBidCount = failBidCount,
            zzimCount = zzimCount,
            soldOut = soldOut
        )
}

enum class SalesItemType {
    APARTMENT,
    DETACHED_HOUSE,
    MULTI_HOUSEHOLD,
    ROW_HOUSE,
    MULTI_FAMILY,
    VILLA,
    AUTOMOBILE,
    HEAVY_EQUIPMENT,
    LAND,
    FOREST,
    FARMLAND,
    COMMERCIAL,
    OFFICE_TEL,
    NEIGHBORHOOD_FACILITY,
    OTHER
}