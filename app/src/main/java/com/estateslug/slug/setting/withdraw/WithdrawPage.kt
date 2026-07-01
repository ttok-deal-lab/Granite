package com.estateslug.slug.setting.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.setting.SettingTopBar
import com.estateslug.slug.ui.component.button.basic.BasicTextButton
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
fun WithdrawPage(onBackClick: () -> Unit, onWithDrawClick: () -> Unit) {
    val withDrawTitle = "탈퇴하시나요?\n탈퇴 전 안내 사항을 반드시 확인해주세요."
    val withDrawSubDataRemove = "고객님의 개인정보는 개인정보처리 방침에 따라 안전하게 삭제돼요."
    val withDrawSubCanNotReUseId = "사용하신 아이디는 다시 사용할 수 없어요."
    val withDrawCheckDescriptionAllChecked = "탈퇴 전 안내 사항을 모두 확인했어요."
    val withDrawButtonText = "탈퇴하기"
    var isAllChecked by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        SettingTopBar(onBackClick = onBackClick)
        Column(
            modifier = Modifier
                .background(NeutralInverted)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(withDrawTitle, style = SlugTypographyStyle.TitleLargeBold, color = Neutral)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row {
                    Text(
                        text = " • ",
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                    Text(
                        text = withDrawSubDataRemove,
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                }
                Row {
                    Text(
                        text = " • ",
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                    Text(
                        text = withDrawSubCanNotReUseId,
                        style = SlugTypographyStyle.BodySmallMedium,
                        color = NeutralSubtler
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(NeutralInverted)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .blockingClickable(onClick = { isAllChecked = !isAllChecked })
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAllChecked)
                    ImageProcessor(imageResource = ImageResource.Id(R.drawable.ic_check_selected_24_24))
                else
                    ImageProcessor(imageResource = ImageResource.Id(R.drawable.ic_check_unselected_24_24))
                Spacer(Modifier.width(16.dp))
                Text(
                    withDrawCheckDescriptionAllChecked,
                    style = SlugTypographyStyle.BodyMediumMedium,
                    color = Neutral
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(20.dp)) {
                BasicTextButton(
                    isDisabled = !isAllChecked,
                    buttonText = withDrawButtonText,
                    onButtonClick = onWithDrawClick
                )
            }
        }
    }

}

@Composable
@Preview
fun PreviewWithdrawPage() {
    Surface {
        WithdrawPage({},{})
    }
}