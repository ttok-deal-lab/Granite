package com.warehouseinhand.slug.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.AuctionStatusFilterType
import com.warehouseinhand.slug.home.BuildingFilterType
import com.warehouseinhand.slug.home.FilterOption
import com.warehouseinhand.slug.home.Price
import com.warehouseinhand.slug.home.ToggleFilterType
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.Gray150
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable

data class FilterButtonState(
    val isFilterSelected: Boolean,
    private val filterOptions: List<FilterOption>,
) {
    constructor(
        isFilterSelected: Boolean,
        filterOption: FilterOption,
    ) : this(
        isFilterSelected = isFilterSelected,
        filterOptions = listOf(filterOption)
    )

    val filterOption: FilterOption = filterOptions.first()

    @Composable
    fun getDisplayName(): String =
        if (filterOptions.size > 1)
            filterOption.getDisplayText() + " 외 ${filterOptions.size - 1}"
        else
            filterOption.getDisplayText()

    companion object{
        val defaultStateList = listOf(
            FilterButtonState(
                isFilterSelected = false,
                filterOption = ToggleFilterType.VERIFIED,
            ),
            FilterButtonState(
                isFilterSelected = false,
                filterOption = BuildingFilterType.APARTMENT,
            ),
            FilterButtonState(
                isFilterSelected = false,
                filterOption = AuctionStatusFilterType.NEW,
            ),
            FilterButtonState(
                isFilterSelected = false,
                filterOption = Price.Range(820_000_000, 820_000_000),
            ),
            FilterButtonState(
                isFilterSelected = false,
                filterOption = ToggleFilterType.FINISHED_PRODUCT,
            ),
        )
    }
}

@Composable
fun FilterButton(
    onClick: () -> Unit,
    state: FilterButtonState
) {
    val backgroundColor: Color = if (state.isFilterSelected) Neutral else Gray150
    val contentColor: Color = if (state.isFilterSelected) Gray150 else Neutral
    Row(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(100.dp))
            .blockingClickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (state.filterOption) {
            ToggleFilterType.VERIFIED -> VerifiedFilterButtonContent(
                contentColor = contentColor,
                state = state
            )

            is BuildingFilterType -> MultiSelectTypeFilterButtonContent(
                contentColor = contentColor,
                state = state
            )

            is AuctionStatusFilterType -> MultiSelectTypeFilterButtonContent(
                contentColor = contentColor,
                state = state
            )

            is Price -> MultiSelectTypeFilterButtonContent(
                contentColor = contentColor,
                state = state
            )

            else -> FilterButtonContent(
                contentColor = contentColor,
                state = state
            )
        }
    }
}

@Composable
fun VerifiedFilterButtonContent(
    contentColor: Color,
    state: FilterButtonState
) {
    ImageProcessor(
        modifier = Modifier.size(16.dp),
        imageResource = ImageResource.Id(R.drawable.verified_star_22_22)
    )
    Text(
        text = if (state.isFilterSelected) state.getDisplayName() else state.filterOption.getEmptyDisplayText(),
        style = SlugTypographyStyle.BodyMiniMedium,
        color = contentColor
    )
}

@Composable
fun FilterButtonContent(
    contentColor: Color,
    state: FilterButtonState
) {
    Text(
        text = if (state.isFilterSelected) state.getDisplayName() else state.filterOption.getEmptyDisplayText(),
        style = SlugTypographyStyle.BodyMiniMedium,
        color = contentColor
    )
}

//TODO : 차후 slot 시스템을 사용한 형태로 수정
//TODO :         text = if (state.isFilterSelected)state.getDisplayName() else state.filterOption.getEmptyDisplayText(), 부분 수정 고민
@Composable
fun MultiSelectTypeFilterButtonContent(
    contentColor: Color,
    state: FilterButtonState
) {
    Text(
        text = if (state.isFilterSelected) state.getDisplayName() else state.filterOption.getEmptyDisplayText(),
        style = SlugTypographyStyle.BodyMiniMedium,
        color = contentColor
    )
    Icon(
        modifier = Modifier.sizeIn(minWidth = 16.dp, minHeight = 16.dp),
        painter = painterResource(R.drawable.arrow_down_16_16),
        tint = contentColor,
        contentDescription = "arrow down for Select filter"
    )
}


@Composable
@Preview
fun PreviewFilterButton() {
    var isFilterSelected by remember { mutableStateOf(true) }
    val onClick: () -> Unit = {
        isFilterSelected = !isFilterSelected
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val filterOption = ToggleFilterType.VERIFIED
            val state = FilterButtonState(
                isFilterSelected = isFilterSelected,
                filterOptions = listOf(filterOption),
            )
            FilterButton(
                onClick = onClick,
                state = state.copy(isFilterSelected = !isFilterSelected)
            )
            FilterButton(
                onClick = onClick,
                state = state
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val filterOption = ToggleFilterType.FINISHED_PRODUCT
            val state = FilterButtonState(
                isFilterSelected = isFilterSelected,
                filterOptions = listOf(filterOption),
            )
            FilterButton(
                onClick = onClick,
                state = state.copy(isFilterSelected = !isFilterSelected),
            )
            FilterButton(
                onClick = onClick,
                state = state
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val filterOption = BuildingFilterType.APARTMENT
            val state = FilterButtonState(
                isFilterSelected = isFilterSelected,
                filterOptions = listOf(filterOption, filterOption),
            )
            FilterButton(
                onClick = onClick,
                state = state.copy(!isFilterSelected),
            )
            FilterButton(
                onClick = onClick,
                state = state,
            )
        }
    }

}