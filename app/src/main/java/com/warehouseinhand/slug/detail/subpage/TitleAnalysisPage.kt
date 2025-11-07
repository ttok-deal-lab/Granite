package com.warehouseinhand.slug.detail.subpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.Gray150
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.NeutralMuted
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.SlugTheme
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable
import com.warehouseinhand.slug.util.dropShadow

//TODO : i18n

@Composable
fun TitleAnalysisPage() {
    val listOfLessee = LesseeInfo.lesseePreviewList
    val numberOfLessee = listOfLessee.size
    Column(
        modifier = Modifier.padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemTitle("임차인")
            Spacer(Modifier.width(4.dp))
            Text(
                numberOfLessee.toString(),
                style = SlugTypographyStyle.TitleMediumBold,
                color = Primary
            )
        }
        val lazyListState = rememberLazyListState()
        var selectedIndex: Int by remember { mutableIntStateOf(0) }
        LaunchedEffect(selectedIndex) {
            lazyListState.animateScrollToItem(selectedIndex)
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = lazyListState
        ) {
            itemsIndexed(listOfLessee) { index, lessee ->
                Box(
                    modifier = Modifier
                        .blockingClickable(
                            onClick = { selectedIndex = index }
                        )
                ) {
                    LesseeNameCard(
                        name = lessee.lesseeName,
                        isSelected = index == selectedIndex,
                        state = lessee.occupancyStatus
                    )
                }
            }
        }
        LesseeDataCard(data = listOfLessee[selectedIndex])
    }
}

@Composable
private fun LesseeNameCard(name: String, isSelected: Boolean, state: OccupancyStatus) {
    val borderColor = if (isSelected) Primary else NeutralMuted
    val cardShape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .let {
                if (isSelected)
                    it.dropShadow(
                        cardShape,
                        color = Color(0x14353A3F),
                        blur = 20.dp,
                        offsetY = 2.dp,
                        offsetX = 0.dp,
                        spread = 0.dp
                    )
                else it
            }
            .clip(shape = cardShape)
            .background(NeutralInverted)
            .border(1.dp, color = borderColor, shape = cardShape)
            .padding(16.dp)
            .widthIn(min = 178.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = name, style = SlugTypographyStyle.BodyMediumBold, color = Neutral)
            Spacer(Modifier.width(8.dp))
            if (isSelected)
                ImageProcessor(
                    modifier = Modifier.size(18.dp),
                    ImageResource.Id(R.drawable.ic_check_20_20)
                )
        }
        Text(
            text = state.displayName,
            style = SlugTypographyStyle.BodyMediumBold,
            color = state.statusColor
        )
    }
}

@Composable
private fun LesseeDataCard(data: LesseeInfo) {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(shape = shape)
            .background(color = NeutralInverted)
            .border(shape = shape, width = 1.dp, color = Gray150)
    ) {
        //윗부분
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = data.lesseeName,
                    style = SlugTypographyStyle.BodyMediumBold,
                    color = Neutral
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = data.nameOfPlace,
                    style = SlugTypographyStyle.BodyMicroMedium,
                    color = NeutralSubtler
                )
            }
            Text(
                text = data.occupancyStatus.displayName,
                style = SlugTypographyStyle.BodyMediumBold,
                color = data.occupancyStatus.statusColor
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
            LesseeDataCardItemTitle(name = "대항력", value = if (data.hasOpposition) "있음" else "없음")
            LesseeDataCardItemValue(name = "전입신고일", value = data.moveInDate)
            LesseeDataCardItemValue(name = "점유상태", value = data.occupancyStatus.displayName)
            LesseeDataCardItemTitle(
                name = "우선변제권",
                value = if (data.hasPreferentialRight) "있음" else "없음"
            )
            LesseeDataCardItemValue(name = "확정일자", value = data.confirmedDate)
            LesseeDataCardItemTitle(
                name = "배당요구",
                value = if (data.hasDividendClaim) "있음" else "없음"
            )
            LesseeDataCardItemValue(name = "배당요구일", value = data.dividendClaimDate)
            LesseeDataCardItemValue(name = "보증금", value = data.depositAmount)
            LesseeDataCardItemValue(name = "월세", value = data.monthlyRent)

        }
    }
}

