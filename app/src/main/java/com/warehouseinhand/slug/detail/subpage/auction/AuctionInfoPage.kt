package com.warehouseinhand.slug.detail.subpage.auction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.detail.DetailBottomSheetType
import com.warehouseinhand.slug.ui.theme.NeutralWeak

@Composable
fun AuctionInfoPage(
    requestBottomSheet: (DetailBottomSheetType) -> Unit,
    onMapFocused: (Boolean) -> Unit
) {
    val checkCardList = listOf(
        AuctionCardUiModel(
            name = "경매구분", value = "강제경매", isCritical = true,
            DetailBottomSheetType.InfoSheetType.TypeOfAuction
        ),
        AuctionCardUiModel(
            name = "임차인", value = "대항력 있음", isCritical = true,
            DetailBottomSheetType.InfoSheetType.Lessee
        ),
        AuctionCardUiModel(
            name = "채권자", value = "5명", isCritical = false,
            DetailBottomSheetType.InfoSheetType.Creditor
        ),
    )

//    val salePropertyUiModel: SalePropertyInfoUiModel = SalePropertyInfoUiModel.preview
    val auctionHistoryUiModel: AuctionHistoryUiModel = AuctionHistoryUiModel.preview

    val courtInfoUiModel = CourtInfoUiModel.preview
    val registryInfoUiModel = RegistryInfoUiModel.preview

    val courtDetailInfo = "1동의 건물의 표시\n" +
            "서울특별시 서초구 서초동 1557-8\n" +
            "주건축물\n" +
            "[도로명주소]\n" +
            "서울특별시 서초구 반포대로 26길 35\n" +
            "철근콘크리트구조 철근콘크리트지붕\n" +
            "6층 공동주택\n" +
            "지하 2층 199.52㎡\n" +
            "지하 1층 209.17㎡\n" +
            "1층 44.34㎡\n" +
            "1층 38.03㎡\n" +
            "1층 16.66㎡\n" +
            "1층 9.06㎡\n" +
            "2층 202.04㎡\n" +
            "3층 164.06㎡\n" +
            "4층 162.48㎡\n" +
            "5층 139.15㎡\n" +
            "6층 129.96㎡\n" +
            "\n" +
            "전유부분의 건물의 표시\n"

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        FirstCheckCards(
            checkCardList = checkCardList,
            requestBottomSheet = requestBottomSheet
        )
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        AuctionHistory(auctionHistoryUiModel.copy())
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        RegistryInfo(uiModel = registryInfoUiModel)
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
//        SalePropertyInfo(uiModel = salePropertyUiModel)
//        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        CourtInfo(uiModel = courtInfoUiModel, onMapFocused = onMapFocused)
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        CourtDetailInfo(courtDetailInfo = courtDetailInfo)
    }

}


@Composable
@Preview(widthDp = 360)
fun PreviewAuctionInfoPage() {
    AuctionInfoPage({ }, {  })
}