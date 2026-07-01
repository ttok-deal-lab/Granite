package com.estateslug.slug.data.network.sales

import com.estateslug.slug.domain.court.CourtSalesItem
import com.estateslug.slug.domain.sales.CourtSaleDetail
import javax.inject.Inject

class RemoteSalesDataRepository @Inject constructor(
    private val courtSalesService: CourtSalesService,
) {
    suspend fun getCourtSales(
        ids: Array<String>
    ): Result<List<CourtSalesItem>> =
        runCatching {
            courtSalesService.getCourtSales(ids = ids)
                .map(CourtSalesResponseDTO::toDomain)
        }

    suspend fun getCourtSaleDetail(
        id: String
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