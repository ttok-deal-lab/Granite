package com.estateslug.slug.detail.subpage.auction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.detail.subpage.ItemTitle
import com.estateslug.slug.ui.theme.Gray150
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralLight
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTypographyStyle

//TODO : 쪼갤것!
@Composable
fun SalePropertyInfo(uiModel: SalePropertyInfoUiModel) {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = NeutralInverted)
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ItemTitle("매각물건 현황")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("감정가", style = SlugTypographyStyle.BodyMediumMedium, color = NeutralSubtler)
            Text(
                uiModel.appraisedTotal,
                style = SlugTypographyStyle.BodyMediumBold,
                color = Neutral
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = shape)
                .background(color = NeutralInverted)
                .border(shape = shape, width = 1.dp, color = Gray150)
        ) {
            val landSummary = uiModel.landSummary
            //윗부분
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "대지권",
                        style = SlugTypographyStyle.BodyMediumMedium,
                        color = NeutralSubtler
                    )
                    Text(
                        landSummary.amount,
                        style = SlugTypographyStyle.BodyMediumBold,
                        color = Neutral
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = landSummary.areaChip,
                    style = SlugTypographyStyle.BodyTinyMedium,
                    color = NeutralSubtler
                )
            }

            //아랫부분
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = NeutralLight)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BuildingItemInfo("소재지", uiModel.landSummary.location)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = shape)
                .background(color = NeutralInverted)
                .border(shape = shape, width = 1.dp, color = Gray150)
        ) {
            val buildingSummary = uiModel.buildingSummary
            //윗부분
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "건물",
                        style = SlugTypographyStyle.BodyMediumMedium,
                        color = NeutralSubtler
                    )
                    Text(
                        buildingSummary.amount,
                        style = SlugTypographyStyle.BodyMediumBold,
                        color = Neutral
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = buildingSummary.areaChip,
                    style = SlugTypographyStyle.BodyTinyMedium,
                    color = NeutralSubtler
                )
            }
            val buildings = uiModel.buildingUnits

            //아랫부분
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = NeutralLight),
            ) {
                buildings.forEachIndexed {
                        index, buildingUnit ->
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BuildingItemInfo("소재지", buildingUnit.location)
                        BuildingItemInfo("용도/면적", buildingUnit.usageOrArea)
                        BuildingItemInfo("감정가", buildingUnit.appraised)
                    }
                    if (index < buildings.lastIndex) {
                        HorizontalDivider(color = Gray150, thickness = 1.dp)
                    }
                }
            }
        }
    }
}



@Composable
internal fun BuildingItemInfo(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = name, style = SlugTypographyStyle.BodySmallMedium, color = NeutralSubtler)
        Text(text = value, style = SlugTypographyStyle.BodySmallMedium, color = NeutralSubtler)
    }
}

data class SalePropertyInfoUiModel(
    val appraisedTotal: String,        // 상단 "감정가 2억 3,000만원"
    val landSummary: PartSummary,      // 대지권 박스 요약
    val buildingSummary: PartSummary,  // 건물 박스 헤더 요약
    val buildingUnits: List<BuildingUnit> // 건물 상세(여러 유닛)
) {
    companion object {
        val preview = SalePropertyInfoUiModel(
            appraisedTotal = "2억 3,000만원",
            landSummary = PartSummary(
                title = "대지권",
                amount = "5천 2500만원",
                areaChip = "면적 10.52㎡",
                location = "5층 625호"
            ),
            buildingSummary = PartSummary(
                title = "건물",
                amount = "1억 2500만원",
                areaChip = "면적 10.52㎡",
                location = "-"
            ),
            buildingUnits = listOf(
                BuildingUnit(
                    location = "8층 중 4층",
                    usageOrArea = "오피스텔",
                    appraised = "5,000만원"
                ),
                BuildingUnit(
                    location = "8층 중 5층",
                    usageOrArea = "오피스텔",
                    appraised = "5,000만원"
                )
            )
        )
    }
}

data class PartSummary(
    val title: String,          // 예: "대지권", "건물"
    val amount: String,         // 예: "5천 2500만원", "1억 2500만원"
    val areaChip: String,      // 예: "면적 10.52㎡" (칩 형태)
    val location: String   // 대지권 하단 회색 박스의 "소재지 5층 625호" 등
)

data class BuildingUnit(
    val location: String,    // 예: "8층 중 4층"
    val usageOrArea: String, // 예: "오피스텔" (혹은 "용도/면적" 표시값)
    val appraised: String    // 예: "5,000만원"
)


@Composable
@Preview
fun PreviewSalePropertyInfo() {
    val uiModel: SalePropertyInfoUiModel = SalePropertyInfoUiModel.preview
    SalePropertyInfo(uiModel)
}