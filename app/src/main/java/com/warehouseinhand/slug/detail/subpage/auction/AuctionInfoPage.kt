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
    uiModel: AuctionInfoUiModel,
    requestBottomSheet: (DetailBottomSheetType) -> Unit,
    onMapFocused: (Boolean) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        FirstCheckCards(
            checkCardList = uiModel.checkCardList,
            requestBottomSheet = requestBottomSheet
        )
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        AuctionHistory(uiModel.auctionHistoryUiModel)
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
//        RegistryInfo(uiModel = uiModel.registryInfoUiModel)// TODO : 1차 MVP 미포함
//        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
//        SalePropertyInfo(uiModel = salePropertyUiModel)
//        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        CourtInfo(uiModel = uiModel.courtInfoUiModel, onMapFocused = onMapFocused)
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        CourtDetailInfo(courtDetailInfo = uiModel.courtDetailInfo)
    }

}


@Composable
@Preview(widthDp = 360)
fun PreviewAuctionInfoPage() {
    AuctionInfoPage(uiModel = AuctionInfoUiModel.preview, { }, { })
}