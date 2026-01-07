package com.warehouseinhand.slug.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.util.blockingClickable

@Composable
fun MyPageTopBar(
    onSettingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Row(
            Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val minSizeModifier = Modifier.sizeIn(48.dp, minHeight = 48.dp)

            Box(
                modifier = minSizeModifier
                    .blockingClickable(onClick = onSettingClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.sizeIn(28.dp, minHeight = 28.dp),
                    painter = painterResource(R.drawable.ic_setting_cog_28_28),
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
    val onSettingClick: () -> Unit = {}
    MyPageTopBar(
        onSettingClick = onSettingClick,
    )
}