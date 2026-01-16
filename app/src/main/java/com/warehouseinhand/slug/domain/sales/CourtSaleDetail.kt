package com.warehouseinhand.slug.domain.sales

data class CourtSaleDetail(
    val id: Long,
    val salesNumber: String,
    val verified: Boolean,
    val salesBuildingName: String,
    val salesAddress: String,
    val exclusiveArea: Double,
    val caseName: String,
    val creditorCount: Int,
    val isSoldOut: Boolean,
    val itemTypes: List<String>, // TODO 타입으로 받아야함!
    val appraisalPrice: Long,
    val lowestSalesPrice: Long,
    val recentTransactionPrice: Long,
    val recentTransactionDate: String,
    val bidType: String,
    val salesDateTime: String,
    val salesLocation: String,
    val salesNote: String,
    val salesReceptionDate: String,
    val salesOpenDate: String,
    val distributionRequiredDeadlineDate: String,
    val salesCategories: List<String>,
    val failBidCount: Int,
    val zzimCount: Int,
    val courtCode: String,
    val courtTeam: String,
    val salesDetails: List<SalesDetail>,
    val salesPictures: List<SalesPicture>,
    val salesBuildings: List<SalesBuilding>,
    val salesItemDetails: List<SalesItemDetail>,
    val conditionReport: ConditionReport,
    val appraisalDocumentUrl: String?,
    val appraisalDocuments: List<AppraisalDocument>,
    val nearbySalesStats: List<NearbySalesStat>,
    val rightsAnalysis: List<RightsAnalysis>,
)

data class SalesDetail(
    val timeStamp: String,
    val type: String,
    val location: String,
    val leastSalesPrice: Long,
    val result: String,
)

data class SalesPicture(
    val sequence: Int,
    val imageUrl: String,
)

data class SalesBuilding(
    val siDoAddressName: String,
    val guAddressName: String,
    val dongAddressName: String,
    val riAddressName: String,
    val fullAddressName: String,
    val detailAddressName: String,
    val category: String,
)

data class SalesItemDetail(
    val sequence: Int,
    val type: String,
    val content: String,
)

data class ConditionReport(
    val investigationDate: String,
    val estateLeaseInfos: List<EstateLeaseInfo>,
    val occupationRelations: List<OccupationRelation>,
    val occupationRelationReports: List<OccupationRelationReport>,
)

data class EstateLeaseInfo(
    val sequence: Int,
    val address: String,
    val leaseRelation: String,
)

data class OccupationRelation(
    val address: String,
    val relation: String,
    val etc: String,
)

data class OccupationRelationReport(
    val sequence: Int,
    val address: String,
    val occupant: String,
    val relation: String,
    val occupiedPart: String,
    val purpose: String,
    val duration: String,
    val deposit: String,
    val rental: String,
    val movedAt: String,
    val confirmedAt: String,
)

data class AppraisalDocument(
    val sequence: Int,
    val title: String,
    val subTitle: String,
    val content: String,
)

data class NearbySalesStat(
    val term: String,
    val salesCount: Int,
    val averageAppraisalPrice: Long,
    val averageSalesPrice: Long,
    val salesPriceRate: Double,
    val averageFailBidCount: Double,
)

data class RightsAnalysis(
    val name: String,
    val role: String,
    val hasOppositionRight: String,
    val moveInReportDate: String,
    val occupationStatus: String,
    val priorityRepaymentRight: String,
    val fixedDate: String,
    val dividendRequest: String,
    val dividendRequestDate: String,
    val deposit: Long,
    val monthlyRent: Long,
)