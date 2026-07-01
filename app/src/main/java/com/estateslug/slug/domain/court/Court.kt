package com.estateslug.slug.domain.court

data class Court(
    val code: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)
