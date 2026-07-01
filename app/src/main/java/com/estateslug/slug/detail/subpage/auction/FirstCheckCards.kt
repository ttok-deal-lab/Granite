package com.estateslug.slug.detail.subpage.auction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.detail.DetailBottomSheetType
import com.estateslug.slug.detail.subpage.ItemTitle
import com.estateslug.slug.ui.theme.CriticalLight
import com.estateslug.slug.ui.theme.CriticalSubtle
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralLight
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
fun FirstCheckCards(
    checkCardList: List<AuctionCardUiModel>,
    requestBottomSheet: (DetailBottomSheetType) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = NeutralInverted)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ItemTitle("한 눈에 보기")
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            checkCardList.forEach { data ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .blockingClickable(onClick = { requestBottomSheet(data.type) })
                        .background(
                            if (data.isCritical) CriticalLight else NeutralLight,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        data.name,
                        style = SlugTypographyStyle.BodyMicroMedium,
                        color = NeutralSubtler
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        data.value,
                        style = SlugTypographyStyle.BodySmallBold,
                        textAlign = TextAlign.Center,
                        color = if (data.isCritical) CriticalSubtle else NeutralSubtler
                    )
                }
            }
        }
    }
}

data class AuctionCardUiModel(
    val name: String,
    val value: String,
    val isCritical: Boolean,
    val type: DetailBottomSheetType
) {
    companion object {
        val preview = listOf(
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
    }
}

@Composable
@Preview
fun PreviewFirstCheckCards() {

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
    FirstCheckCards(checkCardList, {})
}
