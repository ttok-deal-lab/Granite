package com.estateslug.slug.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.estateslug.slug.R
import com.estateslug.slug.data.favorite.FavoriteStateStore
import com.estateslug.slug.data.local.recent.RecentItemRepository
import com.estateslug.slug.data.network.sales.RemoteSalesDataRepository
import com.estateslug.slug.detail.navigation.RouteDetail
import com.estateslug.slug.detail.subpage.LesseeInfo
import com.estateslug.slug.detail.subpage.OccupancyStatus
import com.estateslug.slug.detail.subpage.auction.AuctionCardUiModel
import com.estateslug.slug.detail.subpage.auction.AuctionHistoryUiModel
import com.estateslug.slug.detail.subpage.auction.AuctionInfoUiModel
import com.estateslug.slug.detail.subpage.auction.AuctionResult
import com.estateslug.slug.detail.subpage.auction.AuctionRound
import com.estateslug.slug.detail.subpage.auction.CourtInfoUiModel
import com.estateslug.slug.detail.subpage.auction.courtDisplayName
import com.estateslug.slug.detail.subpage.auction.RegistryInfoUiModel
import com.estateslug.slug.domain.sales.CourtSaleDetail
import com.estateslug.slug.domain.user.GetFavoriteStatusUseCase
import com.estateslug.slug.home.ProductItemUiModel
import com.estateslug.slug.ui.component.SlugText
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.component.label.SlugLabelBackground
import com.estateslug.slug.ui.component.label.SlugLabelStyle
import com.estateslug.slug.ui.component.label.SlugLabelUiModel
import com.estateslug.slug.ui.theme.Critical
import com.estateslug.slug.ui.theme.CriticalWeak
import com.estateslug.slug.util.calculateDaysLeft
import com.estateslug.slug.util.extractDateFromDateAndTime
import com.estateslug.slug.util.extractTimeHHmm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DetailedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val remoteSalesDataRepository: RemoteSalesDataRepository,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    private val recentItemRepository: RecentItemRepository,
    private val favoriteStateStore: FavoriteStateStore,
) : ViewModel() {
    // raw 상태는 서버 진실 그대로 유지하고, 관심 오버레이는 방출 시점에 병합한다
    private val _uiState: MutableStateFlow<CourtSaleDetailUiState> =
        MutableStateFlow(CourtSaleDetailUiState.preview)
    val uiState: StateFlow<CourtSaleDetailUiState> =
        combine(_uiState, favoriteStateStore.overrides) { state, overrides ->
            state.withFavoriteOverride(overrides[state.productId])
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _uiState.value)

    private var lastRequestedId: String = ""
    private var lastDetail: CourtSaleDetail? = null
    private var lastServerFavorite: Boolean = false

    init {
        // typed route 인자가 SavedStateHandle에 실려 process death 후에도 복원된다.
        // toRoute라 RouteDetail 필드명 변경도 컴파일 타임에 잡힌다.
        // requestData는 public 유지 — 백스택 엔트리 없이 id를 꽂는 호스트(추후 2-pane)의 진입 경로
        requestData(savedStateHandle.toRoute<RouteDetail>().productId)
    }

    fun requestData(id: String) {
        lastRequestedId = id
        _uiState.update { it.copy(isLoading = true, isError = false) }
        viewModelScope.launch(Dispatchers.IO) {
            remoteSalesDataRepository.getCourtSaleDetail(id)
                .onSuccess { detail ->

                    // 상세 데이터가 정상적으로 내려온 경우에만 최근 본 목록에 추가
                    // (기존: 홈 아이템 클릭 시점 → 변경: 상세 진입/딥링크 진입 모두 커버)
                    recentItemRepository.addRecentItem(id)

                    val favoriteStatus = getFavoriteStatusUseCase(id)
                        .getOrDefault(false)
                    lastDetail = detail
                    lastServerFavorite = favoriteStatus
                    favoriteStateStore.onServerStateObserved(mapOf(id to favoriteStatus))

                    val lessees = detail.toLesseesRaw()
                    val detailSimpleInformation: DetailSimpleInformationUiModel =
                        detail.toDetailSimpleInformationUiModel(isFavorite = favoriteStatus)
                    val auctionInfo = detail.toAuctionInfoUiModel()

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productId = id,
                            listOfLessees = lessees,
                            detailSimpleInformation = detailSimpleInformation,
                            auctionInfo = auctionInfo,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
        }
    }

    fun retry() = requestData(lastRequestedId)

    fun onLikeChangeRequest() {
        val current = uiState.value
        if (current.productId.isBlank()) return
        favoriteStateStore.setFavorite(
            productId = current.productId,
            isFavorite = !current.detailSimpleInformation.isFavorite,
            snapshot = lastDetail?.toProductItemSnapshot(
                id = current.productId,
                serverFavorite = lastServerFavorite,
            ),
        )
    }
}

private fun CourtSaleDetailUiState.withFavoriteOverride(override: Boolean?): CourtSaleDetailUiState {
    if (override == null || productId.isBlank()) return this
    val info = detailSimpleInformation
    if (override == info.isFavorite) return this
    return copy(
        detailSimpleInformation = info.copy(
            isFavorite = override,
            numberOfFavorite = (info.numberOfFavorite + if (override) 1 else -1).coerceAtLeast(0),
        )
    )
}

/** 관심탭 prepend용 스냅샷 — 서버 재조회 없이 목록 행을 구성한다 */
private fun CourtSaleDetail.toProductItemSnapshot(
    id: String,
    serverFavorite: Boolean,
): ProductItemUiModel =
    ProductItemUiModel(
        id = id,
        priceOfProduct = appraisalPrice,
        nameOfProduct = salesBuildingName,
        location = salesAddress,
        daysLeft = calculateDaysLeft(salesDateTime, System.currentTimeMillis()),
        buildingImage = salesPictures.firstOrNull()?.let { ImageResource.Url(it.imageUrl) }
            ?: ImageResource.Id(R.drawable.logo_metaopo),
        isFavorite = true,
        // fetch 시점 zzimCount에 이 사용자가 이미 포함돼 있었는지에 따라 보정
        favoritePersons = (zzimCount + if (serverFavorite) 0 else 1).toLong(),
        infoChipList = buildSnapshotChips(),
    )

private fun CourtSaleDetail.buildSnapshotChips(): List<SlugLabelUiModel> {
    val chips = mutableListOf<SlugLabelUiModel>()
    if (verified)
        chips += SlugLabelUiModel(SlugLabelStyle.GradientBackground.Verified, SlugText.Text("인증매물"))
    if (isSoldOut)
        chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("매각완료"))
    else if (failBidCount > 0)
        chips += SlugLabelUiModel(
            SlugLabelStyle.BuildingInfo.State,
            SlugText.Text("유찰 ${failBidCount}회")
        )
    salesCategories.firstOrNull()?.let { category ->
        chips += SlugLabelUiModel(
            SlugLabelStyle.BuildingInfo.BuildingType,
            ProductItemUiModel.getStringFromCategory(category),
        )
    }
    return chips
}

