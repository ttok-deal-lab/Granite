package com.estateslug.slug.domain.sales

/** 최근 실거래 — 서버 recentTransaction 중첩 객체의 도메인 표현. */
data class RecentTransaction(
    val price: Long,
    val date: String,
) {
    val isNone: Boolean get() = this == NONE

    companion object {
        /** 실거래 없음(서버 recentTransaction = null) 정규화 값 — nullable 대신 상수 객체로 다룬다 */
        val NONE = RecentTransaction(price = 0L, date = "")
    }
}
