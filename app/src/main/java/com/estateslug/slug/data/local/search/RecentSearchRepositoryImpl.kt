package com.estateslug.slug.data.local.search

import com.estateslug.slug.data.local.database.RecentSearchDao
import com.estateslug.slug.data.local.database.RecentSearchEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RecentSearchRepositoryImpl @Inject constructor(
    private val recentSearchDao: RecentSearchDao
) : RecentSearchRepository {

    override fun getRecentSearchKeywords(): Flow<List<String>> =
        recentSearchDao.getRecentSearchKeywords()

    override suspend fun addRecentSearch(keyword: String) {
        recentSearchDao.upsertRecentSearch(
            RecentSearchEntity(
                keyword = keyword,
                searchedAt = System.currentTimeMillis()
            )
        )
        val count = recentSearchDao.getCount()
        if (count > MAX_RECENT_SEARCHES) {
            recentSearchDao.deleteOldest(count - MAX_RECENT_SEARCHES)
        }
    }

    override suspend fun removeRecentSearch(keyword: String) {
        recentSearchDao.deleteByKeyword(keyword)
    }

    override suspend fun clearRecentSearches() {
        recentSearchDao.deleteAll()
    }
}

private const val MAX_RECENT_SEARCHES = 100
