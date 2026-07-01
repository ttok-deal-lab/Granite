package com.estateslug.slug.data.network

import com.estateslug.slug.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SlugRetrofit {
    @Singleton
    @Provides
    @WithOutAccessToken
    fun provideRetrofitWithOutAccessToken(
        @WithOutAccessToken okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @WithAccessToken
    fun provideRetrofitWithAccessToken(
        @WithAccessToken okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    companion object{
        const val BASE_URL: String = BuildConfig.BASE_URL
        const val AUTH_CONTEXT: String = "api/sherbet-auth"
    }
}

