package com.warehouseinhand.slug.home.bottomsheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.Gray150
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable

@Composable
fun FilterChip(
    chipText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color.Transparent else Gray150
    val shape = RoundedCornerShape(100.dp)
    val backgroundColor by animateColorAsState(
        if (isSelected) Neutral else NeutralInverted,
        label = "color"
    )
    val textColor by animateColorAsState(
        if (isSelected) NeutralInverted else Neutral,
        label = "color"
    )

    Box(
        modifier = Modifier
            .blockingClickable(onClick = onClick)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .background(color = backgroundColor, shape = shape)
            .padding(vertical = 10.dp, horizontal = 14.dp)
    ) {
        Text(
            text = chipText,
            color = textColor,
            style = SlugTypographyStyle.BodySmallMedium
        )
    }
}

@Composable
@Preview
fun PreviewFilterChip() {
    var isSelected by remember { mutableStateOf(false) }
    var isSelected2 by remember { mutableStateOf(true) }
    Surface(color = NeutralInverted) {
        Column(Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            FilterChip("아파트", isSelected, { isSelected = !isSelected })
            FilterChip("아파트", isSelected2, { isSelected2 = !isSelected2 })
        }
    }
}