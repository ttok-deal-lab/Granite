package com.warehouseinhand.slug.ui.component.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.login.SocialLoginUIModel
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.ui.theme.PrimaryWhite
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle


@Composable
fun SocialLoginButton(
    uiModel: SocialLoginUIModel,
    onSocialLoginSelected: (SocialLoginType) -> Unit
) {
    val buttonShape = RoundedCornerShape(52.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(min = 335.dp)
            .height(height = 52.dp)
            .clip(shape = buttonShape)
            .background(color = uiModel.backgroundColor)
            .border(width = 1.dp, color = uiModel.borderColor, shape = buttonShape)
            .clickable {
                onSocialLoginSelected(uiModel.type)
            }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        //icon 필수! 개별 SNS 로그인 버튼 가이드 라인 볼것!
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(uiModel.iconId),
                contentDescription = ""
            )
        }
        Text(
            text = stringResource(id = uiModel.loginSNSText),
            style = SlugTypographyStyle.BodyMediumMedium
                .copy(color = uiModel.textColor),
        )
    }
}

@Composable
@Preview()
fun PreviewSocialLoginButtons() {
    Column(
        modifier = Modifier
            .background(color = PrimaryWhite)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialLoginUIModel.entries.forEach {
            SocialLoginButton(it, {})
        }
    }
}