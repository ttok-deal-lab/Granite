package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName

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
    val result: String
)