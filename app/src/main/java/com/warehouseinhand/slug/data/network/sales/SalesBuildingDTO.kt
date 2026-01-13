package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName

data class SalesBuildingDTO(
    @SerializedName("siDoAddressName")
    val siDoAddressName: String,

    @SerializedName("guAddressName")
    val guAddressName: String,

    @SerializedName("dongAddressName")
    val dongAddressName: String,

    @SerializedName("riAddressName")
    val riAddressName: String?,

    @SerializedName("fullAddressName")
    val fullAddressName: String,

    @SerializedName("detailAddressName")
    val detailAddressName: String?,

    @SerializedName("category")
    val category: String
)