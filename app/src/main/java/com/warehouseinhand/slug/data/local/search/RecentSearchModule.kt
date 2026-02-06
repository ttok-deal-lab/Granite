package com.warehouseinhand.slug.data.local.search

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RecentSearchModule {

    @Singleton
    @Binds
    abstract fun bindRecentSearchRepository(
        impl: RecentSearchRepositoryImpl
    ): RecentSearchRepository
}
