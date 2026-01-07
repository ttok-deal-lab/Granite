package com.warehouseinhand.slug.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.warehouseinhand.slug.ui.component.dialog.DoubleButtonAlertDialog

@Composable
fun LogoutDialog(
    dialogVisibility: Boolean,
    onLogOutConfirmClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (dialogVisibility)
        Dialog( // TODO : 좀더 core로 올리는 방안은?
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(),
        ) {
            DoubleButtonAlertDialog( //TODO : left right가 아닌 confirm dismiss로 변경하는건?
                title = "로그아웃하시겠어요?",
                description = "",
                leftButtonText = "아니요",
                onLeftButtonClick = onDismissRequest,
                rightButtonText = "네",
                onRightButtonClick = onLogOutConfirmClicked
            )
        }

}

@Composable
@Preview
private fun PreviewLogoutDialog() {
    var dialogVisibility by remember { mutableStateOf(true) }
    val list = remember { mutableListOf<String>() }
    val onConfirmClicked: () -> Unit = {
        list += "네!"
        dialogVisibility = false
    }
    val onDismissClicked: () -> Unit = {
        list += "아니요?"
        dialogVisibility = false
    }

    Column(modifier = Modifier.systemBarsPadding()) {
        Button(onClick = { dialogVisibility = true }) {
            Text("ClickToShow")
        }
        Spacer(Modifier.height(4.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            list.forEach {
                Text(it)
            }
        }
    }
    LogoutDialog(
        dialogVisibility = dialogVisibility,
        onLogOutConfirmClicked = onConfirmClicked,
        onDismissRequest = onDismissClicked
    )
}