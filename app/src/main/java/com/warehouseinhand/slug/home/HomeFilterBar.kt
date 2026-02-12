package com.warehouseinhand.slug.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.component.FilterButton
import com.warehouseinhand.slug.home.component.FilterButtonState
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.skeleton.shimmerEffect
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeFilterBar(
    verifiedProductExist: Boolean,
    numberOfProduct: Long,
    sortTypeName: String,
    stateList: List<FilterButtonState>,
    onSortingClick: () -> Unit,
    onFilterClick: (FilterOption) -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FilterRow(
            stateList = stateList,
            onClick = onFilterClick
        )
        CountAndSort(
            numberOfProduct = numberOfProduct,
            sortTypeName = sortTypeName,
            onSortingClick = onSortingClick,
            isLoading = isLoading,
        )
        if (verifiedProductExist)
            VerifiedProductAnnounce()
    }
}

@Composable
fun FilterRow(stateList: List<FilterButtonState>, onClick: (FilterOption) -> Unit) {
    Row(
        Modifier
            .padding(vertical = 12.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.width(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            stateList.forEach { state ->
                FilterButton(
                    onClick = { onClick(state.filterOption) },
                    state = state
                )
            }
        }
        Spacer(Modifier.width(20.dp))
    }
}

@Composable
fun CountAndSort(
    isLoading: Boolean,
    numberOfProduct: Long,
    sortTypeName: String,
    onSortingClick: () -> Unit
) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.KOREA) }
    val formattedNumber = remember(numberOfProduct) { formatter.format(numberOfProduct) }
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .sizeIn(minHeight = 42.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.animateContentSize(), verticalAlignment = Alignment.CenterVertically){
            Text(
                "상품",
                style = SlugTypographyStyle.BodyMicroMedium,
                color = Neutral
            )
            Spacer(modifier = Modifier.width(4.dp))
            if (isLoading)
                Box(
                    modifier = Modifier
                        .width(35.dp)
                        .height(15.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            else
                Text(
                    formattedNumber,
                    style = SlugTypographyStyle.BodyMicroBold,
                    color = Neutral
                )
        }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .blockingClickable(onClick = onSortingClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                sortTypeName,
                style = SlugTypographyStyle.BodyMicroMedium,
                color = Neutral
            )
            ImageProcessor(
                modifier = Modifier.size(22.dp),
                imageResource = ImageResource.Id(R.drawable.arrow_updown_16_16)
            )
        }
    }
}

@Composable
fun VerifiedProductAnnounce() {
    Column(
        modifier = Modifier
            .background(color = NeutralLight)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ImageProcessor(
                modifier = Modifier.size(22.dp),
                imageResource = ImageResource.Id(R.drawable.verified_star_22_22)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "인증매물",
                style = SlugTypographyStyle.BodyMediumBold,
                color = Primary
            )
            Text(
                text = "이란?",
                style = SlugTypographyStyle.BodyMediumBold,
                color = Neutral
            )
        }
        Spacer(Modifier.height(4.dp))
        Row {
            Text(
                text = "임장보고서가 존재해 정확한 정보를 확인할 수 있어요.",
                style = SlugTypographyStyle.BodyMiniMedium,
                color = NeutralSubtler
            )
        }
    }
}


@Composable
@Preview
fun PreviewHomeFilterBar() {
    val stateList: List<FilterButtonState> = listOf(
        FilterButtonState(
            isFilterSelected = true,
            filterOption = ToggleFilterType.VERIFIED,
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = BuildingFilterType.APARTMENT,
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = AuctionStatusFilterType.NEW,
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = Price.Range(820_000_000, 820_000_000),
        ),
        FilterButtonState(
            isFilterSelected = true,
            filterOption = ToggleFilterType.FINISHED_PRODUCT,
        ),
    )

    val verifiedProductExist = true
    val numberOfProduct: Long = 13894
    val sortTypeName: String = "최신 등록순"
    val onSortingClick: () -> Unit = { }
    val onFilterClick: (FilterOption) -> Unit = { }
    HomeFilterBar(
        verifiedProductExist = verifiedProductExist,
        numberOfProduct = numberOfProduct,
        sortTypeName = sortTypeName,
        stateList = stateList,
        onSortingClick = onSortingClick,
        onFilterClick = onFilterClick,
        isLoading = true,
    )
}