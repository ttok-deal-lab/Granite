package com.estateslug.slug.domain.search

data class SearchResultPage(
    val totalCount: Long,
    val items: List<AuctionSearchItem>,
    val nextCursor: String?
)
