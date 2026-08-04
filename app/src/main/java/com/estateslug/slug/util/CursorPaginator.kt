package com.estateslug.slug.util

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
    /** @return 성공 여부 — 호출자가 실패 시 재시도 플래그를 유지할 수 있게 한다 */
    suspend fun loadInitial(): Boolean {
        state.update { CursorPaginationState(isInitialLoading = true) }
        return try {
            val page = fetchPage(null)
            state.update {
                CursorPaginationState(
                    items = page.items,
                    nextCursor = page.nextCursor,
                    hasMore = page.nextCursor != null,
                    totalCount = page.totalCount,
                )
            }
            true
        } catch (e: Exception) {
            state.update { CursorPaginationState(error = e) }
            false
        }
    }

    /** @return 성공 여부 */
    suspend fun refresh(): Boolean {
        if (state.value.items.isEmpty()) {
            return loadInitial()
        }
        state.update { it.copy(isRefreshing = true) }
        return try {
            val page = fetchPage(null)
            state.update {
                val isSame = if (itemKey != null) {
                    it.items.map(itemKey) == page.items.map(itemKey)
                } else {
                    it.hasSameData(page)
                }
                if (isSame) {
                    it.copy(isRefreshing = false, error = null)
                } else {
                    CursorPaginationState(
                        items = page.items,
                        nextCursor = page.nextCursor,
                        hasMore = page.nextCursor != null,
                        totalCount = page.totalCount,
                    )
                }
            }
            true
        } catch (e: Exception) {
            state.update { it.copy(isRefreshing = false, error = e) }
            false
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
                    error = null,
                )
            }
        } catch (e: Exception) {
            state.update { it.copy(isLoadingMore = false, error = e) }
        }
    }

    fun prependItem(item: T) {
        state.update {
            it.copy(
                items = listOf(item) + it.items,
                totalCount = it.totalCount + 1,
            )
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
