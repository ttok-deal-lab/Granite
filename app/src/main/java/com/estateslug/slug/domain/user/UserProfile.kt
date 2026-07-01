package com.estateslug.slug.domain.user

data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val provider: String,
    val status: String
) 