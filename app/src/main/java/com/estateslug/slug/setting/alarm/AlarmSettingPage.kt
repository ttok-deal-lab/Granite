package com.estateslug.slug.setting.alarm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.estateslug.slug.setting.SettingToggleButton
import com.estateslug.slug.setting.SettingTopBar

//TODO :i18n
@Composable
fun AlarmSettingPage(onBackClick: () -> Unit) {
    var isToggleOn: Boolean by remember { mutableStateOf(false) }

    var list by remember { mutableStateOf(listOf<NotificationAllowUiModel>()) }

    val onToggleButtonClicked: () -> Unit = {
        //TODO :
        isToggleOn = !isToggleOn

        list +=  if (isToggleOn) NotificationAllowUiModel.CONFIRM else NotificationAllowUiModel.DENY


    }
    val valueName: String = "알림"
    val valueDescription: String = "중요한 정보를 알림으로 전달드려요."

    Box(Modifier.fillMaxSize()){
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                SettingTopBar(text = "알림 설정 관리", onBackClick = onBackClick)
                SettingToggleButton(
                    valueName = valueName,
                    valueDescription = valueDescription,
                    isToggleOn = isToggleOn,
                    onToggleButtonClicked = onToggleButtonClicked
                )
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)){
            SlugSnackBarContainer(list)
        }
    }

}


@Composable
@Preview
fun PreviewAlarmSettingPage() {
    val onBackClick: () -> Unit = {}
    Box(
        modifier = Modifier.systemBarsPadding()
    ){
        AlarmSettingPage(onBackClick)
    }
}