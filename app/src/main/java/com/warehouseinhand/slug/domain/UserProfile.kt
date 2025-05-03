package com.warehouseinhand.slug.domain

data class UserProfile(
    val id: Long,
    val email: String,
    val name: String,
    val provider: String,
    val status: String
) 