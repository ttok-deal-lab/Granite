package com.warehouseinhand.slug.data.network.search

import com.warehouseinhand.slug.domain.search.SearchResultPage
import com.warehouseinhand.slug.domain.user.GetFavoriteStatusUseCase
import com.warehouseinhand.slug.home.SearchQuery
import com.warehouseinhand.slug.util.CursorPage
import javax.inject.Inject

class RemoteSearchRepository @Inject constructor(
    private val searchService: SearchService,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
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
                soldOutStatus = query.soldOutStatus.name,
                minimumPrice = query.minimumPrice,
                maximumPrice = query.maximumPrice,
                nextCursor = if (nextCursor.isNullOrBlank()) "unknown" else nextCursor,
                sort = query.sort,
            ).toDomain()
        }

    suspend fun searchAutoComplete(keyword: String): Result<List<String>> =
        runCatching { searchService.searchAutoComplete(keyword) }

    suspend fun getProductListByCursorWithFavorites(
        nextCursor: String?,
        query: SearchQuery
    ): CursorPage<com.warehouseinhand.slug.domain.search.AuctionSearchItem> {
        val result = searchService.search(
            keyword = query.keyword,
            region = query.region,
            district = query.district,
            buildType = query.buildType.joinToString(",") { it.name },
            auctionFailCount = query.auctionFailCount.joinToString(",") { it.name },
            verificationStatus = query.verificationStatus.name,
            soldOutStatus = query.soldOutStatus.name,
            minimumPrice = query.minimumPrice,
            maximumPrice = query.maximumPrice,
            nextCursor = if (nextCursor.isNullOrBlank()) "unknown" else nextCursor,
            sort = query.sort,
        )

        val domainItems = result.auctionItemResponses.map { it.toDomain() }

        val ids = domainItems.map { it.id }
        val favoriteMap = getFavoriteStatusUseCase(ids).getOrElse { emptyMap() }

        val itemsWithFavorite = domainItems.map { item ->
            item.copy(isFavorite = favoriteMap[item.id] ?: false)
        }

        val nextKey = if (result.nextCursor == "unknown") null else result.nextCursor

        return CursorPage(
            items = itemsWithFavorite,
            nextCursor = nextKey,
            totalCount = result.searchHitCount,
        )
    }
}