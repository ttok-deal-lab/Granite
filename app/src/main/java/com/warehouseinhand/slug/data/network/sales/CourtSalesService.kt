package com.warehouseinhand.slug.data.network.sales

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CourtSalesService {

    /**
     * 경매 목록 조회 V2
     * 조건에 맞는 경매 목록을 조회합니다.
     */
    @GET("$VERSION/courts/sales")
    suspend fun getCourtSales(
        @Query("ids") ids: List<Long>
    ): List<CourtSalesResponseDTO>

    @GET("$VERSION/courts/sales/{$ID}")
    suspend fun getCourtSaleDetail(
        @Path(ID) id: Long
    ): CourtSaleDetailResponseDTO


    @GET("$VERSION/courts/sales/all")
    suspend fun getCourtSalesAll(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 20,
    ): CourtSalesCursorResponseDTO
    companion object {
        private const val VERSION: String = "v2"
        private const val ID: String = "id"
    }
}