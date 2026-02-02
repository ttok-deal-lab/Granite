package com.warehouseinhand.slug.data.local.recent


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RecentItemModule {

    @Singleton
    @Binds
    abstract fun bindRecentItemRepository(
        impl: RecentItemRepositoryImpl
    ): RecentItemRepository
}
