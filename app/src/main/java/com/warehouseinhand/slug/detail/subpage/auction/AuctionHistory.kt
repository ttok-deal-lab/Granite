package com.warehouseinhand.slug.detail.subpage.auction

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.detail.subpage.ItemInfo
import com.warehouseinhand.slug.detail.subpage.ItemTitle
import com.warehouseinhand.slug.home.component.tooltip.AlertSlugTooltip
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.Critical
import com.warehouseinhand.slug.ui.theme.Gray150
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralLight
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.PrimaryLight
import com.warehouseinhand.slug.ui.theme.PrimaryWhite
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable


@Composable
fun AuctionHistory(uiModel: AuctionHistoryUiModel) {
    val shape = RoundedCornerShape(8.dp)
    var isExpanded by remember { mutableStateOf(false) }
    val isExpandable = uiModel.rounds.size > 1
    //경매기록 수가 2건 이상이라면(expend ux적용)

    Column(
        Modifier
            .background(color = NeutralInverted)
            .padding(top = 24.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ItemTitle("경매 히스토리")
        ItemInfo(name = "경매개시일", value = uiModel.auctionStartDate)
        ItemInfo(name = "배당종기일", value = uiModel.dividendDeadline)
        ItemInfo(name = "감정평가일", value = uiModel.appraisalDate)

        Column {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = shape)
                        .background(color = NeutralLight)
                        .border(shape = shape, width = 1.dp, color = Gray150)
                        .animateContentSize()
                ) {
                    if (isExpanded)
                        uiModel.rounds.forEachIndexed { index, round ->
                            Round(index = index, round = round)
                            if (index < uiModel.rounds.lastIndex) {
                                HorizontalDivider(thickness = 1.dp, color = Gray150)
                            }
                        }
                    else {
                        Round(index = 0, round = uiModel.rounds.first())
                    }
                }
                if (isExpandable && !isExpanded)
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(24.dp)
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(Color.Transparent, PrimaryWhite),
                                )
                            )
                    )
            }
            if (isExpandable)
                Box(
                    modifier = Modifier
                        .blockingClickable(onClick = {
                            isExpanded = !isExpanded
                        })
                        .height(48.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ImageProcessor(
                        modifier = Modifier
                            .rotate(if (isExpanded) 0f else 180f)
                            .size(16.dp),
                        imageResource = ImageResource.Id(R.drawable.ic_arrow_up_16_16)
                    )
                }
            else {
                //아랫 패딩 역활을 대신함!
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun Round(index: Int, round: AuctionRound) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (index == 0)
                Box(
                    modifier = Modifier
                        .background(
                            color = PrimaryLight,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 3.dp, horizontal = 6.dp)
                ) {
                    Text(
                        "최신",
                        style = SlugTypographyStyle.CaptionLargeMedium,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            Text(
                text = "${round.round}차 | ${round.date}",
                style = SlugTypographyStyle.BodySmallBold,
                color = NeutralSubtler
            )
            Spacer(Modifier.weight(1f))
            //TODO 이동이 필요한 Ui 같음 확인 필요!
            ImageProcessor(
                modifier = Modifier.size(16.dp),
                imageResource = ImageResource.Id(R.drawable.arrow_right_16_16)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "최저매각",
                style = SlugTypographyStyle.BodySmallMedium,
                color = NeutralSubtler
            )
            Text(
                text = round.minSalePrice,
                style = SlugTypographyStyle.BodySmallMedium,
                color = NeutralSubtler
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "결과",
                style = SlugTypographyStyle.BodySmallMedium,
                color = NeutralSubtler
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = round.result.displayName,
                    style = SlugTypographyStyle.BodySmallBold,
                    color = round.result.color
                )
                Spacer(modifier = Modifier.width(2.dp))
                AlertSlugTooltip(round.result.tooltipText)
            }
        }
    }
}


// 결과 상태 (색상 토큰은 프로젝트의 Theme 컬러 사용 가정)
//TODO : i18n
enum class AuctionResult(val displayName: String, val color: Color, val tooltipText: String) {
    PROCEEDING(
        displayName = "진행",
        color = Primary,
        tooltipText = "경매 기일에 입찰이 진행 중이거나 예정된 상태"
    ),
    CHANGED(
        displayName = "변경",
        color = NeutralSubtler,
        tooltipText = "법원 사정 등으로 경매 일정(기일)이 변경된 상태"
    ),
    FAILED(
        displayName = "유찰",
        color = NeutralSubtler,
        tooltipText = "입찰자가 없어 낙찰되지 않고 다음 기일로 넘어가는 상태"
    ),
    SOLD(
        displayName = "매각",
        color = NeutralSubtler,
        tooltipText = "입찰 경쟁을 통해 낙찰자가 결정되어 매각이 완료된 상태 (매수인 및 낙찰가 포함)"
    ),
    APPROVED(
        displayName = "허가",
        color = NeutralSubtler,
        tooltipText = "법원이 매각허가결정을 내려 소유권 이전 절차가 진행되는 상태"
    ),
    WITHDRAWN(
        displayName = "취하",
        color = NeutralSubtler,
        tooltipText = "채권자의 신청 또는 채무자의 변제로 경매절차가 중단된 상태"
    ),
    REJECTED(
        displayName = "불허가",
        color = Critical,
        tooltipText = "이의제기 등으로 법원이 매각허가를 불허한 상태"
    ),
    ENDED(
        displayName = "종료",
        color = NeutralSubtler,
        tooltipText = "매각허가결정이 확정되면서 경매절차가 종료된 상태"
    )
}

data class AuctionRound(
    val round: Int,              // 회차 (예: 8)
    val date: String,            // 날짜 (예: "2025.05.12")
    val minSalePrice: String,    // 최저매각 (예: "419,000,000원")
    val result: AuctionResult    // 결과
)

data class AuctionHistoryUiModel(
    val auctionStartDate: String, // 경매개시일
    val dividendDeadline: String, // 배당종기일
    val appraisalDate: String,    // 감정평가일
    val rounds: List<AuctionRound>
) {
    companion object {
        val preview = AuctionHistoryUiModel(
            auctionStartDate = "2023.12.20",
            dividendDeadline = "2024.02.21",
            appraisalDate = "2023.12.27",
            rounds = listOf(
                AuctionRound(
                    round = 8,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.PROCEEDING
                ),
                AuctionRound(
                    round = 7,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.CHANGED
                ),
                AuctionRound(
                    round = 6,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.FAILED
                ),
                AuctionRound(
                    round = 5,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.SOLD
                ),
                AuctionRound(
                    round = 4,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.APPROVED
                ),
                AuctionRound(
                    round = 3,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.WITHDRAWN
                ),
                AuctionRound(
                    round = 2,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.REJECTED
                ),
                AuctionRound(
                    round = 1,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.ENDED
                )
            )
        )
        val previewShort = AuctionHistoryUiModel(
            auctionStartDate = "2023.12.20",
            dividendDeadline = "2024.02.21",
            appraisalDate = "2023.12.27",
            rounds = listOf(
                AuctionRound(
                    round = 1,
                    date = "2025.05.12",
                    minSalePrice = "419,000,000원",
                    result = AuctionResult.ENDED
                )
            )
        )
    }
}


@Preview(heightDp = 1200)
@Composable
fun PreviewAuctionHistory() {
    val uiModel: AuctionHistoryUiModel = AuctionHistoryUiModel.preview
    Column {
        AuctionHistory(AuctionHistoryUiModel.previewShort)
        Spacer(Modifier.height(24.dp))
        AuctionHistory(uiModel)
    }
}