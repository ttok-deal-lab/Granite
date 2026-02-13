package com.warehouseinhand.slug.setting

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.warehouseinhand.slug.ui.component.dialog.DoubleButtonAlertDialog

@Composable
fun WithdrawDialog(
    dialogVisibility: Boolean,
    onWithdrawConfirmClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (dialogVisibility)
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(),
        ) {
            DoubleButtonAlertDialog(
                title = "정말 탈퇴하시겠어요?",
                description = "",
                leftButtonText = "아니요",
                onLeftButtonClick = onDismissRequest,
                rightButtonText = "네",
                onRightButtonClick = onWithdrawConfirmClicked
            )
        }
}
