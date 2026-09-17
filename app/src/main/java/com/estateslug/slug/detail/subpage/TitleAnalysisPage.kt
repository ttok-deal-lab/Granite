package com.estateslug.slug.detail.subpage

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
import com.estateslug.slug.R
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable
import com.estateslug.slug.util.dropShadow
import com.estateslug.slug.util.numberToNumberFormatKR

//TODO : i18n


@Composable
fun TitleAnalysisPage(listOfLessee: List<LesseeInfo>) {
    val numberOfLessee: Int = listOfLessee.size
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
                color = SlugTheme.colors.primary
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
    val borderColor = if (isSelected) SlugTheme.colors.primary else SlugTheme.colors.neutralMuted
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
            .background(SlugTheme.colors.surfaceRaised)
            .border(1.dp, color = borderColor, shape = cardShape)
            .padding(16.dp)
            .widthIn(min = 178.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                style = SlugTypographyStyle.BodyMediumBold,
                color = SlugTheme.colors.neutral
            )
            Spacer(Modifier.width(8.dp))
            if (isSelected)
                ImageProcessor(
                    modifier = Modifier.size(18.dp),
                    ImageResource.Id(R.drawable.ic_check_20_20),
                    tint = SlugTheme.colors.primary // 자산 선 색 #1E9EFF = Primary
                )
        }
        Text(
            text = state.displayName,
            style = SlugTypographyStyle.BodyMediumBold,
            color = state.tone.color()
        )
    }
}

@Composable
private fun LesseeDataCard(data: LesseeInfo) {
    val depositAmount = remember(data.depositAmount) { numberToNumberFormatKR(data.depositAmount) }
    val monthlyRent = remember(data.monthlyRent) { numberToNumberFormatKR(data.monthlyRent) }

    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(shape = shape)
            .background(color = SlugTheme.colors.surfaceRaised)
            .border(shape = shape, width = 1.dp, color = SlugTheme.colors.outlineVariant)
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
                    color = SlugTheme.colors.neutral
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = data.nameOfPlace,
                    style = SlugTypographyStyle.BodyMicroMedium,
                    color = SlugTheme.colors.neutralSubtler
                )
            }
            Text(
                text = data.occupancyStatus.displayName,
                style = SlugTypographyStyle.BodyMediumBold,
                color = data.occupancyStatus.tone.color()
            )
        }
        //아랫부분
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = SlugTheme.colors.surfaceInset)
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
            LesseeDataCardItemValue(name = "보증금", value = "${depositAmount}원")
            LesseeDataCardItemValue(name = "월세", value = "${monthlyRent}원")

        }
    }
}

@Composable
private fun LesseeDataCardItemTitle(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = SlugTypographyStyle.BodySmallBold,
            color = SlugTheme.colors.neutralSubtler
        )
        Text(
            text = value,
            style = SlugTypographyStyle.BodySmallBold,
            color = SlugTheme.colors.neutralSubtler
        )
    }
}

@Composable
private fun LesseeDataCardItemValue(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "└ $name",
            style = SlugTypographyStyle.BodySmallMedium,
            color = SlugTheme.colors.neutralSubtler
        )
        Text(
            text = value,
            style = SlugTypographyStyle.BodySmallMedium,
            color = SlugTheme.colors.neutralSubtler
        )
    }
}

enum class OccupancyStatus(val displayName: String, val tone: StatusTone) {
    OCCUPIED(displayName = "점유중", tone = StatusTone.POSITIVE),
    VACANT(displayName = "공실", tone = StatusTone.NEUTRAL),
    NONE(displayName = "없음", tone = StatusTone.NEUTRAL),
    UNKNOWN(displayName = "점유확인 필요", tone = StatusTone.NEUTRAL)
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
    val depositAmount: Long,     // 보증금
    val monthlyRent: Long        // 월세
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
                    depositAmount = 100_000_000,
                    monthlyRent = 100_000
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
                    depositAmount = 50_000_000,
                    monthlyRent = 0
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
                    depositAmount = 80_000_000,
                    monthlyRent = 200_000
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
    val listOfLessee: List<LesseeInfo> = LesseeInfo.lesseePreviewList
    SlugTheme(
//        darkTheme = true
    ) {
        Surface {
            TitleAnalysisPage(listOfLessee)
        }
    }
}