package com.estateslug.slug.data.network.user

import com.google.gson.annotations.SerializedName

data class FcmTokenResponseDTO(
    @SerializedName("uuid")
    val uuid: Long,

    @SerializedName("deviceId")
    val deviceId: String,

    @SerializedName("pushServerType")
    val pushServerType: String,

    @SerializedName("pushToken")
    val pushToken: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,
)
