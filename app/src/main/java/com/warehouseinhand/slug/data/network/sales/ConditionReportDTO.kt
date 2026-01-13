package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName

data class ConditionReportDTO(
    @SerializedName("investigationDate")
    val investigationDate: String,

    @SerializedName("estateLeaseInfos")
    val estateLeaseInfos: List<EstateLeaseInfoDTO>,

    @SerializedName("occupationRelations")
    val occupationRelations: List<OccupationRelationDTO>,

    @SerializedName("occupationRelationReports")
    val occupationRelationReports: List<OccupationRelationReportDTO>
)

data class EstateLeaseInfoDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("address")
    val address: String,

    @SerializedName("leaseRelation")
    val leaseRelation: String
)

data class OccupationRelationDTO(
    @SerializedName("address")
    val address: String,

    @SerializedName("relation")
    val relation: String,

    @SerializedName("etc")
    val etc: String?
)

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

    @SerializedName("deposit")
    val deposit: String, // 예시가 문자열이어서 String으로 안전하게

    @SerializedName("rental")
    val rental: String,  // 예시가 문자열이어서 String으로 안전하게

    @SerializedName("movedAt")
    val movedAt: String,

    @SerializedName("confirmedAt")
    val confirmedAt: String
)