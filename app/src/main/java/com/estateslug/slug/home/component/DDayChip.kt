package com.estateslug.slug.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.theme.CriticalSubtle
import com.estateslug.slug.ui.theme.CriticalWeak
import com.estateslug.slug.ui.theme.Gray600
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralMuted
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
fun DDayChip(state: DDayState) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color = state.backGroundColor)
            .padding(vertical = 3.dp, horizontal = 6.dp)
    ) {
        Text(
            state.label,
            style = SlugTypographyStyle.CaptionLargeMedium,
            color = state.textColor
        )
    }
}

@Composable
@Preview
fun PreviewDDayChip() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        DDayChip(DDayState.Upcoming(daysLeft = 4))
        DDayChip(DDayState.Imminent(daysLeft = 3))
        DDayChip(DDayState.Imminent(daysLeft = 0))
        DDayChip(DDayState.Passed(daysPassed = 2))
        DDayChip(DDayState.SoldOut)
    }
}

/**
 * D-day 칩 상태. 각 상태가 라벨·스타일을 자체 보유해 렌더 지점의 조합 로직을 없앤다.
 * 매각 여부와 날짜 경과는 별개 정보 — 센티널(daysLeft=-1) 대신 [from]의 명시 인자로 판정한다.
 */
sealed interface DDayState {
    val textColor: Color
    val backGroundColor: Color
    val label: String

    /** 매각기일 4일 이상 남음 */
    data class Upcoming(val daysLeft: Int) : DDayState {
        override val textColor = CriticalWeak
        override val backGroundColor = Neutral
        override val label = "D-$daysLeft"
    }

    /** 매각기일 3일 이내 — 긴급 강조 */
    data class Imminent(val daysLeft: Int) : DDayState {
        override val textColor = CriticalWeak
        override val backGroundColor = CriticalSubtle
        override val label = "D-$daysLeft"
    }

    /** 매각기일 경과·미매각(유찰/개찰 반영 전) — 지난 일정이라 저채도·저대비로 한 톤 흐리게 */
    data class Passed(val daysPassed: Int) : DDayState {
        override val textColor = Gray600
        override val backGroundColor = NeutralMuted
        override val label = "D+$daysPassed"
    }

    data object SoldOut : DDayState {
        override val textColor = NeutralInverted
        override val backGroundColor = NeutralSubtler
        override val label = "매각"
    }

    companion object {
        // 부호 반전은 여기 한 곳에서만 — Passed는 경과일을 양수로 보관한다
        fun from(daysLeft: Int, isSoldOut: Boolean): DDayState = when {
            isSoldOut -> SoldOut
            daysLeft >= 4 -> Upcoming(daysLeft)
            daysLeft >= 0 -> Imminent(daysLeft)
            else -> Passed(daysPassed = -daysLeft)
        }
    }
}
