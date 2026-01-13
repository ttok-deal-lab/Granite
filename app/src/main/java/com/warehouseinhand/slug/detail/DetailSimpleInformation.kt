package com.warehouseinhand.slug.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.component.tooltip.AlertSlugTooltip
import com.warehouseinhand.slug.ui.component.label.SlugLabelBackground
import com.warehouseinhand.slug.ui.component.label.SlugLabelLarge
import com.warehouseinhand.slug.ui.component.label.SlugLabelStyle
import com.warehouseinhand.slug.ui.component.label.VerifiedSlugLabelLarge
import com.warehouseinhand.slug.ui.theme.Critical
import com.warehouseinhand.slug.ui.theme.CriticalWeak
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
import com.warehouseinhand.slug.util.numberToCurrency

//TODO : 각 리소스 별 Description 처리 할것.
//TODO : home Top bar와 공통 컴포넌트화 할지 고민
@Composable
fun DetailSimpleInformation(
    itemNumberCopyRequest: () -> Unit,
    likeClicked: () -> Unit,
) {
    val isFavorite = false
    val numberOfFavorite = 100

    val nameOfProduct: String = "신촌금호2단지"
    val numberOfProduct: String = "2023타경 102411"
    val typeDisplayName: String = "아파트"
    val size: String = "공급 110.52㎡ (33평)"
    val labelModels: List<Pair<SlugLabelStyle, String>> = listOf(
        SlugLabelStyle.GradientBackground.Verified to "인증매물",
        SlugLabelStyle.BuildingInfo.State to "유찰 2회",
        SlugLabelStyle.Dynamic(
            background = SlugLabelBackground.Solid(CriticalWeak),
            textColor = Critical
        ) to "매각 D-2",
        SlugLabelStyle.BuildingInfo.State to "매각 D-4",
    )

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NameAndLike(
            nameOfProduct = nameOfProduct,
            numberOfProduct = numberOfProduct,
            typeDisplayName = typeDisplayName,
            size = size,
            isFavorite = isFavorite,
            numberOfFavorite = numberOfFavorite,
            itemNumberCopyRequest = itemNumberCopyRequest,
            likeClicked = likeClicked
        )
        LabelList(labelModels = labelModels)
        RecentAuctionPrice()
        //info
    }
}

@Composable
private fun RecentAuctionPrice() {
    //TODO : i18n

    val lowestPrice = 183200000L
    val priceDiff = -48000000L
    val recentDealPrice = 554210000L
    val recentDealDate = "25.03.16"
    val lastSaleDate = "2025.04.08 10:00"

    val displayLowestPrice = remember { numberToCurrency(lowestPrice) }
    val displayPriceDiff = remember { numberToCurrency(priceDiff) }
    val displayRecentDealPrice = remember { numberToCurrency(recentDealPrice) }

    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = shape)
            .background(color = NeutralInverted)
            .border(shape = shape, width = 1.dp, color = Gray150)
    ) {
        //윗부분
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "최저매각가격",
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                    AlertSlugTooltip("법원이 책정한 입찰을 시작할 수 있는 가장 낮은 가격이에요.")
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = displayLowestPrice,
                    style = SlugTypographyStyle.TitleMediumBold,
                    color = Neutral
                )
            }
            Spacer(Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "감정가대비",
                        style = SlugTypographyStyle.BodyTinyMedium,
                        color = NeutralSubtler
                    )
                    Text(
                        text = displayPriceDiff,
                        style = SlugTypographyStyle.BodyTinyMedium,
                        color = if (priceDiff >= 0) Critical else Primary
                    )
                }
            }
        }
        //아랫부분
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = NeutralLight)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "감정가",
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                    AlertSlugTooltip("시세·입지·건물 상태를 기준으로 감정평가서가 책정한 가격이에요.")
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = displayLowestPrice,
                    style = SlugTypographyStyle.BodySmallMedium,
                    color = NeutralSubtler
                )
            }
            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "최근실거래가",
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = recentDealDate,
                        style = SlugTypographyStyle.BodyTinyRegular,
                        color = NeutralSubtler
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = displayRecentDealPrice,
                    style = SlugTypographyStyle.BodySmallMedium,
                    color = NeutralSubtler
                )
            }
            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "매각기일",
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = lastSaleDate,
                    style = SlugTypographyStyle.BodySmallBold,
                    color = Neutral
                )
            }
        }
    }
}

@Composable
private fun LabelList(labelModels: List<Pair<SlugLabelStyle, String>>) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        labelModels.forEach { (style, string) ->
            if (style is SlugLabelStyle.GradientBackground.Verified) {
                VerifiedSlugLabelLarge(labelStyle = style, text = string)
            } else
                SlugLabelLarge(labelStyle = style, text = string)
        }
    }
}

//TODO : 더 나은 이름 생각해보기.
@Composable
private fun NameAndLike(
    nameOfProduct: String,
    numberOfProduct: String,
    typeDisplayName: String,
    size: String,
    isFavorite: Boolean,
    numberOfFavorite: Int,
    itemNumberCopyRequest: () -> Unit,
    likeClicked: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = nameOfProduct,
                style = SlugTypographyStyle.TitleLargeBold,
                color = Neutral
            )
            Text(
                text = "$typeDisplayName | $size",
                style = SlugTypographyStyle.BodyMiniMedium,
                color = NeutralSubtler
            )
            Row(
                modifier = Modifier.blockingClickable(onClick = {
                    itemNumberCopyRequest()
                }),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "매물번호 $numberOfProduct",//TODO : i18n
                    style = SlugTypographyStyle.BodyMiniMedium,
                    color = NeutralSubtler
                )
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.ic_copy_18_18),
                    tint = if (isFavorite) Critical else NeutralMuted,
                    contentDescription = "CopyProductNumber",
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Column(
            modifier = Modifier.blockingClickable(onClick = likeClicked),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_heart),
                tint = if (isFavorite) Critical else NeutralMuted,
                contentDescription = "FavoriteIcon",
            )
            Text(
                text = numberOfFavorite.toString(),
                style = SlugTypographyStyle.BodyMicroMedium,
                color = NeutralSubtler
            )
        }

    }
}

@Composable
@Preview
fun PreviewDetailSimpleInformation() {
    SlugTheme {
        Surface() {

            DetailSimpleInformation(
                itemNumberCopyRequest = {},
                likeClicked = {}
            )
        }
    }
}