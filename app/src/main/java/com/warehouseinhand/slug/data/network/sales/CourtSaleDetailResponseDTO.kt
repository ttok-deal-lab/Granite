package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName
import com.warehouseinhand.slug.data.network.MapperToDomain
import com.warehouseinhand.slug.domain.sales.AppraisalDocument
import com.warehouseinhand.slug.domain.sales.ConditionReport
import com.warehouseinhand.slug.domain.sales.CourtSaleDetail
import com.warehouseinhand.slug.domain.sales.EstateLeaseInfo
import com.warehouseinhand.slug.domain.sales.NearbySalesStat
import com.warehouseinhand.slug.domain.sales.OccupationRelation
import com.warehouseinhand.slug.domain.sales.OccupationRelationReport
import com.warehouseinhand.slug.domain.sales.RightsAnalysis
import com.warehouseinhand.slug.domain.sales.SalesBuilding
import com.warehouseinhand.slug.domain.sales.SalesDetail
import com.warehouseinhand.slug.domain.sales.SalesItemDetail
import com.warehouseinhand.slug.domain.sales.SalesPicture

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

    // JSON에 isSoldOut 로 내려옴
    @SerializedName("isSoldOut")
    val isSoldOut: Boolean,

    @SerializedName("itemTypes")
    val itemTypes: List<String>,

    @SerializedName("appraisalPrice")
    val appraisalPrice: Long,

    @SerializedName("lowestSalesPrice")
    val lowestSalesPrice: Long,

    // 센티넬(-999999999) 가능
    @SerializedName("recentTransactionPrice")
    val recentTransactionPrice: Long,

    // 비정상 날짜("2026-99-99") 가능
    @SerializedName("recentTransactionDate")
    val recentTransactionDate: String,

    @SerializedName("bidType")
    val bidType: String,

    @SerializedName("salesDateTime")
    val salesDateTime: String,

    @SerializedName("salesLocation")
    val salesLocation: String,

    // JSON에 "null" 문자열로 옴
    @SerializedName("salesNote")
    val salesNote: String,

    @SerializedName("salesReceptionDate")
    val salesReceptionDate: String,

    @SerializedName("salesOpenDate")
    val salesOpenDate: String,

    @SerializedName("distributionRequiredDeadlineDate")
    val distributionRequiredDeadlineDate: String,

    @SerializedName("salesCategories")
    val salesCategories: List<String>,

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
    val conditionReport: ConditionReportDTO,

    @SerializedName("appraisalDocumentUrl")
    val appraisalDocumentUrl: String?,

    @SerializedName("appraisalDocuments")
    val appraisalDocuments: List<AppraisalDocumentDTO>,

    @SerializedName("nearbySalesStats")
    val nearbySalesStats: List<NearbySalesStatDTO>,

    @SerializedName("rightsAnalysis")
    val rightsAnalysis: List<RightsAnalysisDTO>,
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
            isSoldOut = isSoldOut,
            itemTypes = itemTypes,
            appraisalPrice = appraisalPrice,
            lowestSalesPrice = lowestSalesPrice,
            recentTransactionPrice = recentTransactionPrice,
            recentTransactionDate = recentTransactionDate,
            bidType = bidType,
            salesDateTime = salesDateTime,
            salesLocation = salesLocation,
            salesNote = salesNote,
            salesReceptionDate = salesReceptionDate,
            salesOpenDate = salesOpenDate,
            distributionRequiredDeadlineDate = distributionRequiredDeadlineDate,
            salesCategories = salesCategories,
            failBidCount = failBidCount,
            zzimCount = zzimCount,
            courtCode = courtCode,
            courtTeam = courtTeam,
            salesDetails = salesDetails.map { it.toDomain() },
            salesPictures = salesPictures.map { it.toDomain() },
            salesBuildings = salesBuildings.map { it.toDomain() },
            salesItemDetails = salesItemDetails.map { it.toDomain() },
            conditionReport = conditionReport.toDomain(),
            appraisalDocumentUrl = appraisalDocumentUrl,
            appraisalDocuments = appraisalDocuments.map { it.toDomain() },
            nearbySalesStats = nearbySalesStats.map { it.toDomain() },
            rightsAnalysis = rightsAnalysis.map { it.toDomain() },
        )
}

/** salesDetails */
data class SalesDetailDTO(
    @SerializedName("timeStamp")
    val timeStamp: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("leastSalesPrice")
    val leastSalesPrice: Long,

    @SerializedName("result")
    val result: String,
) : MapperToDomain<SalesDetail> {
    override fun toDomain(): SalesDetail =
        SalesDetail(
            timeStamp = timeStamp,
            type = type,
            location = location,
            leastSalesPrice = leastSalesPrice,
            result = result
        )
}

/** salesPictures */
data class SalesPictureDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("imageUrl")
    val imageUrl: String,
) : MapperToDomain<SalesPicture> {
    override fun toDomain(): SalesPicture =
        SalesPicture(sequence = sequence, imageUrl = imageUrl)
}

/** salesBuildings */
data class SalesBuildingDTO(
    @SerializedName("siDoAddressName")
    val siDoAddressName: String,

    @SerializedName("guAddressName")
    val guAddressName: String,

    @SerializedName("dongAddressName")
    val dongAddressName: String,

    @SerializedName("riAddressName")
    val riAddressName: String,

    @SerializedName("fullAddressName")
    val fullAddressName: String,

    @SerializedName("detailAddressName")
    val detailAddressName: String,

    @SerializedName("category")
    val category: String,
) : MapperToDomain<SalesBuilding> {
    override fun toDomain(): SalesBuilding =
        SalesBuilding(
            siDoAddressName = siDoAddressName,
            guAddressName = guAddressName,
            dongAddressName = dongAddressName,
            riAddressName = riAddressName,
            fullAddressName = fullAddressName,
            detailAddressName = detailAddressName,
            category = category
        )
}

