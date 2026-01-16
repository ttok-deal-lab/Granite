package com.warehouseinhand.slug.data.network.sales

import com.warehouseinhand.slug.data.network.WithAccessToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SlugSalesData {

    @Provides
    @Singleton
    fun provideCourtSalesService(
        @WithAccessToken retrofit: Retrofit
    ): CourtSalesService =
        retrofit.create(CourtSalesService::class.java)
}