package com.warehouseinhand.slug.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.component.button.ToggleSwitchCircle
import com.warehouseinhand.slug.ui.theme.NeutralContrast
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable

@Composable
internal fun SettingToggleButton(
    valueName: String,
    valueDescription: String,
    isToggleOn: Boolean,
    onToggleButtonClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(valueName, style = SlugTypographyStyle.BodyLargeMedium, color = NeutralContrast)
            Spacer(Modifier.height(4.dp))
            Text(
                valueDescription,
                style = SlugTypographyStyle.BodyMiniMedium,
                color = NeutralSubtler
            )
        }
        Box(modifier = Modifier.blockingClickable(onClick = onToggleButtonClicked)) {
            ToggleSwitchCircle(state = isToggleOn)
        }
    }
}

@Composable
@Preview
private fun PreviewSettingToggleButton() {
    var isToggleOn: Boolean by remember { mutableStateOf(false) }
    val onToggleButtonClicked: () -> Unit = {
        isToggleOn = !isToggleOn
    }
    val valueName: String = "알림"
    val valueDescription: String = "중요한 정보를 알림으로 전달드려요."
    Box(
        modifier = Modifier.systemBarsPadding()
    ){
        SettingToggleButton(
            isToggleOn = isToggleOn,
            onToggleButtonClicked = onToggleButtonClicked,
            valueName = valueName,
            valueDescription = valueDescription,
        )
    }

}