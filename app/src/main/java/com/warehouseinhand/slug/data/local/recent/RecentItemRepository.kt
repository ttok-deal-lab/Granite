package com.warehouseinhand.slug.data.local.recent

import kotlinx.coroutines.flow.Flow

interface RecentItemRepository {

    /** LRU 로직을 적용하여 아이템 ID를 저장 */
    suspend fun addRecentItem(id: String)

    /** 저장된 아이템 ID 리스트를 Flow로 관찰 (index 0 = 최신) */
    fun getRecentItemIds(): Flow<List<String>>

    /** 저장된 리스트 전체 초기화 */
    suspend fun clearRecentItems()
}
