package com.warehouseinhand.slug.home.bottomsheet.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.HomeViewModel
import com.warehouseinhand.slug.home.Price
import com.warehouseinhand.slug.home.bottomsheet.BottomSheetHead
import com.warehouseinhand.slug.home.component.SlugRangeSlider
import com.warehouseinhand.slug.ui.component.button.basic.BasicButton
import com.warehouseinhand.slug.ui.component.button.basic.BasicButtonSizeType
import com.warehouseinhand.slug.ui.component.button.basic.BasicButtonStyle
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.SlugTheme
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.numberToCurrency

@Composable
fun PriceRangeFilterContent(
    requestHideBottomSheet: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val valueOfItem by homeViewModel.tempProductSize.collectAsStateWithLifecycle()
    val minValue by homeViewModel.minPrice.collectAsStateWithLifecycle()
    val maxValue by homeViewModel.maxPrice.collectAsStateWithLifecycle()
    val lastPrice by homeViewModel.priceRange.collectAsStateWithLifecycle()
    var price: Price by remember { mutableStateOf(lastPrice) }

    LaunchedEffect(Unit) { homeViewModel.fetchTempCountFromTotal() }

    fun onRangeChanged(value: Pair<Long, Long>) {
        val result = when {
            value.first == minValue && value.second != maxValue ->
                Price.Below(value.second)

            value.second == maxValue && value.first != minValue ->
                Price.Above(value.first)

            else ->
                Price.Range(value.first, value.second)
        }
        price = result
        homeViewModel.updateTempPriceRange(result)
    }

    val onConfirmClicked = {
        homeViewModel.changePriceRange(price)
        requestHideBottomSheet()
    }

    PriceRangeFilter(
        valueOfItem = valueOfItem,
        minValue = minValue,
        maxValue = maxValue,
        price = price,
        onRangeChanged = ::onRangeChanged,
        onConfirmClicked = onConfirmClicked
    )

}


@Composable
fun <T : Price> PriceRangeFilter(
    valueOfItem: Long,
    minValue: Long,
    maxValue: Long,
    price: T,
    onRangeChanged: (Pair<Long, Long>) -> Unit,
    onConfirmClicked: () -> Unit,
) {
    val endString = "${numberToCurrency(maxValue)} 이상"//TODO i18n
    val startString = "${numberToCurrency(minValue)}원"//TODO i18n
    val buttonText = "${valueOfItem}개 매물보기"

    val rangeString = when {
        price is Price.Range -> {
            if (price.start == minValue && price.end == maxValue)
                stringResource(R.string.filter_price_all)
            else
                price.getDisplayText()
        }

        else ->
            price.getDisplayText()
    }


    fun percentageToValue(percentage: (Pair<Float, Float>)): Unit {
        val rangeOfValue = maxValue - minValue
        val changed: Pair<Long, Long> =
            Pair(
                minValue + (percentage.first * rangeOfValue).toLong(),
                minValue + (percentage.second * rangeOfValue).toLong()
            )
        onRangeChanged(changed)
    }

    val startValue = (when (price) {
        is Price.Range -> price.start
        is Price.Above -> price.value
        is Price.Below -> minValue
        else -> minValue
    }.toFloat() / maxValue)

    val endValue = (when (price) {
        is Price.Range -> price.end
        is Price.Below -> price.value
        is Price.Above -> maxValue
        else -> maxValue
    }.toFloat() / maxValue)
    val elementDefaultModifier = Modifier.padding(horizontal = 20.dp)
    Column {
        BottomSheetHead(
            modifier = elementDefaultModifier,
            string = stringResource(R.string.filter_price)
        )
        Spacer(modifier = Modifier.height(1.dp).background(NeutralLight).fillMaxWidth())
        Spacer(modifier = elementDefaultModifier.height(16.dp))
        Text(
            modifier = elementDefaultModifier,
            text = rangeString,
            style = SlugTypographyStyle.BodyLargeMedium
        )
        Spacer(modifier = elementDefaultModifier.height(12.dp))
        SlugRangeSlider(
            modifier = elementDefaultModifier,
            segments = 39,//TODO : segment 처리 어떻게 할까?
            startValue = startValue,
            endValue = endValue,
            onRangeChanged = ::percentageToValue,

            )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = elementDefaultModifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = startString,
                style = SlugTypographyStyle.BodyTinyMedium,
                color = NeutralSubtler
            )
            Text(
                text = endString,
                style = SlugTypographyStyle.BodyTinyMedium,
                color = NeutralSubtler
            )
        }
        Box(Modifier.padding(vertical = 16.dp)) {
            BasicButton(
                modifier = elementDefaultModifier,
                buttonText = buttonText,
                buttonStyle = BasicButtonStyle.Fill.PRIMARY,
                sizeType = BasicButtonSizeType.LARGE,
                onButtonClick = onConfirmClicked
            )
        }
    }
}


@Preview
@Composable
fun PreviewPriceRangeFilter() {

    val valueOfItem = 1_200L
    val minValue = 0L
    val maxValue = 2_000_000_000L
    var price: Price by remember {
        mutableStateOf(Price.Range(start = minValue, end = maxValue) as Price)
    }

    //preview만을 위한 것 price는 viewmodel에서 관리해줌.
    fun onRangeChanged(value: Pair<Long, Long>) {
        val result = when {
            value.first == minValue && value.second != maxValue ->
                Price.Below(value.second)

            value.second == maxValue && value.first != minValue ->
                Price.Above(value.first)

            else ->
                Price.Range(value.first, value.second)
        }
        price = result
    }

    SlugTheme {
        Surface {
            PriceRangeFilter(
                valueOfItem = valueOfItem,
                minValue = minValue,
                maxValue = maxValue,
                price = price,
                onRangeChanged = ::onRangeChanged,
                onConfirmClicked = { }
            )
        }
    }
}