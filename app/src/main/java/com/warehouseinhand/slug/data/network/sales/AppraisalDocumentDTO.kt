package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName

data class AppraisalDocumentDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("subTitle")
    val subTitle: String,

    @SerializedName("content")
    val content: String // HTML
)

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
    val salesPriceRate: Int,

    @SerializedName("averageFailBidCount")
    val averageFailBidCount: Double
)

data class RightsAnalysisDTO(
    @SerializedName("name")
    val name: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("hasOppositionRight")
    val hasOppositionRight: String,

    @SerializedName("moveInReportDate")
    val moveInReportDate: String?,

    @SerializedName("occupationStatus")
    val occupationStatus: String,

    @SerializedName("priorityRepaymentRight")
    val priorityRepaymentRight: String,

    @SerializedName("fixedDate")
    val fixedDate: String?,

    @SerializedName("dividendRequest")
    val dividendRequest: String,

    @SerializedName("dividendRequestDate")
    val dividendRequestDate: String?,

    @SerializedName("deposit")
    val deposit: Long,

    @SerializedName("monthlyRent")
    val monthlyRent: Long
)