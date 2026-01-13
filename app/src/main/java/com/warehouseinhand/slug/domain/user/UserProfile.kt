package com.warehouseinhand.slug.domain.user

data class UserProfile(
    val id: Long,
    val email: String,
    val name: String,
    val provider: String,
    val status: String
) 