package com.estateslug.slug.data.local.recent

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RecentItemRepositoryImpl @Inject constructor(
    private val dataStoreManager: RecentItemsDataStoreManager
) : RecentItemRepository {

    override suspend fun addRecentItem(id: String) {
        dataStoreManager.addRecentItem(id, maxSize = RECENT_ITEMS_MAX_SIZE)
    }

    override fun getRecentItemIds(): Flow<List<String>> =
        dataStoreManager.getRecentItemIds()

    override suspend fun clearRecentItems() {
        dataStoreManager.clearRecentItems()
    }
}
private const val RECENT_ITEMS_MAX_SIZE = 10
