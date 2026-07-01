package com.estateslug.slug.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
fun HomeTopBar(//TODO : 각 리소스 별 Description 처리 할것.
    sectionName: String,
    onLocationSelectClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .blockingClickable(onClick = onLocationSelectClick)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(sectionName, style = SlugTypographyStyle.TitleLargeBold, color = Neutral)
            Spacer(Modifier.width(4.dp))
            Icon(
                modifier = Modifier.sizeIn(minWidth = 16.dp, minHeight = 16.dp),
                painter = painterResource(R.drawable.arrow_down_16_16),
                tint = Neutral,
                contentDescription = "arrow down for Select Section"
            )
        }
        Spacer(Modifier.weight(1f))

        Row(
            Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val minSizeModifier = Modifier.sizeIn(48.dp, minHeight = 48.dp)
            Box(
                modifier = minSizeModifier
                    .blockingClickable(onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.sizeIn(28.dp, minHeight = 28.dp),
                    painter = painterResource(R.drawable.search_line_28_28),
                    tint = Neutral,
                    contentDescription = "searchIcon"
                )
            }

            Box(
                modifier = minSizeModifier
                    .blockingClickable(onClick = onNotificationClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.sizeIn(28.dp, minHeight = 28.dp),
                    painter = painterResource(R.drawable.notification_line_28_28),
                    tint = Neutral,
                    contentDescription = "notificationIcon"
                )
            }
            Spacer(Modifier.width(10.dp))
        }
    }
}

@Composable
@Preview
fun PreviewHomeTopBar() {
    val sectionName = "서울 관악구"
    val onSectionSelectClick: () -> Unit = {}
    val onSearchClick: () -> Unit = {}
    val onNotificationClick: () -> Unit = {}
    HomeTopBar(
        sectionName = sectionName,
        onLocationSelectClick = onSectionSelectClick,
        onSearchClick = onSearchClick,
        onNotificationClick = onNotificationClick,
    )
}