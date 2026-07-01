package com.estateslug.slug.ui.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
fun ArrowTopBar(
    text: String = "",
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(NeutralInverted)
    ) {
        Row(
            Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val minSizeModifier = Modifier
                .sizeIn(48.dp, minHeight = 48.dp)

            Box(
                modifier = minSizeModifier
                    .blockingClickable(onClick = onBackClick)
                    .padding(start = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.sizeIn(28.dp, minHeight = 28.dp),
                    painter = painterResource(R.drawable.ic_arrow_left_28_28),
                    tint = Neutral,
                    contentDescription = "ArrowBack 뒤로가기"// TODO : i18n
                )
            }
            Text(text = text, style = SlugTypographyStyle.BodyLargeBold, color = Neutral)
            Spacer(Modifier.width(10.dp))
        }
        Spacer(Modifier.weight(1f))

    }
}

@Composable
@Preview
fun PreviewSettingTopBar() {
    val onBackClick: () -> Unit = {}
    SlugTheme {
        Surface {
            Column {
                ArrowTopBar(
                    text = "설정",
                    onBackClick = onBackClick,
                )
                ArrowTopBar(
                    onBackClick = onBackClick,
                )
            }
        }
    }
}