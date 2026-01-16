package com.warehouseinhand.slug.data.network.sales

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CourtSalesService {

    /**
     * 경매 목록 조회 V2
     * 조건에 맞는 경매 목록을 조회합니다.
     */
    @GET("$CONTEXT/$VERSION/$COURTS/$SALES")
    suspend fun getCourtSales(
        @Query("ids") ids: List<String>
    ): List<CourtSalesResponseDTO>

    @GET("$CONTEXT/$VERSION/$COURTS/$SALES/{$ID}")
    suspend fun getCourtSaleDetail(
        @Path(ID) id: String
    ): CourtSaleDetailResponseDTO


    @GET("$CONTEXT/$VERSION/$COURTS/$SALES/$ALL")
    suspend fun getCourtSalesAll(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 20,
    ): CourtSalesCursorResponseDTO
    companion object {
        private const val CONTEXT: String = "api/sherbet-api"

        private const val VERSION: String = "v2"
        private const val COURTS: String = "courts"
        private const val SALES: String = "sales"
        private const val ALL: String = "all"
        private const val ID: String = "id"
    }
}