package com.warehouseinhand.slug.detail

import androidx.lifecycle.ViewModel
import com.warehouseinhand.slug.data.network.sales.RemoteSalesDataRepository
import com.warehouseinhand.slug.detail.subpage.LesseeInfo
import com.warehouseinhand.slug.detail.subpage.OccupancyStatus
import com.warehouseinhand.slug.detail.subpage.auction.AuctionCardUiModel
import com.warehouseinhand.slug.detail.subpage.auction.AuctionHistoryUiModel
import com.warehouseinhand.slug.detail.subpage.auction.AuctionInfoUiModel
import com.warehouseinhand.slug.detail.subpage.auction.AuctionResult
import com.warehouseinhand.slug.detail.subpage.auction.AuctionRound
import com.warehouseinhand.slug.detail.subpage.auction.CourtInfoUiModel
import com.warehouseinhand.slug.detail.subpage.auction.RegistryInfoUiModel
import com.warehouseinhand.slug.domain.sales.CourtSaleDetail
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.label.SlugLabelBackground
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import com.warehouseinhand.slug.ui.component.label.SlugLabelUiModel
import com.warehouseinhand.slug.ui.theme.Critical
import com.warehouseinhand.slug.ui.theme.CriticalWeak
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DetailedViewModel @Inject constructor(
    private val remoteSalesDataRepository: RemoteSalesDataRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<CourtSaleDetailUiState> = MutableStateFlow(CourtSaleDetailUiState.preview)
    val uiState: StateFlow<CourtSaleDetailUiState> = _uiState.asStateFlow()


    fun requestData(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteSalesDataRepository.getCourtSaleDetail(id)
                .onSuccess { detail ->
                    val lessees = detail.toLesseesRaw()
                    val detailSimpleInformation: DetailSimpleInformationUiModel =
                        detail.toDetailSimpleInformationUiModel()
                    val auctionInfo = detail.toAuctionInfoUiModel()

                    _uiState.update {
                        it.copy(
                            listOfLessees = lessees,
                            detailSimpleInformation = detailSimpleInformation,
                            auctionInfo = auctionInfo,
                        )
                    }
                }
        }
    }


}

data class CourtSaleDetailUiState(
    val detailSimpleInformation: DetailSimpleInformationUiModel,
    val listOfLessees: List<LesseeInfo>,
    val auctionInfo: AuctionInfoUiModel,
) {
    companion object {
        val preview = CourtSaleDetailUiState(
            detailSimpleInformation = DetailSimpleInformationUiModel.preview,
            listOfLessees = LesseeInfo.lesseePreviewList,
            auctionInfo = AuctionInfoUiModel.preview
        )
    }
}


private fun CourtSaleDetail.toAuctionInfoUiModel(): AuctionInfoUiModel =
    AuctionInfoUiModel(
        checkCardList = toCHeckCardList(),
        auctionHistoryUiModel = toAuctionHistoryUiModel(),
        courtInfoUiModel = toCourtInfoUiModel(),
        registryInfoUiModel = RegistryInfoUiModel.preview, /*TODO 1차MVP 미포함*/
        courtDetailInfo = if (salesItemDetails.isEmpty()) "" else salesItemDetails.first().content
    )


private fun CourtSaleDetail.toCHeckCardList(): List<AuctionCardUiModel> = listOf(
    AuctionCardUiModel(
        name = "경매구분", value = "1차 MVP 논의 필요", isCritical = true,
        DetailBottomSheetType.InfoSheetType.TypeOfAuction
    ),
    AuctionCardUiModel(
        name = "임차인", value = "1차 MVP 논의 필요", isCritical = true,
        DetailBottomSheetType.InfoSheetType.Lessee
    ),
    AuctionCardUiModel(
        name = "채권자", value = "1차 MVP 논의 필요", isCritical = false,
        DetailBottomSheetType.InfoSheetType.Creditor
    ),
)

private fun CourtSaleDetail.toAuctionHistoryUiModel(): AuctionHistoryUiModel =
    AuctionHistoryUiModel(
        auctionStartDate = salesOpenDate,
        dividendDeadline = distributionRequiredDeadlineDate,
        appraisalDate = conditionReport.investigationDate,
        rounds = salesDetails.let {
            it.size
            it.mapIndexed { index, detail ->
                AuctionRound(
                    round = it.size - index,
                    date = detail.timeStamp,
                    minSalePrice = detail.leastSalesPrice,
                    result = AuctionResult.fromDisplayName(detail.result),
                )
            }
        }
    )

