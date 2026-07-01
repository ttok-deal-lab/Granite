package com.estateslug.slug.domain.court

data class CourtSalesCursorPage(
    val items: List<CourtSalesSummary>,
    val nextCursor: String?,
    val hasNext: Boolean
)