/** salesItemDetails */
data class SalesItemDetailDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("content")
    val content: String,
) : MapperToDomain<SalesItemDetail> {
    override fun toDomain(): SalesItemDetail =
        SalesItemDetail(sequence = sequence, type = type, content = content)
}

/** conditionReport */
data class ConditionReportDTO(
    @SerializedName("investigationDate")
    val investigationDate: String,

    @SerializedName("estateLeaseInfos")
    val estateLeaseInfos: List<EstateLeaseInfoDTO>,

    @SerializedName("occupationRelations")
    val occupationRelations: List<OccupationRelationDTO>,

    @SerializedName("occupationRelationReports")
    val occupationRelationReports: List<OccupationRelationReportDTO>,
) : MapperToDomain<ConditionReport> {
    override fun toDomain(): ConditionReport =
        ConditionReport(
            investigationDate = investigationDate,
            estateLeaseInfos = estateLeaseInfos.map { it.toDomain() },
            occupationRelations = occupationRelations.map { it.toDomain() },
            occupationRelationReports = occupationRelationReports.map { it.toDomain() },
        )
}

data class EstateLeaseInfoDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("address")
    val address: String,

    @SerializedName("leaseRelation")
    val leaseRelation: String,
) : MapperToDomain<EstateLeaseInfo> {
    override fun toDomain(): EstateLeaseInfo =
        EstateLeaseInfo(sequence = sequence, address = address, leaseRelation = leaseRelation)
}

data class OccupationRelationDTO(
    @SerializedName("address")
    val address: String,

    @SerializedName("relation")
    val relation: String,

    @SerializedName("etc")
    val etc: String,
) : MapperToDomain<OccupationRelation> {
    override fun toDomain(): OccupationRelation =
        OccupationRelation(address = address, relation = relation, etc = etc)
}

data class OccupationRelationReportDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("address")
    val address: String,

    @SerializedName("occupant")
    val occupant: String,

    @SerializedName("relation")
    val relation: String,

    @SerializedName("occupiedPart")
    val occupiedPart: String,

    @SerializedName("purpose")
    val purpose: String,

    @SerializedName("duration")
    val duration: String,

    // 서버가 "-1" 문자열로 내려옴
    @SerializedName("deposit")
    val deposit: String,

    @SerializedName("rental")
    val rental: String,

    @SerializedName("movedAt")
    val movedAt: String,

    @SerializedName("confirmedAt")
    val confirmedAt: String,
) : MapperToDomain<OccupationRelationReport> {
    override fun toDomain(): OccupationRelationReport =
        OccupationRelationReport(
            sequence = sequence,
            address = address,
            occupant = occupant,
            relation = relation,
            occupiedPart = occupiedPart,
            purpose = purpose,
            duration = duration,
            deposit = deposit,
            rental = rental,
            movedAt = movedAt,
            confirmedAt = confirmedAt
        )
}

/** appraisalDocuments */
data class AppraisalDocumentDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("subTitle")
    val subTitle: String,

    @SerializedName("content")
    val content: String,
) : MapperToDomain<AppraisalDocument> {
    override fun toDomain(): AppraisalDocument =
        AppraisalDocument(sequence = sequence, title = title, subTitle = subTitle, content = content)
}

/** nearbySalesStats */
data class NearbySalesStatDTO(
    @SerializedName("term")
    val term: String,

    @SerializedName("salesCount")
    val salesCount: Int,

    @SerializedName("averageAppraisalPrice")
    val averageAppraisalPrice: Long,

    @SerializedName("averageSalesPrice")
    val averageSalesPrice: Long,

    @SerializedName("salesPriceRate")
    val salesPriceRate: Double,

    @SerializedName("averageFailBidCount")
    val averageFailBidCount: Double,
) : MapperToDomain<NearbySalesStat> {
    override fun toDomain(): NearbySalesStat =
        NearbySalesStat(
            term = term,
            salesCount = salesCount,
            averageAppraisalPrice = averageAppraisalPrice,
            averageSalesPrice = averageSalesPrice,
            salesPriceRate = salesPriceRate,
            averageFailBidCount = averageFailBidCount
        )
}

/** rightsAnalysis */
data class RightsAnalysisDTO(
    @SerializedName("name")
    val name: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("hasOppositionRight")
    val hasOppositionRight: String,

    @SerializedName("moveInReportDate")
    val moveInReportDate: String,

    @SerializedName("occupationStatus")
    val occupationStatus: String,

    @SerializedName("priorityRepaymentRight")
    val priorityRepaymentRight: String,

    @SerializedName("fixedDate")
    val fixedDate: String,

    @SerializedName("dividendRequest")
    val dividendRequest: String,

    @SerializedName("dividendRequestDate")
    val dividendRequestDate: String,

    @SerializedName("deposit")
    val deposit: Long,

    @SerializedName("monthlyRent")
    val monthlyRent: Long,
) : MapperToDomain<RightsAnalysis> {
    override fun toDomain(): RightsAnalysis =
        RightsAnalysis(
            name = name,
            role = role,
            hasOppositionRight = hasOppositionRight,
            moveInReportDate = moveInReportDate,
            occupationStatus = occupationStatus,
            priorityRepaymentRight = priorityRepaymentRight,
            fixedDate = fixedDate,
            dividendRequest = dividendRequest,
            dividendRequestDate = dividendRequestDate,
            deposit = deposit,
            monthlyRent = monthlyRent
        )
}