private fun CourtSaleDetail.toCourtInfoUiModel(): CourtInfoUiModel =
    CourtInfoUiModel(
        courtName = courtCode,
        courtTeam = courtTeam,
        saleDate = salesDateTime,
        bidTime = salesDetails.first().timeStamp,
        openingTime = " TODO 논의 필요",
        lat = 37.453145,
        lng = 127.159211
    )

private fun CourtSaleDetail.toLesseesRaw(): List<LesseeInfo> =
    rightsAnalysis.map { ra ->
        LesseeInfo(
            lesseeName = ra.name,
            nameOfPlace = "", // 서버에 없으니 빈값(그대로)
            hasOpposition = ra.hasOppositionRight.toBooleanRaw(),
            moveInDate = ra.moveInReportDate,
            occupancyStatus = ra.occupationStatus.toOccupancyStatusRaw(),
            hasPreferentialRight = ra.priorityRepaymentRight.toBooleanRaw(),
            confirmedDate = ra.fixedDate,
            hasDividendClaim = ra.dividendRequest.toBooleanRaw(),
            dividendClaimDate = ra.dividendRequestDate,
            depositAmount = ra.deposit,
            monthlyRent = ra.monthlyRent,
        )
    }


private fun CourtSaleDetail.toDetailSimpleInformationUiModel(): DetailSimpleInformationUiModel =
    DetailSimpleInformationUiModel(
        topTitle = salesAddress,
        imageList = salesPictures.map { ImageResource.Url(it.imageUrl) },
        isFavorite = false, /*TODO 서버랑 논의 필요*/
        numberOfFavorite = zzimCount,
        nameOfProduct = salesBuildingName,
        numberOfProduct = salesNumber,
        typeDisplayName = itemTypes.first(),
        size = exclusiveArea.exclusiveAreaToSize(),
        labelModels = labelModels(),
        lowestPrice = lowestSalesPrice,
        appraisalPrice = appraisalPrice,
        priceDiff = appraisalPrice - lowestSalesPrice,
        recentDealPrice = recentTransactionPrice,
        recentDealDate = recentTransactionDate, //최근실거래가?
        lastSaleDate = salesDateTime
    )

private fun CourtSaleDetail.labelModels(): List<SlugLabelUiModel> {
    //TODO : 어떤 칩들을 보여줄것인지?

    val chips = mutableListOf<SlugLabelUiModel>()

    //인증 매물
    if (verified)
        chips += SlugLabelUiModel(SlugLabelStyle.GradientBackground.Verified, "인증매물")
    //유찰 횟수
    if (failBidCount > 0)
        chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, "유찰 ${failBidCount}회")
    // 매각 여부
    if (isSoldOut) {
        chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, "매각완료")
    }
    // 매각 기한
    val leftDay: Int = getDaysLeftFromSalesDateTime(salesDateTime)
    //어디선가 정해져야함!
    chips += if (leftDay in 0..2) {
        SlugLabelUiModel(
            SlugLabelStyle.Dynamic(
                background = SlugLabelBackground.Solid(CriticalWeak),
                textColor = Critical
            ), "매각 D-$leftDay"
        )
    } else if (leftDay > 2) {
        SlugLabelUiModel(
            labelStyle = SlugLabelStyle.BuildingInfo.State,
            text = "매각 D-$leftDay"
        )
    } else {
        SlugLabelUiModel(
            labelStyle = SlugLabelStyle.BuildingInfo.State,
            text = "매각 D+${leftDay * -1}"
        )
    }

    return chips
}


fun getDaysLeftFromSalesDateTime(
    salesDateTime: String,
    now: LocalDateTime = LocalDateTime.now()
): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val target = LocalDateTime.parse(salesDateTime, formatter)
    val days = Duration.between(now, target).toDays().toInt()
    return days
}

private fun Double.exclusiveAreaToSize(): String {
    return "공급 ${this}㎡ (${(this / 3.306).toInt()}평)"
}


private fun String.toBooleanRaw(): Boolean {
    // 서버가 "있음", "없음", "있음(mock)" 같은 형태면
    // "있" 포함 여부로만 판단 (가장 raw에 가까운 단순 규칙)
    return contains("있")
}

private fun String.toOccupancyStatusRaw(): OccupancyStatus =
    when {
        // 서버가 enum-like로 내려주는 경우가 있으면 여기에 추가
        contains("점유중") -> OccupancyStatus.OCCUPIED
        contains("공실") -> OccupancyStatus.VACANT
        contains("없음") -> OccupancyStatus.NONE
        else -> OccupancyStatus.UNKNOWN
    }
