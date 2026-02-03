package com.warehouseinhand.slug.data.network.user

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.warehouseinhand.slug.domain.search.AuctionSearchItem

class FavoriteProductsByCursorPagingSource(
    private val service: UserPrivateService,
    private val userId: String,
) : PagingSource<String, AuctionSearchItem>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, AuctionSearchItem> =
        try {
            val cursor = params.key

            val dto =
                service.getUserFavoriteProductList(userId = userId, cursor = cursor ?: "unknown")

            val domainItems = dto.items.map {
                it.toDomain()
            } // DTO -> Domain

            val nextKey =
                if (cursor == "unknown") null else dto.nextCursor// null을 입력하면 더이상 받아오지 않음!

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