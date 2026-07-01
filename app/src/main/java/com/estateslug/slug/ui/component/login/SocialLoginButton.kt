package com.estateslug.slug.ui.component.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.login.SocialLoginUIModel
import com.estateslug.slug.login.sns.SocialLoginType
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.PrimaryWhite
import com.estateslug.slug.ui.theme.SlugTypographyStyle


@Composable
fun SocialLoginButton( //TODO : 해당 Feature로 옮기는 것 고민 할것!
    uiModel: SocialLoginUIModel,
    onSocialLoginSelected: (SocialLoginType) -> Unit,
    isLastLoginType: Boolean = false
) {
    val buttonShape = RoundedCornerShape(52.dp)
    Box {
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
                text = stringResource(id = uiModel.loginSNSText) + "로 로그인",
                style = SlugTypographyStyle.BodyMediumMedium
                    .copy(color = uiModel.textColor),
            )
        }

        if (isLastLoginType) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .absoluteOffset(x = 6.dp)
            ) {
                LastLoginTypeText()
            }
        }
    }

}

@Composable
fun LastLoginTypeText() {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier.size(width = 5.dp, height = 7.dp)
        ) {
            val path = Path().apply {
                moveTo(size.width, 0f)
                lineTo(0f, size.height / 2f)
                lineTo(size.width, size.height)
            }
            drawPath(
                path = path,
                color = Neutral
            )
        }
        Box(
            modifier = Modifier
                .background(shape = RoundedCornerShape(6.dp), color = Neutral)
                .padding(vertical = 4.dp, horizontal = 8.dp),
        ) {
            Text(
                text = "최근 로그인",
                style = SlugTypographyStyle.CaptionMediumMedium,
                color = NeutralInverted
            )//TODO : i18n
        }
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
        SocialLoginButton(SocialLoginUIModel.GOOGLE, {}, isLastLoginType = true)
        SocialLoginUIModel.entries.forEach {
            SocialLoginButton(it, {}, isLastLoginType = false)
        }

    }
}