package com.warehouseinhand.slug.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey
    val keyword: String,
    val searchedAt: Long
)
