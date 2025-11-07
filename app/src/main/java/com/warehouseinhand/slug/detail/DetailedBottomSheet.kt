package com.warehouseinhand.slug.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.component.button.basic.BasicButton
import com.warehouseinhand.slug.ui.component.button.basic.BasicButtonSizeType
import com.warehouseinhand.slug.ui.theme.Black200
import com.warehouseinhand.slug.ui.theme.Critical
import com.warehouseinhand.slug.ui.theme.CriticalLight
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralContrast
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedBottomSheet(detailBottomSheetType: DetailBottomSheetType, onDismiss: () -> Unit) {
    var isBottomSheetShowing = detailBottomSheetType != DetailBottomSheetType.EMPTY

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val coroutineScope = rememberCoroutineScope()
    val requestHideBottomSheet: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
            isBottomSheetShowing = false
            onDismiss()
        }
    }

    if (isBottomSheetShowing)
        ModalBottomSheet(
            onDismissRequest = requestHideBottomSheet,
            sheetState = sheetState,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )
            },
            content = {
                when (val sheetType = detailBottomSheetType) {
                    is DetailBottomSheetType.InfoSheetType -> {
                        InfoBottomSheet(
                            type = sheetType,
                            onDismissClicked = { requestHideBottomSheet() })
                    }

                    DetailBottomSheetType.EMPTY -> {

                    }

                    DetailBottomSheetType.Share -> {
                        TODO()
                    }
                }
            }
        )
}

@Composable
private fun InfoBottomSheet(
    type: DetailBottomSheetType.InfoSheetType,
    onDismissClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = type.info.name,
            style = SlugTypographyStyle.HeadingSmallBold,
            color = Neutral
        )
        Text(
            text = type.info.description,
            style = SlugTypographyStyle.BodyLargeRegular,
            color = Neutral
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            type.subInfo.forEach { subInfo ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = if (subInfo.isCritical) CriticalLight else NeutralLight
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = subInfo.name,
                        style = SlugTypographyStyle.BodyLargeBold,
                        color = if (subInfo.isCritical) Critical else Neutral,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subInfo.description,
                        style = SlugTypographyStyle.BodyMediumRegular,
                        color = NeutralContrast
                    )
                }
            }
        }
        Box(Modifier.padding(vertical = 16.dp)) {
            BasicButton(

                buttonText = stringResource(R.string.detailed_bottom_confirm),
                sizeType = BasicButtonSizeType.LARGE,
                onButtonClick = { onDismissClicked() }
            )
        }

    }
}

@Preview
@Composable
fun PreviewInfoBottomSheet() {
    Column {
        InfoBottomSheet(DetailBottomSheetType.InfoSheetType.TypeOfAuction, {

        })
    }
}


@Preview(device = Devices.PIXEL_XL)
@Composable
fun PreviewDetailedBottomSheet() {
    val typeList = remember {
        listOf<DetailBottomSheetType>(
            DetailBottomSheetType.InfoSheetType.TypeOfAuction,
            DetailBottomSheetType.InfoSheetType.RightToUseSite,
            DetailBottomSheetType.InfoSheetType.Superficies,
            DetailBottomSheetType.InfoSheetType.Lessee,
            DetailBottomSheetType.InfoSheetType.IllegalConstruction,
            DetailBottomSheetType.InfoSheetType.Creditor,
        )
    }
    var index by remember { mutableIntStateOf(0) }

    val selectedBottomSheetType by remember { derivedStateOf { typeList[index] } }
    Surface {
        Column(modifier = Modifier.systemBarsPadding()) {
            Box(
                Modifier
                    .background(color = Primary)
                    .clickable(onClick = {
                        index = (index + 1) % typeList.size
                    })
                    .padding(20.dp)
            ) {
                Text("TO NEXT", color = Black200)
            }
            DetailedBottomSheet(
                detailBottomSheetType = selectedBottomSheetType,
                onDismiss = {

                }
            )

        }
    }

}