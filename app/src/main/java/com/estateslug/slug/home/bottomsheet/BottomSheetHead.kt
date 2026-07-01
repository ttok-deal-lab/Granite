package com.estateslug.slug.home.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable


@Composable
fun BottomSheetHead(modifier: Modifier = Modifier, string: String) {
    Row {
        Box(
            modifier = modifier
                .weight(1f)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = string, style = SlugTypographyStyle.TitleLargeBold, color = Neutral)
        }
    }
}

@Composable
fun BottomSheetHeadRedo(modifier: Modifier = Modifier, string: String, onRedoClick: () -> Unit) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = string, style = SlugTypographyStyle.TitleLargeBold, color = Neutral)
        }
        Box(
            modifier = Modifier
                .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                .blockingClickable(onClick = onRedoClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_redo_18_18),
                contentDescription = "resetFilter",
                tint = Color(0xFF5E656E)
            )
        }
    }
}

@Composable
@Preview
fun PreviewBottomSheetHead() {
    Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        BottomSheetHead(string = "정렬")
        BottomSheetHeadRedo(string = "REDO!", onRedoClick = {})
    }
}