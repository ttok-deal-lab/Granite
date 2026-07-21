package com.estateslug.slug.setting.permission

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.permission.PermissionDataModel
import com.estateslug.slug.setting.SettingTopBar
import com.estateslug.slug.ui.theme.NeutralContrast
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
internal fun PermissionSettingPage(
    permissions: List<PermissionDataModel>,
    grantedStates: Map<PermissionDataModel, Boolean>,
    onPermissionClick: (PermissionDataModel) -> Unit,
    onBackClick: () -> Unit,
) {
    Column {
        SettingTopBar(text = "권한 설정", onBackClick = onBackClick)
        for (permission in permissions) {
            val granted = grantedStates[permission] ?: false
            PermissionSettingRow(
                permission = permission,
                granted = granted,
                onClick = { onPermissionClick(permission) },
            )
        }
    }
}

@Composable
private fun PermissionSettingRow(
    permission: PermissionDataModel,
    granted: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .blockingClickable(onClick = onClick)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(id = permission.permissionNameId),
                style = SlugTypographyStyle.BodyLargeMedium,
                color = NeutralContrast
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(id = permission.permissionDescriptionId),
                style = SlugTypographyStyle.BodyMiniMedium,
                color = NeutralSubtler
            )
        }
        Toggle(checked = granted)
    }
}

/** 48×28 트랙 토글. 트랙 primary(선택)/outline(비선택) · 썸 onPrimary(선택 시 white). */
@Composable
private fun Toggle(checked: Boolean) {
    val trackColor =
        if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val thumbOffset by animateDpAsState(if (checked) 20.dp else 0.dp, label = "thumb")
    Box(
        Modifier
            .size(width = 48.dp, height = 28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(trackColor)
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            Modifier
                .padding(start = thumbOffset)
                .size(22.dp)
                .clip(CircleShape)
                // 트랙 위 콘텐츠 컬러 — 선택 시 white(onPrimary), 비선택도 동일 톤으로 대비 확보.
                .background(MaterialTheme.colorScheme.onPrimary),
        )
    }
}

@Composable
@Preview
private fun PreviewPermissionSettingPage() {
    val permissions = PermissionDataModel.permissions
    var grantedStates by remember {
        mutableStateOf(permissions.associateWith { false })
    }
    SlugTheme {
        Surface {
            PermissionSettingPage(
                permissions = permissions,
                grantedStates = grantedStates,
                onPermissionClick = { permission ->
                    grantedStates = grantedStates.toMutableMap().apply {
                        this[permission] = !(this[permission] ?: false)
                    }
                },
                onBackClick = {},
            )
        }
    }
}
