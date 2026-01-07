package com.warehouseinhand.slug.data.network.user

import com.google.gson.annotations.SerializedName
import com.warehouseinhand.slug.data.network.MapperToDomain
import com.warehouseinhand.slug.domain.SlugToken

//TODO : 현재 REFRESH 정보 없음 정책 확인 후 수정 할 것!
data class TokenDTO(
    @SerializedName("accessToken")
    val accessToken: String?,
//    @SerializedName("refreshToken")
//    val refreshToken: String?,
) : MapperToDomain<SlugToken> {
    override fun toDomain(): SlugToken =
        SlugToken(accessToken = accessToken?.removePrefix("Bearer ") ?: "",
//            refreshToken = refreshToken ?: ""
        )
}
