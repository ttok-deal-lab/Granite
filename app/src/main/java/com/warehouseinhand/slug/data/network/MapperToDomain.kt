package com.warehouseinhand.slug.data.network

internal interface MapperToDomain<T> {
    fun toDomain(): T

    companion object {
        fun <T : MapperToDomain<R>, R> List<T>.toDomain(): List<R> =
            this.map(MapperToDomain<R>::toDomain)
    }
}