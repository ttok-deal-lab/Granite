package com.warehouseinhand.slug.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable

//TODO : 각 리소스 별 Description 처리 할것.
//TODO : home Top bar와 공통 컴포넌트화 할지 고민
@Composable
fun DetailTopBar(
    onBackButtonClick: () -> Unit,
    onShareButtonClick: () -> Unit,
    topTitle:String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
        , verticalAlignment = Alignment.CenterVertically
    ) {
        val minSizeModifier = Modifier.sizeIn(48.dp, minHeight = 48.dp)
        Box(
            modifier = minSizeModifier
                .blockingClickable(onClick = onBackButtonClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.sizeIn(28.dp, minHeight = 28.dp),
                painter = painterResource(R.drawable.ic_arrow_left_28_28),
                tint = Neutral,
                contentDescription = "notificationIcon"
            )
        }
        Text(
            topTitle,
            style = SlugTypographyStyle.BodyLargeBold,
            color = Neutral
        )
        Spacer(Modifier.weight(1f))

        Box(
            modifier = minSizeModifier
                .blockingClickable(onClick = onShareButtonClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.sizeIn(28.dp, minHeight = 28.dp),
                painter = painterResource(R.drawable.ic_share_28_28),
                tint = Neutral,
                contentDescription = "notificationIcon"
            )
        }

    }
}

@Composable
@Preview
fun PreviewDetailTopBar() {
    val onNotificationClick: () -> Unit = {}
    DetailTopBar(
        topTitle = "관천로 22길 52",
        onBackButtonClick = onNotificationClick,
        onShareButtonClick = {}
    )
}