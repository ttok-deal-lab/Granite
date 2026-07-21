package com.estateslug.slug.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
fun SettingMainPage(
    appVersion: String,
    isRecentVersion: Boolean,
    onBackClick: () -> Unit,
    onTOSClick: () -> Unit,
    onMoveToStoreClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithDrawClick: () -> Unit,
    onPermissionSettingClick: () -> Unit,
) {
//    val onAlarmSettingClick: () -> Unit = {}

    Column {
        SettingTopBar(text = "설정", onBackClick = onBackClick)
//        ArrowSettingButton(buttonText = "알림 설정 관리", onClick = onAlarmSettingClick)
        ArrowSettingButton(buttonText = "권한 설정", onClick = onPermissionSettingClick)
        ArrowSettingButton(buttonText = "서비스 약관", onClick = onTOSClick)

        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)
        ArrowSettingButton(buttonText = "로그아웃", onClick = onLogoutClick)
        ArrowSettingButton(buttonText = "탈퇴하기", onClick = onWithDrawClick)
        Column(
            modifier = Modifier
                .background(NeutralWeak)
                .padding(20.dp)
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                Text(
                    "앱 버전 $appVersion",
                    style = SlugTypographyStyle.BodySmallMedium,
                    color = NeutralSubtler
                )
                if (!isRecentVersion) {
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.blockingClickable(onClick = onMoveToStoreClick)) {
                        Text(
                            "업데이트하기",
                            style = SlugTypographyStyle.BodySmallMedium,
                            color = NeutralSubtler,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
            Box(modifier = Modifier.blockingClickable(onClick = onOpenSourceClick)) {
                Text(
                    "오픈소스 라이선스 보기",
                    style = SlugTypographyStyle.BodySmallMedium,
                    color = NeutralSubtler,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }

}


@Composable
@Preview
fun PreviewSettingScreen() {
    val onBackClick: () -> Unit = {}
    val appVersion: String = "1.20.1"
    val isRecentVersion: Boolean = true
    val onTOSClick: () -> Unit = {}
    val onMoveToStoreClick: () -> Unit = {}
    val onOpenSourceClick: () -> Unit = {}
    val onLogoutClick: () -> Unit = {}
    val onWithDrawClick: () -> Unit = {}
    val onPermissionSettingClick: () -> Unit = {}
    Surface {
        SettingMainPage(
            appVersion = appVersion,
            isRecentVersion = isRecentVersion,
            onBackClick = onBackClick,
            onTOSClick = onTOSClick,
            onMoveToStoreClick = onMoveToStoreClick,
            onOpenSourceClick = onOpenSourceClick,
            onLogoutClick = onLogoutClick,
            onWithDrawClick = onWithDrawClick,
            onPermissionSettingClick = onPermissionSettingClick,
        )
    }
}