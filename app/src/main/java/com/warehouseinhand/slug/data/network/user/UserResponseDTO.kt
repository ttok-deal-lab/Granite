package com.warehouseinhand.slug.data.network.user

import com.google.gson.annotations.SerializedName
import com.warehouseinhand.slug.data.network.MapperToDomain
import com.warehouseinhand.slug.domain.user.UserProfile

data class UserResponseDTO(
    @SerializedName("user")
    val user: UserDTO,
    
    @SerializedName("token")
    val token: TokenDTO
)

data class UserDTO(
    @SerializedName("id")
    val id: Long,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("provider")
    val provider: String,

    @SerializedName("status")
    val status: String
) : MapperToDomain<UserProfile> {
    override fun toDomain(): UserProfile = UserProfile(
        name = name,
        email = email,
        id = id,
        provider = provider,
        status = status
    )
}