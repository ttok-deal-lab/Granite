package com.estateslug.slug.detail.subpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
internal fun ItemTitle(title: String) {
    Text(text = title, style = SlugTypographyStyle.TitleMediumBold, color = SlugTheme.colors.neutral)
}

@Composable
internal fun ItemInfo(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = name, style = SlugTypographyStyle.BodyMediumMedium, color = SlugTheme.colors.neutralSubtler)
        Text(text = value, style = SlugTypographyStyle.BodyMediumMedium, color = SlugTheme.colors.neutral)
    }
}

/**
 * 상태 글자의 색 역할. 상태 enum(OccupancyStatus, AuctionResult)이 [Color]를 직접 들지 않고
 * 이 역할만 들며, 렌더 시점에 [color]로 [SlugTheme.colors]에서 해석한다.
 * 라이트 값은 기존 상수와 같다: POSITIVE = Primary, NEUTRAL = NeutralSubtler, CRITICAL = Critical.
 */
enum class StatusTone {
    POSITIVE, NEUTRAL, CRITICAL;

    @Composable
    @ReadOnlyComposable
    fun color(): Color = when (this) {
        POSITIVE -> SlugTheme.colors.primary
        NEUTRAL -> SlugTheme.colors.neutralSubtler
        CRITICAL -> SlugTheme.colors.critical
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