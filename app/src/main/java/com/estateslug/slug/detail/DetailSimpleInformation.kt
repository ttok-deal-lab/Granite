package com.estateslug.slug.detail

import android.widget.Toast
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.home.component.tooltip.AlertSlugTooltip
import com.estateslug.slug.ui.component.label.SlugLabelLarge
import com.estateslug.slug.ui.component.label.SlugLabelStyle
import com.estateslug.slug.ui.component.label.SlugLabelUiModel
import com.estateslug.slug.ui.component.label.VerifiedSlugLabelLarge
import com.estateslug.slug.ui.theme.Critical
import com.estateslug.slug.ui.theme.Gray150
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralLight
import com.estateslug.slug.ui.theme.NeutralMuted
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.Primary
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.ClipBoardModule
import com.estateslug.slug.util.blockingClickable
import com.estateslug.slug.util.numberToCurrency

//TODO : 각 리소스 별 Description 처리 할것.
//TODO : home Top bar와 공통 컴포넌트화 할지 고민

@Composable
fun DetailSimpleInformation(
    uiModel: DetailSimpleInformationUiModel,
    likeClicked: () -> Unit,
) {
    Column {
        if (uiModel.imageList.isNotEmpty()) {
            DetailPageTopImagePager(uiModel.imageList)
        }
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NameAndLike(
                nameOfProduct = uiModel.nameOfProduct,
                numberOfProduct = uiModel.numberOfProduct,
                typeDisplayName = uiModel.typeDisplayName,
                size = uiModel.size,
                isFavorite = uiModel.isFavorite,
                numberOfFavorite = uiModel.numberOfFavorite,
                likeClicked = likeClicked
            )
            LabelList(labelModels = uiModel.labelModels)
            RecentAuctionPrice(
                lowestPrice = uiModel.lowestPrice,
                priceDiff = uiModel.priceDiff,
                recentDealPrice = uiModel.recentDealPrice,
                recentDealDate = uiModel.recentDealDate,
                lastSaleDate = uiModel.lastSaleDate,
                appraisalPrice = uiModel.appraisalPrice
            )
        }
    }

}

@Composable
private fun RecentAuctionPrice(
    lowestPrice: Long,
    priceDiff: Long,
    recentDealPrice: Long,
    appraisalPrice: Long,
    recentDealDate: String,
    lastSaleDate: String,
) {
    //TODO : i18n
    val displayLowestPrice = numberToCurrency(lowestPrice)
    val displayPriceDiff = numberToCurrency(priceDiff)
    val displayAppraisalPrice = numberToCurrency(appraisalPrice)
    val displayRecentDealPrice = numberToCurrency(recentDealPrice)
    val percentageOfPriceDiff = priceDiff * 10000 / appraisalPrice / 100.0

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
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = if (priceDiff == 0L) "-" else "$displayPriceDiff (${percentageOfPriceDiff}%)",
                        style = SlugTypographyStyle.BodyTinyMedium,
                        color = when {
                            priceDiff > 0 -> Critical
                            priceDiff < 0 -> Primary
                            else -> NeutralSubtler
                        }
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
                    text = displayAppraisalPrice,
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
private fun LabelList(labelModels: List<SlugLabelUiModel>) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        labelModels.forEach { uiModel ->
            if (uiModel.labelStyle is SlugLabelStyle.GradientBackground.Verified) {
                VerifiedSlugLabelLarge(uiModel = uiModel)
            } else
                SlugLabelLarge(uiModel = uiModel)
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
    likeClicked: () -> Unit,
) {
    val context = LocalContext.current
    fun toastIt(string: String) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
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
                    ClipBoardModule.addTextToClipBoard(
                        context = context,
                        textToClipData = numberOfProduct
                    )
                    toastIt("매물번호가 복사 되었습니다.\n$numberOfProduct")

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
                    tint = NeutralMuted,
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
        Surface {
            DetailSimpleInformation(
                uiModel = DetailSimpleInformationUiModel.preview,
                likeClicked = {}
            )
        }
    }
}