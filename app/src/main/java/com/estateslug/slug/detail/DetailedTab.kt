package com.estateslug.slug.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.component.tab.LineHugTab


@Composable
fun DetailedTabRow(selectedRoute: DetailedRoute, onTabClicked: (DetailedRoute) -> Unit) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(selectedRoute) {
        lazyListState.animateScrollToItem(selectedRoute.period)
    }
    val onTabClick: (DetailedRoute) -> Unit = { route -> onTabClicked(route) }
    LazyRow(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        state = lazyListState
    ) {
        items(items = DetailedRoute.entries) { route ->
            LineHugTab(
                tabName = route.name,
                isSelected = route == selectedRoute,
                onTabClick = { onTabClick(route) }
            )
        }
    }
}


@Composable
@Preview(device = Devices.PIXEL_XL)
fun PreviewTabLineHugRow() {

    var selectedRoute: DetailedRoute by remember { mutableStateOf(DetailedRoute.AuctionInfo) }
    Box(modifier = Modifier.systemBarsPadding()) {
        DetailedTabRow(
            selectedRoute = selectedRoute,
            onTabClicked = { selectedRoute = it })
    }
}