package com.warehouseinhand.slug.home.bottomsheet.sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.bottomsheet.BottomSheetHead
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable

@Composable
fun SortingTypeItem(
    type: SortingType,
    lastSelectedType: SortingType,
    onClick: (SortingType) -> Unit
) {
    val isSelected = type == lastSelectedType
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backGroundColor by
    remember { derivedStateOf { if (isPressed) NeutralLight else NeutralInverted } }

    Row(
        Modifier
            .blockingClickable(
                onClick = { onClick(type) },
                interactionSource = interactionSource,
            )
            .background(color = backGroundColor)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(type.localizedText),
            style = SlugTypographyStyle.BodyLargeMedium,
            color = Neutral
        )
        Spacer(Modifier.weight(1f))
        if (isSelected)
            Icon(
                painterResource(R.drawable.ic_check_20_20),
                tint = Primary,
                contentDescription = "선택됨"
            )
    }
}

@Composable

fun SortBottomSheetContent(
    lastSelected: SortingType,
    onClick: (SortingType) -> Unit
) {
    val itemList = SortingType.entries
    Column {
        BottomSheetHead(stringResource(R.string.sorting_head))
        itemList.forEach { type ->
            SortingTypeItem(
                type = type,
                lastSelectedType = lastSelected,
                onClick = onClick
            )
        }
    }
}

@Composable
@Preview
fun PreviewSortTypeItem() {
    val type: SortingType = SortingType.NEWEST
    val onClick: (SortingType) -> Unit = {}
    SortingTypeItem(
        type = type,
        lastSelectedType = SortingType.NEWEST,
        onClick = onClick
    )
}

@Composable
@Preview
fun PreviewSortBottomSheet() {
    var lastSelected: SortingType by remember { mutableStateOf(SortingType.NEWEST) }
    val onClick: (SortingType) -> Unit = {
        lastSelected = it
    }
    SortBottomSheetContent(
        lastSelected = lastSelected,
        onClick = onClick
    )
}

