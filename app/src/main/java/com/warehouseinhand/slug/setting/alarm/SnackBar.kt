package com.warehouseinhand.slug.setting.alarm

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.theme.Gray600
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import kotlinx.coroutines.delay

@Composable
private fun CustomSnackBarTest() {
    var list by remember { mutableStateOf(listOf<NotificationAllowUiModel>()) }
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        snackbarHost = {
            SlugSnackBarContainer(list)
        },
    ) { contentPadding ->
        // Screen content
        Box(modifier = Modifier.padding(contentPadding))
        ExtendedFloatingActionButton(
            text = { Text("Show snackbar") },
            icon = { Icon(Icons.Filled.Build, contentDescription = "") },
            onClick = {
                list += NotificationAllowUiModel.CONFIRM
            }
        )
    }
}
//1. list의 형태를 가진다.
//2. 아래에서 위로 밀려 올라가며(slide in), 사라질때는 옅어지며 사라진다(fade out)V
//3. 일정 시간이후 사라져야한다.V list에서 제거된다!
//4. 싱글이벤트를 받아 동작해야한다.
//5. 중복된 이벤트가 다시와도 처리되어야한다.

@Composable
fun SlugSnackBarContainer(list: List<NotificationAllowUiModel>) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        list.forEach {
            NotificationAllowSnackBar(model = it)
        }
    }
}

@Composable
private fun NotificationAllowSnackBar(
    model: NotificationAllowUiModel,
    onFinished: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
        delay(1500L)
        visible = false
        delay(AnimationConstants.DefaultDurationMillis.toLong())
        onFinished()
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideIn { fullSize -> IntOffset(0, fullSize.height) } +
                expandVertically(),
        exit =
            slideOut { fullSize -> IntOffset(0, fullSize.height * 2) } +
                    shrinkVertically() +
                    fadeOut(),
    ) {
        Row(
            modifier = Modifier
                .background(color = Gray600, RoundedCornerShape(size = 100.dp))
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.sizeIn(24.dp, 24.dp),
                painter = painterResource(model.id),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = model.text,
                style = SlugTypographyStyle.BodyMediumMedium,
                color = NeutralInverted
            )
        }
    }


}

// TODO : i18n
enum class NotificationAllowUiModel(@DrawableRes val id: Int, val text: String) {
    CONFIRM(R.drawable.ic_sucess_fill_24_24, "알림 수신에 동의했어요."),
    DENY(R.drawable.alert_fill_24_24, "알림 수신에 거부했어요."),
}


@Composable
@Preview
fun PreviewNotificationAllowSnackBar() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        NotificationAllowSnackBar(NotificationAllowUiModel.CONFIRM)
        NotificationAllowSnackBar(NotificationAllowUiModel.DENY)
    }
}


@Composable
@Preview
fun PreviewCustomSnackBar() {
    CustomSnackBarTest()
}


