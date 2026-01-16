package com.warehouseinhand.slug.detail.subpage.auction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.detail.subpage.ItemInfo
import com.warehouseinhand.slug.detail.subpage.ItemTitle
import com.warehouseinhand.slug.ui.component.maps.SlugMap
import com.warehouseinhand.slug.ui.theme.NeutralInverted


@Composable
fun CourtInfo(uiModel: CourtInfoUiModel, onMapFocused: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = NeutralInverted)
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ItemTitle("법원 정보")

        ItemInfo(name = "관할법원", value = uiModel.courtName)
        ItemInfo(name = "담당", value = uiModel.courtTeam)
        ItemInfo(name = "매각기일", value = uiModel.saleDate)
        ItemInfo(name = "입찰시간", value = uiModel.bidTime)
        ItemInfo(name = "개찰시간", value = uiModel.openingTime)
        SlugMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.75f)
                .clip(RoundedCornerShape(8.dp))
                .onFocusChanged({ state ->
                    onMapFocused(state.isFocused)
                }
                )
            ,
            latitude = uiModel.lat,
            longitude = uiModel.lng
        )

    }
}

data class CourtInfoUiModel(
    val courtName: String,
    val courtTeam: String,
    val saleDate: String,
    val bidTime: String,
    val openingTime: String,
    val lat: Double,
    val lng: Double,
) {
    companion object {
        val preview = CourtInfoUiModel(
            courtName = "성남지원",
            courtTeam = "경매 2개",
            saleDate = "2025.05.05",
            bidTime = "10:00 - 11:10",
            openingTime = "11:30",
            lat = 37.453145,
            lng = 127.159211
        )
    }
}

@Composable
@Preview
fun PreviewCourtInfo() {
    CourtInfo(
        uiModel = CourtInfoUiModel.preview,
        onMapFocused = {  }
    )
}