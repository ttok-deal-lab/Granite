package com.warehouseinhand.slug.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen() {//viewmodel
    val sectionName = "서울 관악구"
    val onSectionSelectClick: () -> Unit = {}
    val onSearchClick: () -> Unit = {}
    val onNotificationClick: () -> Unit = {}
    HomeTopBar(
        sectionName = sectionName,
        onSectionSelectClick = onSectionSelectClick,
        onSearchClick = onSearchClick,
        onNotificationClick = onNotificationClick,
    )
    // top sectino select
    //filter
    //countOfProduct sort
    //productList - lazy List
    //BottomSheet - filter setting
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}