package com.warehouseinhand.slug.mypage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.login.SocialLoginUIModel
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralContrast
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.SlugTheme
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable
import com.warehouseinhand.slug.util.startSettingActivity

@Composable
fun MyPageScreen(
//    onSettingClick: () -> Unit,
    userName: String,
    socialLoginUIModel: SocialLoginUIModel,
    onRecentViewClicked: () -> Unit,
    onInquiryClicked: () -> Unit
) {
    val currentContext = LocalContext.current
    val onSettingClick = { startSettingActivity(currentContext) }
    Column {
        MyPageTopBar(onSettingClick = onSettingClick)
        UserNameAndLoginType(userName = userName, socialLoginUIModel = socialLoginUIModel)
        HorizontalDivider(thickness = 1.dp, color = NeutralWeak)
        MyPageButton(
            iconId = R.drawable.ic_home_recent_checked_20_20,
            text = "최근 본 매물",
            onClick = onRecentViewClicked
        )
        MyPageButton(
            iconId = R.drawable.ic_chat_20_20,
            text = "1:1 문의",
            onClick = onInquiryClicked
        )
    }
}

//TODO : id 기반 분리
@Composable
private fun UserNameAndLoginType(userName: String, socialLoginUIModel: SocialLoginUIModel) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = userName,
            style = SlugTypographyStyle.TitleLargeBold,
            color = NeutralContrast
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(shape = CircleShape, color = socialLoginUIModel.backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                ImageProcessor(
                    modifier = Modifier.size(9.dp),
                    imageResource = ImageResource.Id(id = socialLoginUIModel.iconId)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${stringResource(id = socialLoginUIModel.loginSNSText)} 로그인",
                style = SlugTypographyStyle.BodyMiniMedium,
                color = NeutralContrast
            )
        }
    }
}

@Composable
private fun MyPageButton(@DrawableRes iconId: Int, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .blockingClickable(onClick = onClick)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconId),
            tint = Neutral,
            contentDescription = "FavoriteIcon",
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text, style = SlugTypographyStyle.BodyLargeMedium, color = Neutral)
    }
}

@Composable
@Preview
fun PreviewMyPageScreen() {
    val onSettingClick: () -> Unit = {}
    val userName = "해리"
    val socialLoginUIModel = SocialLoginUIModel.KAKAO
    val onRecentViewClicked = {}
    val onInquiryClicked = {}

    SlugTheme {
        Surface {
            MyPageScreen(
//                onSettingClick,
                userName,
                socialLoginUIModel,
                onRecentViewClicked,
                onInquiryClicked
            )
        }
    }
}