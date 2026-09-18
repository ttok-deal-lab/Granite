package com.estateslug.slug.setting.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.setting.SettingTopBar
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.ui.theme.ThemeMode

/** 화면 테마 선택. 라이트 / 다크 / 시스템 중 하나를 라디오로 고른다. */
@Composable
internal fun ThemeSettingPage(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    onBackClick: () -> Unit,
) {
    Column(Modifier.background(SlugTheme.colors.neutralInverted)) {
        SettingTopBar(text = "화면 테마", onBackClick = onBackClick)
        Column(Modifier.selectableGroup()) {
            ThemeMode.entries.forEach { mode ->
                ThemeModeRow(
                    mode = mode,
                    selected = mode == selected,
                    onClick = { onSelect(mode) },
                )
            }
        }
    }
}

@Composable
private fun ThemeModeRow(mode: ThemeMode, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // 행 전체가 라디오 하나. 접근성에는 RadioButton 역할과 선택 상태로 읽힌다.
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // 클릭은 행이 받는다
            colors = RadioButtonDefaults.colors(
                selectedColor = SlugTheme.colors.primary,
                unselectedColor = SlugTheme.colors.outline,
            ),
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = mode.displayName,
            style = SlugTypographyStyle.BodyLargeMedium,
            color = SlugTheme.colors.neutral
        )
    }
}

@Composable
@Preview
private fun PreviewThemeSettingPage() {
    var selected by remember { mutableStateOf(ThemeMode.SYSTEM) }
    SlugTheme(
//        darkTheme = true
    ) {
        Surface {
            ThemeSettingPage(selected = selected, onSelect = { selected = it }, onBackClick = {})
        }
    }
}
