package com.warehouseinhand.slug.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.PrimaryLight
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle


@Composable
fun BuildingInfoChip(
    type: BuildingInfoChips,
    text: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = type.backgroundColor)
            .padding(vertical = 3.dp, horizontal = 6.dp)
    ) {
        Text(text = text, color = type.textColor, style = SlugTypographyStyle.CaptionLargeMedium)
    }
}

enum class BuildingInfoChips(
    val backgroundColor: Color,
    val textColor: Color
) {
    Apartment(backgroundColor = PrimaryLight, textColor = Primary),
    Villa(backgroundColor = PrimaryLight, textColor = Primary),
    Officetel(backgroundColor = PrimaryLight, textColor = Primary),
    Dagagu(backgroundColor = PrimaryLight, textColor = Primary),
    State(backgroundColor = NeutralWeak, textColor = NeutralSubtler),
    ;
}

@Composable
@Preview
fun PreviewBuildingInfoChip() {

    val type: BuildingInfoChips = BuildingInfoChips.Apartment
    val text: String = "아파트"
    BuildingInfoChip(type = type, text = text)
}