package com.warehouseinhand.slug.data.network.search

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("$CONTEXT/$VERSION/$SEARCH")
    suspend fun search(
        @Query("keyword") keyword: String? = null,
        @Query("region") region: Region? = Region.ALL,
        @Query("district") district: String? = null,
        @Query("buildType") buildType: BuildType? = BuildType.ALL,
        @Query("auctionFailCount") auctionFailCount: AuctionFailCount? = AuctionFailCount.ALL,
        @Query("varificationStatus") verificationStatus: VerificationStatus? = VerificationStatus.ALL,
        @Query("minimumPrice") minimumPrice: Long? = -1,
        @Query("maximumPrice") maximumPrice: Long? = -1,
        @Query("nextCursor") nextCursor: String? = null,
        @Query("sort") sort: Sort? = Sort.LATEST_REGISTERED,
//        @Query("keyword") keyword: String? = null,
//        @Query("region") region: Region? = Region.ALL,
//        @Query("district") district: String? = null,
//        @Query("buildType") buildType: BuildType? = BuildType.ALL,
//        @Query("auctionFailCount") auctionFailCount: AuctionFailCount? = AuctionFailCount.ALL,
//        @Query("verificationStatus") verificationStatus: VerificationStatus? = VerificationStatus.ALL,
//        @Query("minimumPrice") minimumPrice: Long? = -1,
//        @Query("maximumPrice") maximumPrice: Long? = -1,
//        @Query("nextCursor") nextCursor: String? = null,
//        @Query("sort") sort: Sort? = Sort.LATEST_REGISTERED,
    ): SearchResponseDTO

    companion object {
        private const val CONTEXT: String = "api/sherbet-api/api"
        private const val VERSION: String = "v2"
        private const val SEARCH: String = "search"

    }
}