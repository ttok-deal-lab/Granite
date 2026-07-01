package com.estateslug.slug.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal suspend fun <T> DataStore<Preferences>.getFromDataStore(key: Preferences.Key<T>): T? =
    data.map { preferences -> preferences[key] }.firstOrNull()

internal suspend fun DataStore<Preferences>.removeAllData(): Preferences =
    edit(MutablePreferences::clear)

internal suspend fun <T> DataStore<Preferences>.setDataStore(key: Preferences.Key<T>, value: T) =
    edit { preferences -> preferences[key] = value }

internal suspend fun <T> DataStore<Preferences>.setDataStore(
    key: Preferences.Key<T>,
    valueMap: (T?) -> T
) {
    edit { preferences ->
        val result = valueMap(preferences[key])
        preferences[key] = result
    }
}

internal suspend fun <T> DataStore<Preferences>.getStoredData(key: Preferences.Key<T>): Result<T> =
    kotlin.runCatching {
        getFromDataStore(key = key)
            ?: throw IllegalStateException("Stored data is not exist key = $key")
    }

internal suspend fun <T> DataStore<Preferences>.storeData(
    key: Preferences.Key<T>,
    value: T
): Result<Unit> =
    kotlin.runCatching { setDataStore(key = key, value = value) }
