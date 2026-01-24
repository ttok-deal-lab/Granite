package com.warehouseinhand.slug.data.network.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.warehouseinhand.slug.domain.search.AuctionSearchItem

class ProductsByCursorPagingSource(
    private val service: SearchService,
    private val onSizeReturn: (Long) -> Unit,
    ) : PagingSource<String, AuctionSearchItem>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, AuctionSearchItem> =
        try {
            val cursor = params.key

            val dto = service.search(nextCursor = cursor)
            val domainItems = dto.auctionItemResponses.map { it.toDomain() } // DTO -> Domain
            onSizeReturn(dto.searchHitCount)
            val nextKey = if (cursor == "unknown") null else dto.nextCursor// null을 입력하면 더이상 받아오지 않음!

            LoadResult.Page(
                data = domainItems,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<String, AuctionSearchItem>): String? = null
}