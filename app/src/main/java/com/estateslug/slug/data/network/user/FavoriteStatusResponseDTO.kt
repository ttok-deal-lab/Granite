package com.estateslug.slug.data.network.user

import com.google.gson.annotations.SerializedName
import com.estateslug.slug.data.network.MapperToDomain
import com.estateslug.slug.domain.user.FavoriteStatus

data class FavoriteStatusResponseDTO(
    @SerializedName("productId")
    val productId: String,

    @SerializedName("isFavorite")
    val isFavorite: Boolean
): MapperToDomain<FavoriteStatus>{
    override fun toDomain(): FavoriteStatus =
        FavoriteStatus(
            productId = productId,
            isFavorite = isFavorite
        )
}

