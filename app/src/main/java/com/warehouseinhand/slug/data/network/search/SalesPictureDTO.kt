package com.warehouseinhand.slug.data.network.search

import com.google.gson.annotations.SerializedName

data class SalesPictureDTO(
    @SerializedName("sequence")
    val sequence: Int,

    @SerializedName("imageUrl")
    val imageUrl: String
)