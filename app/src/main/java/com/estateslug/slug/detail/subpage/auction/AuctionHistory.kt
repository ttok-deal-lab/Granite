package com.estateslug.slug.detail.subpage.auction

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
import com.estateslug.slug.R
import com.estateslug.slug.detail.subpage.ItemInfo
import com.estateslug.slug.detail.subpage.ItemTitle
import com.estateslug.slug.home.component.tooltip.AlertSlugTooltip
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.Critical
import com.estateslug.slug.ui.theme.Gray150
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralLight
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.Primary
import com.estateslug.slug.ui.theme.PrimaryLight
import com.estateslug.slug.ui.theme.PrimaryWhite
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable
import com.estateslug.slug.util.numberToNumberFormatKR


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

        if (uiModel.rounds.isNotEmpty())
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

@Composable
private fun Round(index: Int, round: AuctionRound) {
    val numberToNumberFormatKR =
        remember(round.minSalePrice) { numberToNumberFormatKR(round.minSalePrice) }
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
//            Spacer(Modifier.weight(1f))
//            //TODO 이동이 필요한 Ui 같음 확인 필요!
//            ImageProcessor(
//                modifier = Modifier.size(16.dp),
//                imageResource = ImageResource.Id(R.drawable.arrow_right_16_16)
//            )
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
                text = "${numberToNumberFormatKR}원",
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
// enum 상수명 == 서버 result 코드. displayName == 사용자 노출 한글 라벨.
//TODO : i18n
enum class AuctionResult(val displayName: String, val color: Color, val tooltipText: String) {

    // [기일 진행]
    PLANNED(
        displayName = "예정",
        color = Primary,
        tooltipText = "경매 기일이 잡혀 입찰이 예정된 상태"
    ),
    PREPARING_SALE(
        displayName = "매각준비",
        color = Primary,
        tooltipText = "매각을 위한 사전 절차가 진행 중인 상태"
    ),
    IN_PROGRESS(
        displayName = "진행",
        color = Primary,
        tooltipText = "경매 기일에 입찰이 진행 중이거나 예정된 상태"
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

    // [매각 허가 / 불허가]
    BEST_BID_APPROVED(
        displayName = "최고가매각허가결정",
        color = NeutralSubtler,
        tooltipText = "법원이 매각허가결정을 내려 소유권 이전 절차가 진행되는 상태"
    ),
    SECONDARY_BID_APPROVED(
        displayName = "차순위매각허가결정",
        color = NeutralSubtler,
        tooltipText = "법원이 차순위 매수신고인에게 매각허가결정을 내린 상태"
    ),
    BEST_BID_REJECTED(
        displayName = "최고가매각불허가결정",
        color = Critical,
        tooltipText = "이의제기 등으로 법원이 매각허가를 불허한 상태"
    ),
    SECONDARY_BID_REJECTED(
        displayName = "차순위매각불허가결정",
        color = Critical,
        tooltipText = "이의제기 등으로 법원이 차순위 매수신고인에 대한 매각허가를 불허한 상태"
    ),
    BEST_BID_APPROVAL_CANCELLED(
        displayName = "최고가매각허가취소결정",
        color = Critical,
        tooltipText = "법원이 최고가 매수인에 대한 매각허가결정을 취소한 상태"
    ),
    SECONDARY_BID_APPROVAL_CANCELLED(
        displayName = "차순위매각허가취소결정",
        color = Critical,
        tooltipText = "법원이 차순위 매수신고인에 대한 매각허가결정을 취소한 상태"
    ),

    // [대금 납부]
    PAYMENT_COMPLETED(
        displayName = "납부",
        color = NeutralSubtler,
        tooltipText = "매수인이 정해진 기한 내 매각대금을 완납한 상태"
    ),
    PAYMENT_MISSED(
        displayName = "미납",
        color = Critical,
        tooltipText = "매수인이 기한 내 매각대금을 납부하지 않은 상태"
    ),
    LATE_PAYMENT(
        displayName = "기한후납부",
        color = NeutralSubtler,
        tooltipText = "매수인이 지정된 기한 이후 매각대금을 납부한 상태"
    ),
    OFFSET_APPROVED(
        displayName = "상계허가",
        color = NeutralSubtler,
        tooltipText = "채권자인 매수인이 배당받을 금액과 매각대금의 상계를 법원이 허가한 상태"
    ),

    // [일정 변경]
    MODIFIED(
        displayName = "변경",
        color = NeutralSubtler,
        tooltipText = "법원 사정 등으로 경매 일정(기일)이 변경된 상태"
    ),
    DEADLINE_CHANGED(
        displayName = "기한변경",
        color = NeutralSubtler,
        tooltipText = "법원 사정 등으로 대금 납부 등 기한이 변경된 상태"
    ),
    TO_BE_SPECIFIED(
        displayName = "추후지정",
        color = NeutralSubtler,
        tooltipText = "다음 기일이 아직 정해지지 않아 추후 지정될 예정인 상태"
    ),

    // [배당 / 종결]
    DISTRIBUTION_COMPLETED(
        displayName = "배당종결",
        color = NeutralSubtler,
        tooltipText = "매각대금이 채권자에게 배당되어 경매절차가 종결된 상태"
    ),
    DISTRIBUTION_UNAVAILABLE(
        displayName = "배당불가",
        color = Critical,
        tooltipText = "배당 요건을 충족하지 못해 배당을 진행할 수 없는 상태"
    ),

    INVALID(
        displayName = "INVALID",
        color = Critical,
        tooltipText = "INVALID"
    )

    ;

    companion object {
        /**
         * 서버 result 값을 매핑한다.
         * 1순위: enum 코드명(PLANNED, SOLD, BEST_BID_APPROVED ...) 일치
         * 2순위: 한글 표시명(레거시 호환) 일치
         * 매칭 실패 시 INVALID
         */
        fun fromServer(raw: String): AuctionResult =
            entries.find { it.name == raw }
                ?: entries.find { it.displayName == raw }
                ?: INVALID

        @Deprecated(
            "Use fromServer (서버가 코드값으로 내려줌)",
            ReplaceWith("AuctionResult.fromServer(displayName)")
        )
        fun fromDisplayName(displayName: String): AuctionResult = fromServer(displayName)
    }
}

data class AuctionRound(
    val round: Int,              // 회차 (예: 8)
    val date: String,            // 날짜 (예: "2025.05.12")
    val minSalePrice: Long,    // 최저매각 (예: "419,000,000원")
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
                    round = 11,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.PLANNED
                ),
                AuctionRound(
                    round = 10,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.IN_PROGRESS
                ),
                AuctionRound(
                    round = 9,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.MODIFIED
                ),
                AuctionRound(
                    round = 8,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.FAILED
                ),
                AuctionRound(
                    round = 7,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.SOLD
                ),
                AuctionRound(
                    round = 6,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.BEST_BID_APPROVED
                ),
                AuctionRound(
                    round = 5,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.BEST_BID_REJECTED
                ),
                AuctionRound(
                    round = 4,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.PAYMENT_COMPLETED
                ),
                AuctionRound(
                    round = 3,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.PAYMENT_MISSED
                ),
                AuctionRound(
                    round = 2,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.DISTRIBUTION_COMPLETED
                ),
                AuctionRound(
                    round = 1,
                    date = "2025.05.12",
                    minSalePrice = 419_000_000,
                    result = AuctionResult.DISTRIBUTION_UNAVAILABLE
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
                    minSalePrice = 419_000_000,
                    result = AuctionResult.DISTRIBUTION_COMPLETED
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