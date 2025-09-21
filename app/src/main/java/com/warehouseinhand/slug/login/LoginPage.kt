package com.warehouseinhand.slug.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.login.sns.SocialLoginType
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.component.login.SocialLoginButton


@Composable
fun LoginPage(
    onSocialLoginSelected: (SocialLoginType) -> Unit,
    lastUsedLoginType: SocialLoginType,
) {
    Surface {
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.weight(2f))
                ImageProcessor(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .aspectRatio(0.6f)
                        .requiredWidthIn(max = 150.dp),
                    imageResource = ImageResource.Id(R.drawable.logo_color_meta_typo_sub)
                )
                Spacer(Modifier.weight(1f))
            }
            SocialLoginButtons(
                onSocialLoginSelected = onSocialLoginSelected,
                lastUsedLoginType = lastUsedLoginType
            )
        }

    }
}

@Composable
private fun SocialLoginButtons(
    onSocialLoginSelected: (SocialLoginType) -> Unit,
    lastUsedLoginType: SocialLoginType
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SocialLoginUIModel.entries.forEach { uiModel ->
            SocialLoginButton(
                uiModel = uiModel,
                onSocialLoginSelected = onSocialLoginSelected,
                isLastLoginType = uiModel.type == lastUsedLoginType
            )
        }
    }
}

@Composable
@Preview()
fun PreviewLoginPage() {
    LoginPage(
        onSocialLoginSelected = {},
        lastUsedLoginType = SocialLoginType.NAVER
    )
}