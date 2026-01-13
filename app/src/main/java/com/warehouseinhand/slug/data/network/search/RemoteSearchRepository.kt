package com.warehouseinhand.slug.data.network.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.warehouseinhand.slug.domain.search.AuctionSearchItem
import com.warehouseinhand.slug.domain.search.SearchResultPage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteSearchRepository @Inject constructor(
    private val searchService: SearchService,
) {

    suspend fun getProductListByCursor(
        nextCursor: String?,
    ): Result<SearchResultPage> =
        runCatching {
            searchService.search(
                keyword = "unknown",
                nextCursor = if (nextCursor.isNullOrBlank()) "unknown" else nextCursor,
                district = "unknown"
            ).toDomain()
        }

    //TODO : USECASE로 이동 필요!
    fun getProductListPaging(size: Int = DEFAULT_PAGING_SIZE): Flow<PagingData<AuctionSearchItem>> =
        Pager(
            config = PagingConfig(
                pageSize = size,
                initialLoadSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductsByCursorPagingSource(
                    service = searchService,
                )
            }
        ).flow

}

val DEFAULT_PAGING_SIZE = 20