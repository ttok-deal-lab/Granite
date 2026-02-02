package com.warehouseinhand.slug.data.local.recent

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.warehouseinhand.slug.data.local.DataStoreModule.DataStoreRecentItems
import com.warehouseinhand.slug.data.local.setDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val RECENT_ITEM_IDS_KEY = stringPreferencesKey("recent_item_ids")

internal class RecentItemsDataStoreManager @Inject constructor(
    @DataStoreRecentItems private val dataStore: DataStore<Preferences>
) {

    /** 저장된 ID 리스트를 Flow로 관찰. 읽기 실패 시 빈 리스트 폴백 */
    fun getRecentItemIds(): Flow<List<String>> =
        dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                prefs[RECENT_ITEM_IDS_KEY]
                    ?.let { Json.decodeFromString<List<String>>(it) }
                    ?: emptyList()
            }

    /**
     * LRU 로직 적용 후 저장.
     * setDataStore(key, valueMap) 덕분에 read-modify-write가 단일 edit 블록 안에서 처리됨
     */
    suspend fun addRecentItem(id: String, maxSize: Int) {
        dataStore.setDataStore(RECENT_ITEM_IDS_KEY) { current ->
            val currentList = current
                ?.let { Json.decodeFromString<List<String>>(it) }
                ?: emptyList()

            Log.d("TESTTEST", "addRecentItem: $id")
            buildList {
                add(id)                                        // 새 아이템을 앞에
                addAll(currentList.filter { it != id })        // 중복 제거
            }
                .take(maxSize)                                 // MAX 초과 시 trim
                .also {
                    Log.d("TESTTEST", "addRecentItem: list ${it.joinToString(separator = ", ")}")
                }
                .let { Json.encodeToString(value = it) }
        }
    }

    /** 저장된 리스트 초기화 */
    suspend fun clearRecentItems() {
        dataStore.setDataStore(RECENT_ITEM_IDS_KEY, Json.encodeToString(emptyList<String>()))
    }
}
