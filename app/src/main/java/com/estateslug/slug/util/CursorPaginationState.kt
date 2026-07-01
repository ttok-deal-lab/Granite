package com.estateslug.slug.util

data class CursorPaginationState<T>(
    val items: List<T> = emptyList(),
    val nextCursor: String? = null,
    val isInitialLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val error: Throwable? = null,
    val totalCount: Long = 0L,
    val isRefreshing: Boolean = false,
) {
    val isEmpty: Boolean get() = items.isEmpty() && !isInitialLoading && error == null

    fun hasSameData(page: CursorPage<T>): Boolean =
        items == page.items && nextCursor == page.nextCursor && totalCount == page.totalCount
}
