package com.warehouseinhand.slug.home.bottomsheet.sorting

import androidx.annotation.StringRes
import com.warehouseinhand.slug.R

enum class SortingType(@StringRes val localizedText: Int) {
    NEWEST(localizedText = R.string.sorting_type_newest),
    MOST_FAVORITE(localizedText = R.string.sorting_type_most_favorite),
    LEAST_DAY_LEFT(localizedText = R.string.sorting_type_least_day_left),
    LEAST_FAILED(localizedText = R.string.sorting_type_least_failed),
    PRICE_DESCENDING(localizedText = R.string.sorting_type_price_descending),
    PRICE_ASCENDING(localizedText = R.string.sorting_type_price_ascending),
}