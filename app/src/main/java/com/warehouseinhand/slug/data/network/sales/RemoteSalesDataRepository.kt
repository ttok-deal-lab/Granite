package com.warehouseinhand.slug.data.network.sales

import com.warehouseinhand.slug.domain.court.CourtSalesItem
import javax.inject.Inject

class RemoteSalesDataRepository @Inject constructor(
    private val courtSalesService: CourtSalesService,
) {
    suspend fun getCourtSales(
        ids: List<Long>
    ): Result<List<CourtSalesItem>> =
        runCatching {
            courtSalesService.getCourtSales(ids = ids)
                .map(CourtSalesResponseDTO::toDomain)
        }

    suspend fun getCourtSaleDetail(
        id: Long
    ): Result<CourtSaleDetail> =
        runCatching {
            courtSalesService.getCourtSaleDetail(id)
        }.mapCatching {
            it.toDomain()
        }

    suspend fun getCourtSalesAll(
        cursor: String?,
        size: Int = 20
    ): Result<CourtSalesCursorResponseDTO> =
        runCatching {
            courtSalesService.getCourtSalesAll(cursor = cursor, size = size)
        }
}