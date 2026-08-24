package com.estateslug.slug.util

import java.text.NumberFormat
import java.util.Locale

// Elasticsearch total hits 상한(track_total_hits 기본 10,000)에 걸리면 정확한 총계가 아니므로
// 상한 이상은 "9,999+"로 표기해 초과분이 있음을 드러낸다
private const val MAX_DISPLAY_PRODUCT_COUNT = 9_999L

/** 매물 수 표시 공통 규칙: 천단위 구분자 + ES 상한 초과 시 "9,999+" */
fun formatProductCount(count: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return if (count > MAX_DISPLAY_PRODUCT_COUNT) {
        "${formatter.format(MAX_DISPLAY_PRODUCT_COUNT)}+"
    } else {
        formatter.format(count)
    }
}
