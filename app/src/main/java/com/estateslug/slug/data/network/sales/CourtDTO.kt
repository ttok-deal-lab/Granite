package com.estateslug.slug.data.network.sales

import com.google.gson.annotations.SerializedName
import com.estateslug.slug.data.network.MapperToDomain
import com.estateslug.slug.domain.court.Court

data class CourtDTO(
    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,
) : MapperToDomain<Court> {
    override fun toDomain(): Court = Court(
        code = code,
        name = name,
        address = address,
        latitude = latitude,
        longitude = longitude,
    )
}
