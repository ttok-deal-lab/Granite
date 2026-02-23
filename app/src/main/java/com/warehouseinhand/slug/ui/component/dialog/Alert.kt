package com.warehouseinhand.slug.ui.component.dialog

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.component.button.basic.BasicTextButton
import com.warehouseinhand.slug.ui.component.button.basic.BasicButtonStyle
import com.warehouseinhand.slug.ui.theme.NeutralInverted
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle.BodyMediumMedium
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle.TitleLargeBold

//TODO : Button 컴포넌트 적용
@Composable
private fun BasicAlertDialog(
    title: String,
    description: String,
    buttonSpace: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = NeutralInverted, shape = RoundedCornerShape(16.dp))
            .padding(all = 24.dp)
            .width(335.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(style = TitleLargeBold, text = title)
        if (description.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(style = BodyMediumMedium, color = NeutralSubtler, text = description)
        }
        Spacer(Modifier.height(24.dp))
        buttonSpace()
    }
}

@Composable
fun OneButtonAlertDialog(
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    BasicAlertDialog(
        title = title,
        description = description,
        buttonSpace = {
            BasicTextButton(
                buttonText = buttonText,
                onButtonClick = onButtonClick
            )
        }
    )
}

@Composable
fun DoubleButtonAlertDialog(
    title: String,
    description: String,
    leftButtonText: String,
    onLeftButtonClick: () -> Unit,
    rightButtonText: String,
    onRightButtonClick: () -> Unit
) {
    BasicAlertDialog(
        title = title,
        description = description,
        buttonSpace = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(Modifier.weight(1f)) {

                    BasicTextButton(
                        buttonText = leftButtonText,
                        onButtonClick = onLeftButtonClick,
                        buttonStyle = BasicButtonStyle.Fill.SECONDARY
                    )
                }
                Box(Modifier.weight(1f)) {
                    BasicTextButton(
                        buttonText = rightButtonText,
                        onButtonClick = onRightButtonClick,
                        buttonStyle = BasicButtonStyle.Fill.PRIMARY
                    )
                }
            }
        }
    )
}

@Preview()
@Composable
fun PreviewAlertDialogOneButton() {
    val title = "제목"
    val description =
        "부가 설명 및 내용이 들어갑니다. 부가 설명 및 내용이 들어갑니다. "

    val context = LocalContext.current
    val toastIt: (String) -> Unit = {
        Log.d("TESTTEST", it)
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
    Column(
        modifier = Modifier.padding(50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicAlertDialog(
            title = title,
            description = description,
            buttonSpace = {}
        )
        OneButtonAlertDialog(
            title = title,
            description = description,
            buttonText = "Confirm",
            onButtonClick = {
                toastIt("POSITIVE!")
            }
        )
        DoubleButtonAlertDialog(
            title = title,
            description = description,
            leftButtonText = "Left!",
            onLeftButtonClick = {
                toastIt("Left!")
            },
            rightButtonText = "Right!",
            onRightButtonClick = {
                toastIt("Right!")
            }
        )
    }

}