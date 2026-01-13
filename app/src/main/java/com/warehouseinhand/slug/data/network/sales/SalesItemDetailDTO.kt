package com.warehouseinhand.slug.data.network.sales

import com.google.gson.annotations.SerializedName

data class SalesItemDetailDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("content")
    val content: String
)