package com.warehouseinhand.slug.data.network

internal interface MapperToDomain<T> {
    fun toDomain(): T

    companion object {
        internal fun <T> List<MapperToDomain<T>>.toDomain(): List<T> =
            map { it.toDomain() }
    }
}