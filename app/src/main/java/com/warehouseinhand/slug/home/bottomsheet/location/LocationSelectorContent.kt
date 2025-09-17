package com.warehouseinhand.slug.home.bottomsheet.location

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.warehouseinhand.slug.home.bottomsheet.BottomSheetHead
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.SlugTheme
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.noRippleClickable


@Composable
fun LocationSelectorContent(
    requestHideBottomSheet: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val selectedMain by viewModel.selectedMainLocation.collectAsStateWithLifecycle()
    val selectedSub by viewModel.selectedSubLocation.collectAsStateWithLifecycle()


    val onLocationSelected: (
        locationMain: Location.LocationMain,
        locationSub: Location.LocationSub
    ) -> Unit =
        { main, sub ->
            //SEND TO VIEWMODEL
            viewModel.requestChangeLocationChanged(mainLocation = main, subLocation = sub)
            requestHideBottomSheet()
        }


    LocationSelectorList(
        lastSelectedMain = selectedMain,
        lastSelectedSub = selectedSub,
        onLocationSelected
    )
}

@Composable
fun LocationSelectorList(
    lastSelectedMain: Location.LocationMain,
    lastSelectedSub: Location.LocationSub,
    onLocationSelected: (Location.LocationMain, Location.LocationSub) -> Unit
) {
    val filterName = stringResource(R.string.filter_header_location_select)

    var selectedMain: Location.LocationMain by remember { mutableStateOf(lastSelectedMain) }
    var selectedSub: Location.LocationSub by remember { mutableStateOf(lastSelectedSub) }
    val subListState: LazyListState = rememberLazyListState()

    val onMainClicked = { locationMain: Location.LocationMain ->
        selectedMain = locationMain
        selectedSub = Location.getSubLocationDefault(selectedMain)
        // 전국 선택 시 바로 종료
        if (locationMain == Location.LocationMain.ALL) {
            onLocationSelected(locationMain, Location.LocationSub.ALL.ALL)
        }
    }

    val onSubClicked = { locationSub: Location.LocationSub ->
        selectedSub = locationSub
        onLocationSelected(selectedMain, selectedSub)
    }

    //Main이 변경 되면 sub를 상위로 올림.
    LaunchedEffect(selectedMain) {
        subListState.animateScrollToItem(0)
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        BottomSheetHead(
            modifier = Modifier.padding(horizontal = 20.dp),
            string = filterName,
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(NeutralLight)
                .fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Location.LocationMain.entries.forEach { locationMain ->
                    LocationItem(
                        locationMain.getLocationString(),
                        isSelected = locationMain == selectedMain,
                        onClick = { onMainClicked(locationMain) })
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f), state = subListState
            ) {
                items(items = Location.getSubLocations(selectedMain)) { locationSub ->
                    LocationItem(
                        locationSub.getLocationString(),
                        isSelected = locationSub == selectedSub,
                        onClick = { onSubClicked(locationSub) })
                }
            }
        }
    }
}


@Composable
fun LocationItem(
    locationName: String, isSelected: Boolean, onClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> Neutral
            isPressed -> NeutralLight
            else -> NeutralInverted
        },
        animationSpec = tween(200),
        label = "backgroundColor"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) NeutralInverted else Neutral,
        animationSpec = tween(200),
        label = "textColor"
    )

    Box(
        Modifier
            .noRippleClickable(
                onClick = onClick,
                interactionSource = interactionSource
            )
            .background(color = backgroundColor)
            .padding(vertical = 12.dp, horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(locationName, style = SlugTypographyStyle.BodyLargeMedium, color = textColor)
    }
}

@Composable
@Preview
fun PreviewLocationItem() {
    var selectedLocation: Location by remember { mutableStateOf(Location.LocationMain.SEOUL) }
    val list = listOf(Location.LocationMain.SEOUL, Location.LocationMain.ALL)
    val onClick: (locationMain: Location.LocationMain) -> Unit = {
        selectedLocation = it
    }
    Column {
        list.forEach { locationMain: Location.LocationMain ->
            LocationItem(
                locationName = locationMain.displayName,
                isSelected = selectedLocation == locationMain,
                onClick = { onClick(locationMain) })
        }
    }

}

@Composable
@Preview(heightDp = 400)
fun PreviewLocationSelectorContent() {
    SlugTheme {
        Surface {
            LocationSelectorContent({})
        }
    }
}