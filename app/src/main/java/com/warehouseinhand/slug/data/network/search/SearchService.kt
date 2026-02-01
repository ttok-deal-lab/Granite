package com.warehouseinhand.slug.data.network.search

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("$CONTEXT/$VERSION/$SEARCH")
    suspend fun search(
        @Query("keyword") keyword: String = "unknown",
        @Query("region") region: Region? = Region.ALL,
        @Query("district") district: String = "unknown",
        @Query("buildType") buildType: String = "ALL",
        @Query("auctionFailCount") auctionFailCount: String = "ALL",
        @Query("varificationStatus") verificationStatus: String = "ALL",
        @Query("minimumPrice") minimumPrice: Long? = -1,
        @Query("maximumPrice") maximumPrice: Long? = -1,
        @Query("nextCursor") nextCursor: String? = "unknown",
        @Query("sort") sort: Sort? = Sort.LATEST_REGISTERED,
    ): SearchResponseDTO

    companion object {
        private const val CONTEXT: String = "api/sherbet-api/api"
        private const val VERSION: String = "v2"
        private const val SEARCH: String = "search"

    }
}