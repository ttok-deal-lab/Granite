package com.estateslug.slug.data.local.search

import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {

    fun getRecentSearchKeywords(): Flow<List<String>>

    suspend fun addRecentSearch(keyword: String)

    suspend fun removeRecentSearch(keyword: String)

    suspend fun clearRecentSearches()
}
