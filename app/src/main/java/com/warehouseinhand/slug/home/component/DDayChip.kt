package com.warehouseinhand.slug.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.CriticalSubtle
import com.warehouseinhand.slug.ui.theme.CriticalWeak
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle

@Composable
fun DDayChip(daysLeft: Int) {
    val state = DDayState.getStateByLeftDay(daysLeft)
    Box(
        modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color = state.backGroundColor)
            .padding(vertical = 3.dp, horizontal = 6.dp)
    ) {
        Text(
            if (state != DDayState.SOLD_OUT) "D-$daysLeft" else "매각",
            style = SlugTypographyStyle.CaptionLargeMedium,
            color = state.textColor
        )
    }
}
@Composable
@Preview
fun PreviewDDayChip() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        DDayChip(daysLeft = 4)
        DDayChip(daysLeft = 3)
        DDayChip(daysLeft = 0)
        DDayChip(daysLeft = -1)
    }
}

enum class DDayState(
    val textColor: Color,
    val backGroundColor: Color
) {
    //Naming?
    ABOVE_4(textColor = CriticalWeak, backGroundColor = Neutral),
    IN_3(textColor = CriticalWeak, backGroundColor = CriticalSubtle),
    SOLD_OUT(textColor = NeutralInverted, backGroundColor = NeutralSubtler),

    ;

    companion object {
        fun getStateByLeftDay(daysLeft: Int) = when {
            daysLeft >= 4 -> ABOVE_4
            daysLeft >= 0 -> IN_3
            else -> SOLD_OUT
        }
    }

}