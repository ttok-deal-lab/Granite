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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.domain.sales.RecentTransaction
import com.estateslug.slug.home.component.tooltip.AlertSlugTooltip
import com.estateslug.slug.ui.component.SlugText
import com.estateslug.slug.ui.component.label.SlugLabelLarge
import com.estateslug.slug.ui.component.label.SlugLabelStyle
import com.estateslug.slug.ui.component.label.SlugLabelUiModel
import com.estateslug.slug.ui.component.label.VerifiedSlugLabelLarge
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
                recentDeal = uiModel.recentDeal,
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
    recentDeal: RecentTransaction,
    appraisalPrice: Long,
    lastSaleDate: String,
) {
    //TODO : i18n
    val displayLowestPrice = numberToCurrency(lowestPrice)
    val displayPriceDiff = numberToCurrency(priceDiff)
    val displayAppraisalPrice = numberToCurrency(appraisalPrice)
    val displayRecentDealPrice =
        if (recentDeal.isNone) stringResource(R.string.detail_recent_deal_none)
        else numberToCurrency(recentDeal.price)
    val percentageOfPriceDiff = priceDiff * 10000 / appraisalPrice / 100.0

    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = shape)
            .background(color = SlugTheme.colors.surfaceRaised)
            .border(shape = shape, width = 1.dp, color = SlugTheme.colors.outlineVariant)
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
                        color = SlugTheme.colors.neutralSubtler
                    )
                    AlertSlugTooltip("법원이 책정한 입찰을 시작할 수 있는 가장 낮은 가격이에요.")
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = displayLowestPrice,
                    style = SlugTypographyStyle.TitleMediumBold,
                    color = SlugTheme.colors.neutral
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
                        color = SlugTheme.colors.neutralSubtler
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = if (priceDiff == 0L) "-" else "$displayPriceDiff (${percentageOfPriceDiff}%)",
                        style = SlugTypographyStyle.BodyTinyMedium,
                        color = when {
                            priceDiff > 0 -> SlugTheme.colors.critical
                            priceDiff < 0 -> SlugTheme.colors.primary
                            else -> SlugTheme.colors.neutralSubtler
                        }
                    )
                }
            }
        }
        //아랫부분
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = SlugTheme.colors.surfaceInset)
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
                        color = SlugTheme.colors.neutralSubtler
                    )
                    AlertSlugTooltip("시세·입지·건물 상태를 기준으로 감정평가서가 책정한 가격이에요.")
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = displayAppraisalPrice,
                    style = SlugTypographyStyle.BodySmallMedium,
                    color = SlugTheme.colors.neutralSubtler
                )
            }
            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "최근실거래가",
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = SlugTheme.colors.neutralSubtler
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = recentDeal.date,
                        style = SlugTypographyStyle.BodyTinyRegular,
                        color = SlugTheme.colors.neutralSubtler
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = displayRecentDealPrice,
                    style = SlugTypographyStyle.BodySmallMedium,
                    color = SlugTheme.colors.neutralSubtler
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
                        color = SlugTheme.colors.neutralSubtler
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = lastSaleDate,
                    style = SlugTypographyStyle.BodySmallBold,
                    color = SlugTheme.colors.neutral
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
    typeDisplayName: SlugText,
    size: String,
    isFavorite: Boolean,
    numberOfFavorite: Int,
    likeClicked: () -> Unit,
) {
    val context = LocalContext.current
    fun toastIt(string: String) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    Row(modifier = Modifier.fillMaxWidth()) {
        // 제목 열이 남은 폭만 차지하게 weight — weight 없는 하트 열이 먼저 측정되고 제목은 그 안에서 줄바꿈된다.
        // 매물명이 길수록 하트(28dp) 폭이 눌려 0까지 줄던 문제(하트가 안 보이고 눌리지도 않음)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = nameOfProduct,
                style = SlugTypographyStyle.TitleLargeBold,
                color = SlugTheme.colors.neutral
            )
            Text(
                text = "${typeDisplayName()} | $size",
                style = SlugTypographyStyle.BodyMiniMedium,
                color = SlugTheme.colors.neutralSubtler
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
                    color = SlugTheme.colors.neutralSubtler
                )
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.ic_copy_18_18),
                    tint = SlugTheme.colors.iconUnselected,
                    contentDescription = "CopyProductNumber",
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.blockingClickable(onClick = likeClicked),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_heart),
                tint = if (isFavorite) SlugTheme.colors.criticalIcon else SlugTheme.colors.iconUnselected,
                contentDescription = "FavoriteIcon",
            )
            Text(
                text = numberOfFavorite.toString(),
                style = SlugTypographyStyle.BodyMicroMedium,
                color = SlugTheme.colors.neutralSubtler
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