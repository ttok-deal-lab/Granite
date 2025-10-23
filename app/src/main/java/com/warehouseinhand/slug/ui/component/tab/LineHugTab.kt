package com.warehouseinhand.slug.ui.component.tab

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.noRippleClickable

@Composable
fun LineHugTab(tabName: String, isSelected: Boolean, onTabClick: () -> Unit) {
    //TODO : 하드코딩 값 상수로 변경 필요.
    val tabColor by animateColorAsState(
        targetValue = if (isSelected) Primary else NeutralSubtler,
        label = "color"
    )
    val indicatorColor by animateColorAsState(
        targetValue = if (isSelected) Primary else Primary.copy(alpha = 0f),
        label = "color"
    )
    Column(
        modifier = Modifier.Companion
            .width(IntrinsicSize.Max)
            .noRippleClickable(onClick = onTabClick)
    ) {
        Spacer(Modifier.Companion.height(12.dp))
        Text(
            tabName,
            style = SlugTypographyStyle.BodyLargeBold,
            color = tabColor
        )
        Spacer(Modifier.Companion.height(12.dp))
        Spacer(
            modifier = Modifier.Companion
                .background(indicatorColor)
                .height(2.dp)
                .fillMaxWidth(1f)
        )
    }
}

@Composable
@Preview
fun PreviewLineHugTab() {
    val list = listOf("경매정보", "권리분석")
    var selectedIndex by remember { mutableIntStateOf(1) }
    Row {
        list.forEachIndexed { index, string ->
            LineHugTab(
                tabName = string,
                isSelected = index == selectedIndex,
                { selectedIndex = index })
        }
    }
}