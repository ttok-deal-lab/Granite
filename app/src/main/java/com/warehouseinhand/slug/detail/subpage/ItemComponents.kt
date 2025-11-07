package com.warehouseinhand.slug.detail.subpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle

@Composable
internal fun ItemTitle(title: String) {
    Text(text = title, style = SlugTypographyStyle.TitleMediumBold, color = Neutral)
}

@Composable
internal fun ItemInfo(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = name, style = SlugTypographyStyle.BodyMediumMedium, color = NeutralSubtler)
        Text(text = value, style = SlugTypographyStyle.BodyMediumMedium, color = Neutral)
    }
}

@Composable
@Preview(device = Devices.PIXEL_XL)
fun PreviewItemTitle() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ItemTitle("건물 상세정보")
        ItemInfo("연면적","110.52㎡ (33평)")
        ItemTitle("임차인")
    }
}