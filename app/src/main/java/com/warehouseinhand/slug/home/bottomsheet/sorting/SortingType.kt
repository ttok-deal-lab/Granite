package com.warehouseinhand.slug.home.bottomsheet.sorting

import androidx.annotation.StringRes
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.data.network.search.Sort

enum class SortingType(@StringRes val localizedText: Int) {
    NEWEST(localizedText = R.string.sorting_type_newest),
    MOST_FAVORITE(localizedText = R.string.sorting_type_most_favorite),
    LEAST_DAY_LEFT(localizedText = R.string.sorting_type_least_day_left),
    LEAST_FAILED(localizedText = R.string.sorting_type_least_failed),
    PRICE_DESCENDING(localizedText = R.string.sorting_type_price_descending),
    PRICE_ASCENDING(localizedText = R.string.sorting_type_price_ascending),;

    fun toSort():Sort = when(this){
        NEWEST -> Sort.LATEST_REGISTERED
        MOST_FAVORITE -> Sort.MOST_INTERESTED
        LEAST_DAY_LEFT -> Sort.DEADLINE_SOON
        LEAST_FAILED -> Sort.LEAST_FAILED
        PRICE_DESCENDING -> Sort.PRICE_HIGH
        PRICE_ASCENDING -> Sort.PRICE_LOW
    }
}