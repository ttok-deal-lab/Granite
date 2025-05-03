package com.warehouseinhand.slug.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal class DataStoreModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
   internal annotation class DataStoreSettings
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal  annotation class DataStoreUser

    @Provides
    @Singleton
    @DataStoreSettings
    fun provideSettingsDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.settingsDataStore
    }

    @Provides
    @Singleton
    @DataStoreUser
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.userDataStore
    }

    companion object {

        private const val SETTINGS = "settings"
        private const val USER = "user"

        internal val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS)
        internal val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = USER)
    }
}