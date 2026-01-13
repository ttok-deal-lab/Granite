package com.warehouseinhand.slug.data.network.search

import com.warehouseinhand.slug.data.network.WithAccessToken
import com.warehouseinhand.slug.data.network.user.UserPrivateService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SlugSearch {
    @Provides
    @Singleton
    fun providePrivateUserService(
            @WithAccessToken retrofit: Retrofit
    ): SearchService =
            retrofit.create(SearchService::class.java)
}
