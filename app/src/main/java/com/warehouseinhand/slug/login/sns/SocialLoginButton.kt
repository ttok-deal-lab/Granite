package com.warehouseinhand.slug.login.sns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.login.SocialLoginUIModel
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle


@Composable
fun SocialLoginButton(
    uiModel: SocialLoginUIModel,
    onSocialLoginSelected: (SocialLoginType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 50.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color = uiModel.backgroundColor)
            .clickable {
                onSocialLoginSelected(uiModel.type)
            },
        contentAlignment = Alignment.Center,
    ) {
        //icon 필수! 개별 SNS 로그인 버튼 가이드 라인 볼것!
        Text(
            text = stringResource(id = uiModel.localizedName),
            style = SlugTypographyStyle.SocialLoginButton
                .copy(color = uiModel.textColor),
        )
    }
}

@Composable
@Preview
fun PreviewSocialLoginButtons() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SocialLoginUIModel.entries.forEach {
            SocialLoginButton(it, {})
        }
    }
}