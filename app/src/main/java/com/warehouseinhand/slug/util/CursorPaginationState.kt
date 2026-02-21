package com.warehouseinhand.slug.util

data class CursorPaginationState<T>(
    val items: List<T> = emptyList(),
    val nextCursor: String? = null,
    val isInitialLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val error: Throwable? = null,
    val totalCount: Long = 0L,
) {
    val isEmpty: Boolean get() = items.isEmpty() && !isInitialLoading && error == null
}