@Composable
private fun LesseeDataCardItemTitle(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, style = SlugTypographyStyle.BodySmallBold, color = NeutralSubtler)
        Text(text = value, style = SlugTypographyStyle.BodySmallBold, color = NeutralSubtler)
    }
}

@Composable
private fun LesseeDataCardItemValue(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "└ $name", style = SlugTypographyStyle.BodySmallMedium, color = NeutralSubtler)
        Text(text = value, style = SlugTypographyStyle.BodySmallMedium, color = NeutralSubtler)
    }
}

enum class OccupancyStatus(val displayName: String, val statusColor: Color) {
    OCCUPIED(displayName = "점유중", statusColor = Primary),
    VACANT(displayName = "공실", statusColor = NeutralSubtler),
    UNKNOWN(displayName = "점유확인 필요", statusColor = NeutralSubtler)
}

data class LesseeInfo(
    val lesseeName: String,
    val nameOfPlace: String,
    val hasOpposition: Boolean,     // 대항력 있음/없음
    val moveInDate: String,        // 전입신고일
    val occupancyStatus: OccupancyStatus,    // 점유상태 (예: 거주중, 공실 등)
    val hasPreferentialRight: Boolean, // 우선변제권 있음/없음
    val confirmedDate: String,     // 확정일자
    val hasDividendClaim: Boolean,  // 배당요구 있음/없음
    val dividendClaimDate: String, // 배당요구일
    val depositAmount: String,     // 보증금
    val monthlyRent: String        // 월세
) {
    companion object {
        val lesseePreviewList by lazy {
            listOf(
                LesseeInfo(
                    lesseeName = "김도연",
                    nameOfPlace = "101호",
                    hasOpposition = true,
                    moveInDate = "2023.10.10",
                    occupancyStatus = OccupancyStatus.OCCUPIED,
                    hasPreferentialRight = true,
                    confirmedDate = "2023.09.10",
                    hasDividendClaim = true,
                    dividendClaimDate = "2024.09.06",
                    depositAmount = "100,000,000원",
                    monthlyRent = "100,000원"
                ),
                LesseeInfo(
                    lesseeName = "박시은",
                    nameOfPlace = "102호",
                    hasOpposition = true,
                    moveInDate = "2022.05.01",
                    occupancyStatus = OccupancyStatus.UNKNOWN,
                    hasPreferentialRight = false,
                    confirmedDate = "—",
                    hasDividendClaim = false,
                    dividendClaimDate = "—",
                    depositAmount = "50,000,000원",
                    monthlyRent = "0원"
                ),
                LesseeInfo(
                    lesseeName = "공시리",
                    nameOfPlace = "103호",
                    hasOpposition = false,
                    moveInDate = "2024.01.15",
                    occupancyStatus = OccupancyStatus.VACANT,
                    hasPreferentialRight = true,
                    confirmedDate = "2024.02.01",
                    hasDividendClaim = false,
                    dividendClaimDate = "—",
                    depositAmount = "80,000,000원",
                    monthlyRent = "200,000원"
                )
            )
        }
    }
}

@Composable
@Preview
fun PreviewLesseeNameCard() {
    SlugTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LesseeNameCard("김도연", true, OccupancyStatus.OCCUPIED)
                LesseeNameCard("박시은", false, OccupancyStatus.UNKNOWN)
                LesseeNameCard("공시리", false, OccupancyStatus.VACANT)
            }
        }
    }
}

@Composable
@Preview
fun PreviewTitleAnalysisPage() {
    TitleAnalysisPage()
}