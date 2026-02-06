package com.warehouseinhand.slug.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentSearchEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}
