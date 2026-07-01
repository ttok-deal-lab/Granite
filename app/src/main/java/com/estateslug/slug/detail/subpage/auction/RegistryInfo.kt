package com.estateslug.slug.detail.subpage.auction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.detail.subpage.ItemInfo
import com.estateslug.slug.detail.subpage.ItemTitle
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
fun RegistryInfo(uiModel: RegistryInfoUiModel) {
    Column(
        modifier = Modifier
            .background(NeutralInverted)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ItemTitle("등기부 현황")
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "매각효력",
                style = SlugTypographyStyle.BodyMediumMedium,
                color = NeutralSubtler
            )
            //TODO : 클릭이벤트 물어보기!!
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiModel.saleEffect,
                    style = SlugTypographyStyle.BodyMediumMedium,
                    color = Neutral
                )
                ImageProcessor(
                    modifier = Modifier.size(16.dp),
                    imageResource = ImageResource.Id(id = R.drawable.arrow_right_16_16)
                )

            }
        }
        Spacer(Modifier.height(20.dp))
        ItemInfo(name = "지상권", value = uiModel.surfaceRights)
    }
}

data class RegistryInfoUiModel(
    val saleEffect: String,    // 예: "매각효력 있음"
    val surfaceRights: String  // 예: "해당사항 없음"
) {
    companion object {
        val preview = RegistryInfoUiModel(
            saleEffect = "매각효력 있음",
            surfaceRights = "해당사항 없음"
        )
    }
}

@Preview
@Composable
fun PreviewRegistryInfo() {
    val uiModel = RegistryInfoUiModel.preview
    RegistryInfo(uiModel)
}