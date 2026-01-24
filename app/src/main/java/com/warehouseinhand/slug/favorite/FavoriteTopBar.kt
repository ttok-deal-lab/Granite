package com.warehouseinhand.slug.favorite

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable

//TODO : 각 리소스 별 Description 처리 할것.
//TODO : home Top bar와 공통 컴포넌트화 할지 고민
@Composable
fun FavoriteTopBar(
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
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.home_favorite_top_title),
                style = SlugTypographyStyle.TitleLargeBold,
                color = Neutral
            )
        }
        Spacer(Modifier.weight(1f))
        //TODO : 알림제거
//        Row(
//            Modifier.fillMaxHeight(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            val minSizeModifier = Modifier.sizeIn(48.dp, minHeight = 48.dp)
//            Box(
//                modifier = minSizeModifier
//                    .blockingClickable(onClick = onNotificationClick),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    modifier = Modifier.sizeIn(28.dp, minHeight = 28.dp),
//                    painter = painterResource(R.drawable.notification_line_28_28),
//                    tint = Neutral,
//                    contentDescription = "notificationIcon"
//                )
//            }
//            Spacer(Modifier.width(10.dp))
//        }
    }
}

@Composable
@Preview
fun PreviewFavoriteTopBar() {
    val onNotificationClick: () -> Unit = {}
    FavoriteTopBar(
        onNotificationClick = onNotificationClick,
    )
}