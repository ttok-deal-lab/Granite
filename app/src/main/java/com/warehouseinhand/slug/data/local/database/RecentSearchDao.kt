package com.warehouseinhand.slug.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Query("SELECT keyword FROM recent_searches ORDER BY searchedAt DESC")
    fun getRecentSearchKeywords(): Flow<List<String>>

    @Upsert
    suspend fun upsertRecentSearch(entity: RecentSearchEntity)

    @Query("DELETE FROM recent_searches WHERE keyword = :keyword")
    suspend fun deleteByKeyword(keyword: String)

    @Query("DELETE FROM recent_searches")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM recent_searches")
    suspend fun getCount(): Int

    @Query("DELETE FROM recent_searches WHERE keyword IN (SELECT keyword FROM recent_searches ORDER BY searchedAt ASC LIMIT :count)")
    suspend fun deleteOldest(count: Int)
}
