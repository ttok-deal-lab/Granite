package com.estateslug.slug.data.network.user

import com.google.gson.annotations.SerializedName
import com.estateslug.slug.data.network.search.AuctionItemResponseDTO

data class FavoriteSalesCursorResponseDTO(
    @SerializedName("auctionItemResponses")
    val items: List<AuctionItemResponseDTO>,

    @SerializedName("nextCursor")
    val nextCursor: String?,

)

