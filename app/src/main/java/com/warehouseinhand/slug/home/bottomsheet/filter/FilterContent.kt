package com.warehouseinhand.slug.home.bottomsheet.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.bottomsheet.BottomSheetHead

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterContent(){
    val textId = R.string.filter_building
    Column {
        BottomSheetHead(stringResource(textId))
        FlowRow {  }
    }
}
@Composable
@Preview
fun PreviewFilterContent(){
    FilterContent()
}