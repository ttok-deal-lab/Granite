package com.warehouseinhand.slug.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.util.numberToCurrency


interface FilterOption {
    @Composable
    fun getDisplayText(): String

    @Composable
    fun getEmptyDisplayText(): String
}

enum class ToggleFilterType(@StringRes val stringId: Int) : FilterOption {
    VERIFIED(R.string.filter_verified),
    FINISHED_PRODUCT(R.string.filter_finished), ;

    @Composable
    override fun getDisplayText(): String = stringResource(stringId)

    @Composable
    override fun getEmptyDisplayText() = getDisplayText()
}

enum class BuildingFilterType(@StringRes val stringId: Int) : FilterOption {
    APARTMENT(R.string.filter_building_apartment),
    VILLA(R.string.filter_building_villa),
    OFFICETEL(R.string.filter_building_officetel),
    COMMERCIAL_HOUSE(R.string.filter_building_commercial_house),
    HOUSE(R.string.filter_building_house),
    OTHER(R.string.filter_building_other), ;

    @Composable
    override fun getDisplayText(): String = stringResource(stringId)

    @Composable
    override fun getEmptyDisplayText() = stringResource(R.string.filter_building)

}

enum class AuctionStatusFilterType(@StringRes val stringId: Int) : FilterOption {
    NEW(R.string.filter_auction_status_new),
    DATE_CHANGING(R.string.filter_auction_status_date_changing),
    FAILED1(R.string.filter_auction_status_failed1),
    FAILED2(R.string.filter_auction_status_failed2),
    FAILED_ABOVE3(R.string.filter_auction_status_failed_above3), ;

    @Composable
    override fun getDisplayText(): String = stringResource(stringId)

    @Composable
    override fun getEmptyDisplayText() = stringResource(R.string.filter_auction_status)
}

sealed class Price() : FilterOption {

    @Composable
    override fun getEmptyDisplayText() = stringResource(R.string.filter_price)

    data class Below(val value: Long) :
        Price() {
        @Composable
        override fun getDisplayText(): String =
            numberToCurrency(value) + stringResource(R.string.filter_price_below)
    }

    data class Above(val value: Long) :
        Price() {
        @Composable
        override fun getDisplayText(): String =
            numberToCurrency(value) + stringResource(R.string.filter_price_above)
    }

    data class Range(val start: Long, val end: Long) :
        Price() {
        @Composable
        override fun getDisplayText(): String =
            buildString {
                append(numberToCurrency(start))
                append(stringResource(R.string.filter_price_above))
                append(" ~ ")
                append(numberToCurrency(end))
                append(stringResource(R.string.filter_price_below))
            }
    }
}