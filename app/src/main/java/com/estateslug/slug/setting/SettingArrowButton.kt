package com.estateslug.slug.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
internal fun SettingButton(
    buttonText: String,
    rightSlot: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(NeutralInverted)
            .blockingClickable(onClick = onClick)
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = buttonText, style = SlugTypographyStyle.BodyLargeMedium, color = Neutral)
            rightSlot()
        }
        Spacer(Modifier.width(4.dp))
    }
}

@Composable
internal fun ArrowSettingButton(
    buttonText: String,
    rightSlot: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(NeutralInverted)
            .blockingClickable(onClick = onClick)
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = buttonText, style = SlugTypographyStyle.BodyLargeMedium, color = Neutral)
            rightSlot()
        }
        ImageProcessor(
            modifier = Modifier.size(16.dp),
            imageResource = ImageResource.Id(R.drawable.arrow_right_16_16)
        )
        Spacer(Modifier.width(4.dp))
    }
}

@Composable
@Preview

private fun PreviewMyPageButton() {
    SlugTheme {
        Surface {
            Column {
                SettingButton(buttonText = "알림 설정 관리", onClick = {})

                ArrowSettingButton(buttonText = "알림 설정 관리", onClick = {})
                SettingButton(buttonText = "앱 버전", rightSlot = {
                    Row {
                        Text("최신 버전입니다.")
                        Spacer(Modifier.width(12.dp))
                    }
                }, onClick = {})
                ArrowSettingButton(
                    buttonText = "앱 버전",
                    rightSlot = {
                        Row {
                            Text("업데이트 하기")
                            Spacer(Modifier.width(12.dp))
                        }
                    },
                    onClick = {},
                )
            }
        }
    }


}