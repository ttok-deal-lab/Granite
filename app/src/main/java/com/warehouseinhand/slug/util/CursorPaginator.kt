package com.warehouseinhand.slug.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class CursorPage<T>(
    val items: List<T>,
    val nextCursor: String?,
    val totalCount: Long = 0L,
)

class CursorPaginator<T>(
    private val state: MutableStateFlow<CursorPaginationState<T>>,
    private val fetchPage: suspend (cursor: String?) -> CursorPage<T>,
    private val itemKey: ((T) -> Any)? = null,
) {
    suspend fun loadInitial() {
        state.update { CursorPaginationState(isInitialLoading = true) }
        try {
            val page = fetchPage(null)
            state.update {
                CursorPaginationState(
                    items = page.items,
                    nextCursor = page.nextCursor,
                    hasMore = page.nextCursor != null,
                    totalCount = page.totalCount,
                )
            }
        } catch (e: Exception) {
            state.update { CursorPaginationState(error = e) }
        }
    }

    suspend fun refresh() {
        if (state.value.items.isEmpty()) {
            loadInitial()
            return
        }
        state.update { it.copy(isRefreshing = true) }
        try {
            val page = fetchPage(null)
            state.update {
                val isSame = if (itemKey != null) {
                    it.items.map(itemKey) == page.items.map(itemKey)
                } else {
                    it.hasSameData(page)
                }
                if (isSame) {
                    it.copy(isRefreshing = false)
                } else {
                    CursorPaginationState(
                        items = page.items,
                        nextCursor = page.nextCursor,
                        hasMore = page.nextCursor != null,
                        totalCount = page.totalCount,
                    )
                }
            }
        } catch (e: Exception) {
            state.update { it.copy(isRefreshing = false, error = e) }
        }
    }

    suspend fun loadMore() {
        val current = state.value
        if (current.isLoadingMore || current.isInitialLoading || !current.hasMore) return

        state.update { it.copy(isLoadingMore = true) }
        try {
            val page = fetchPage(current.nextCursor)
            state.update {
                it.copy(
                    items = it.items + page.items,
                    nextCursor = page.nextCursor,
                    isLoadingMore = false,
                    hasMore = page.nextCursor != null,
                    totalCount = page.totalCount,
                )
            }
        } catch (e: Exception) {
            state.update { it.copy(isLoadingMore = false, error = e) }
        }
    }

    fun removeItem(predicate: (T) -> Boolean) {
        state.update {
            val filtered = it.items.filterNot(predicate)
            it.copy(
                items = filtered,
                totalCount = (it.totalCount - (it.items.size - filtered.size)).coerceAtLeast(0L),
            )
        }
    }
}