data class CourtSaleDetailUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val productId: String = "",
    val detailSimpleInformation: DetailSimpleInformationUiModel,
    val listOfLessees: List<LesseeInfo>,
    val auctionInfo: AuctionInfoUiModel,
) {
    companion object {
        val preview = CourtSaleDetailUiState(
            isLoading = true,
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
        courtDetailInfo = if (salesItemDetails.isEmpty()) "" else salesItemDetails.joinToString("\n") { it.content }
    )


private fun CourtSaleDetail.toCHeckCardList(): List<AuctionCardUiModel> = listOf(
    AuctionCardUiModel(
        name = "경매구분", value = caseName, isCritical = true,
        DetailBottomSheetType.InfoSheetType.TypeOfAuction
    ),
    AuctionCardUiModel(
        name = "임차인", value = "대항력 있음", isCritical = true,
        DetailBottomSheetType.InfoSheetType.Lessee
    ),
    AuctionCardUiModel(
        name = "채권자", value = "${creditorCount}명", isCritical = false,
        DetailBottomSheetType.InfoSheetType.Creditor
    ),
)

private fun CourtSaleDetail.toAuctionHistoryUiModel(): AuctionHistoryUiModel =
    AuctionHistoryUiModel(
        auctionStartDate = salesOpenDate.extractDateFromDateAndTime(),
        dividendDeadline = distributionRequiredDeadlineDate.extractDateFromDateAndTime(),
        appraisalDate = conditionReport.investigationDate.extractDateFromDateAndTime(),
        rounds = salesDetails.let {
            it.size
            it.mapIndexed { index, detail ->
                AuctionRound(
                    round = it.size - index,
                    date = detail.timeStamp.extractDateFromDateAndTime(),
                    minSalePrice = detail.leastSalesPrice,
                    result = AuctionResult.fromServer(detail.result),
                )
            }
        }
    )


private fun CourtSaleDetail.toCourtInfoUiModel(): CourtInfoUiModel =
    CourtInfoUiModel(
        // 서버 enum name → 한글 리소스, 미등록 값은 원문 그대로
        courtName = courtDisplayName(court?.name ?: courtCode),
        courtTeam = courtTeam,
        courtAddress = court?.address ?: "",
        saleDate = salesDateTime.extractDateFromDateAndTime(),
        bidTime = salesDetails.first().timeStamp.extractTimeHHmm(),
        // index 0이 항상 최신 회차 — 개찰 시각은 최신 회차 기준으로 표시
        openingTime = salesDetails.first().bidOpeningTimeStamp.extractTimeHHmm(),
        lat = court?.latitude ?: 0.0,
        lng = court?.longitude ?: 0.0,
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


private fun CourtSaleDetail.toDetailSimpleInformationUiModel(isFavorite: Boolean = false): DetailSimpleInformationUiModel =
    DetailSimpleInformationUiModel(
        topTitle = salesAddress,
        imageList = salesPictures.map { ImageResource.Url(it.imageUrl) },
        isFavorite = isFavorite,
        numberOfFavorite = zzimCount,
        nameOfProduct = salesBuildingName,
        numberOfProduct = salesNumber,
        typeDisplayName = getStringFromCategory(salesCategories.first()),
        size = exclusiveArea.exclusiveAreaToSize(),
        labelModels = labelModels(),
        lowestPrice = lowestSalesPrice,
        appraisalPrice = appraisalPrice,
        priceDiff = appraisalPrice - lowestSalesPrice,
        recentDealPrice = recentTransactionPrice,
        recentDealDate = recentTransactionDate, //최근실거래가?
        lastSaleDate = salesDateTime
    )

private fun getStringFromCategory(text: String): SlugText =
    when (text) {
        "VILLA" -> SlugText.Id(R.string.building_type_villa)
        "APARTMENT" -> SlugText.Id(R.string.building_type_apartment)
        "OFFICETEL" -> SlugText.Id(R.string.building_type_officetel)
        "SHOP_HOUSE" -> SlugText.Id(R.string.building_type_commercial_house)
        "HOUSING" -> SlugText.Id(R.string.building_type_house)
        else -> SlugText.Id(R.string.building_type_other)
        //논의에 따라 정해지지 않았던 타입들은 전부 기타로 처리하기로함. 2026/05/24
//                else -> SlugText.Text(text)
    }//TODO : ProductItemUiModel 참고해서 공통화할것.


private fun CourtSaleDetail.labelModels(): List<SlugLabelUiModel> {
    //TODO : 어떤 칩들을 보여줄것인지?

    val chips = mutableListOf<SlugLabelUiModel>()

    //인증 매물
    if (verified)
        chips += SlugLabelUiModel(SlugLabelStyle.GradientBackground.Verified, SlugText.Text("인증매물"))
    //유찰 횟수
    if (failBidCount > 0)
        chips += SlugLabelUiModel(
            SlugLabelStyle.BuildingInfo.State,
            SlugText.Text("유찰 ${failBidCount}회")
        )
    // 매각 여부
    if (isSoldOut) {
        chips += SlugLabelUiModel(SlugLabelStyle.BuildingInfo.State, SlugText.Text("매각완료"))
    }
    // 매각 기한
    val leftDay: Int = getDaysLeftFromSalesDateTime(salesDateTime)
    //어디선가 정해져야함!
    chips += if (leftDay in 0..2) {
        SlugLabelUiModel(
            SlugLabelStyle.Dynamic(
                background = SlugLabelBackground.Solid(CriticalWeak),
                textColor = Critical
            ), SlugText.Text("매각 D-$leftDay")
        )
    } else if (leftDay > 2) {
        SlugLabelUiModel(
            labelStyle = SlugLabelStyle.BuildingInfo.State,
            text = SlugText.Text("매각 D-$leftDay")
        )
    } else {
        SlugLabelUiModel(
            labelStyle = SlugLabelStyle.BuildingInfo.State,
            text = SlugText.Text("매각 D+${leftDay * -1}")
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
