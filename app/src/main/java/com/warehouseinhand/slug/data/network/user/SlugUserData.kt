package com.warehouseinhand.slug.data.network.user

import com.warehouseinhand.slug.data.network.WithAccessToken
import com.warehouseinhand.slug.data.network.WithOutAccessToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SlugUserData {
    @Provides
    @Singleton
    fun providePublicUserService(
        @WithOutAccessToken retrofit: Retrofit
    ): UserPublicService =
        retrofit.create(UserPublicService::class.java)

//    @Provides
//    @Singleton
//    fun providePrivateUserService(
//        @WithAccessToken retrofit: Retrofit
//    ): UserPrivateService =
//        retrofit.create(UserPrivateService::class.java)
}