package com.warehouseinhand.slug.data.network.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.warehouseinhand.slug.domain.search.AuctionSearchItem
import com.warehouseinhand.slug.domain.search.SearchResultPage
import com.warehouseinhand.slug.home.SearchQuery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteSearchRepository @Inject constructor(
    private val searchService: SearchService,
) {

    suspend fun getProductListByCursor(
        nextCursor: String?,
        query: SearchQuery
    ): Result<SearchResultPage> =
        runCatching {
            searchService.search(
                keyword = query.keyword,
                region = query.region,
                district = query.district,
                buildType = query.buildType.joinToString(",") {it.name},
                auctionFailCount = query.auctionFailCount.joinToString(",") {it.name},
                verificationStatus = query.verificationStatus.name,
                minimumPrice = query.minimumPrice,
                maximumPrice = query.maximumPrice,
                nextCursor = if (nextCursor.isNullOrBlank()) "unknown" else nextCursor,
                sort = query.sort,
            ).toDomain()
        }

    //TODO : USECASE로 이동 필요!
    fun getProductListPaging(
        size: Int = DEFAULT_PAGING_SIZE,
        query: SearchQuery,
        onSizeReturn: (Long) -> Unit,
        ): Flow<PagingData<AuctionSearchItem>> =
        Pager(
            config = PagingConfig(
                pageSize = size,
                initialLoadSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductsByCursorPagingSource(
                    service = searchService,
                    query = query,
                    onSizeReturn = onSizeReturn
                )
            }
        ).flow

}

val DEFAULT_PAGING_SIZE = 20