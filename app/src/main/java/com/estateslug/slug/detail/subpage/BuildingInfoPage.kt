package com.estateslug.slug.detail.subpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.component.maps.SlugMap
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
fun BuildingInfoPage(data: BuildingInfoUiModel, onMapFocused: (Boolean) -> Unit) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemTitle("건물 상세정보")
            //TODO : 버튼?물어보기
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .background(color = NeutralWeak)
                    .padding(vertical = 7.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "토지정보 바로보기",
                    style = SlugTypographyStyle.BodyMicroMedium,
                    color = Neutral
                )
            }
        }
        ItemInfo("불법건축물 여부", data.illegalBuildingStatus)
        ItemInfo("연면적", data.grossFloorArea)
        ItemInfo("세대수", data.householdInfo)
        ItemInfo("증축년도", data.constructionYear)
        ItemInfo("용적률", data.floorAreaRatio)
        ItemInfo("용도", data.usage)
        ItemInfo("구조", data.structure)
        ItemInfo("주차방식", data.parkingType)
        ItemInfo("승강기", data.elevator)
        SlugMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.75f)
                .clip(RoundedCornerShape(8.dp))
                .onFocusChanged({ state ->
                    onMapFocused(state.isFocused)
                }
                ),
            latitude = data.lat,
            longitude = data.lng
        )

    }
}


data class BuildingInfoUiModel(
    val illegalBuildingStatus: String,
    val grossFloorArea: String,
    val householdInfo: String,
    val constructionYear: String,
    val floorAreaRatio: String,
    val usage: String,
    val structure: String,
    val parkingType: String,
    val elevator: String,
    val lat: Double,
    val lng: Double,
) {
    companion object {
        val preview = BuildingInfoUiModel(
            illegalBuildingStatus = "-",
            grossFloorArea = "110.52㎡ (33평)",
            householdInfo = "1동 ㅣ 63세대 ㅣ 19층",
            constructionYear = "1996년 10월 29년차",
            floorAreaRatio = "160%",
            usage = "주거",
            structure = "콘크리트",
            parkingType = "기계식",
            elevator = "있음",
            lat = 37.554502,
            lng = 126.921729,
        )
    }
}

@Composable
@Preview
fun PreviewBuildingInfoPage() {
    val data = BuildingInfoUiModel.preview
    BuildingInfoPage(data, {  })
}