package com.warehouseinhand.slug.home.bottomsheet.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.AuctionStatusFilterType
import com.warehouseinhand.slug.home.FilterOption
import com.warehouseinhand.slug.home.bottomsheet.BottomSheetHeadRedo
import com.warehouseinhand.slug.home.bottomsheet.FilterChip
import com.warehouseinhand.slug.ui.component.button.basic.BasicButton
import com.warehouseinhand.slug.ui.component.button.basic.BasicTextButton
import com.warehouseinhand.slug.ui.component.button.basic.BasicButtonSizeType
import com.warehouseinhand.slug.ui.component.button.basic.BasicButtonStyle
import com.warehouseinhand.slug.ui.theme.NeutralLight


@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun <T : FilterOption> FilterContent(
    filterName: String,
    selectedOptions: List<T>,
    options: List<T>,
    onConfirmClicked: (List<T>) -> Unit,
    buttonText: String,
    onSelectionChanged: (List<T>) -> Unit = {},
    isLoading: Boolean = false,
) {
    val selectedStateList = remember { selectedOptions.toMutableStateList() }
    val onChipClicked = remember {
        { option: T ->
            if (selectedStateList.contains(option))
                selectedStateList.remove(option)
            else
                selectedStateList.add(option)
            onSelectionChanged(selectedStateList.toList())
        }
    }
    val onRedoClick: () -> Unit = remember {
        {
            selectedStateList.clear()
            onSelectionChanged(emptyList())
        }
    }

    Column {
        BottomSheetHeadRedo(
            modifier = Modifier.padding(horizontal = 20.dp),
            string = filterName,
            onRedoClick = onRedoClick
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(NeutralLight)
                .fillMaxWidth()
        )
        FlowRow(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    chipText = option.getDisplayText(),
                    isSelected = selectedStateList.contains(option),
                    onClick = { onChipClicked(option) }
                )
            }
        }
        Box(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {//TODO progress 넣을 방안고민
            BasicButton(
                buttonStyle = BasicButtonStyle.Fill.PRIMARY,
                sizeType = BasicButtonSizeType.LARGE,
                onButtonClick = { onConfirmClicked(selectedStateList) },
                content = { currentSize, currentColor ->
                    Row(
                        modifier = Modifier.absoluteOffset(x = if (isLoading) (-12).dp else 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = currentColor.text
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = buttonText,
                            style = currentSize.textStyle,
                            color = currentColor.text
                        )
                    }

                }
            )
        }
    }
}


@Preview
@Composable
fun PreviewFilterContent() {
    val filterName = stringResource(R.string.filter_header_auction_status) + "스트"
    val selectedOptions: List<FilterOption> = listOf(AuctionStatusFilterType.NEW) //from viewModel
    val options = AuctionStatusFilterType.entries
    val onConfirmClicked = {}
    FilterContent(
        filterName = filterName,
        selectedOptions = selectedOptions,
        options = options,
        onConfirmClicked = { onConfirmClicked() },
        buttonText = "1,200개 매물 보기",

        )
}
