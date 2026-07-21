package com.estateslug.slug.setting.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.estateslug.slug.permission.PermissionChecker
import com.estateslug.slug.permission.PermissionDataModel

@Composable
internal fun PermissionSettingRoute(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissions = remember { PermissionDataModel.permissions }

    val grantedStates = remember {
        mutableStateMapOf<PermissionDataModel, Boolean>().apply {
            permissions.forEach { put(it, PermissionChecker.isGranted(context, it.permissionString)) }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissions.forEach { grantedStates[it] = PermissionChecker.isGranted(context, it.permissionString) }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    var pendingPermission by remember { mutableStateOf<PermissionDataModel?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val target = pendingPermission
        if (target != null) {
            grantedStates[target] = isGranted
            if (!isGranted && !activity.shouldShowRequestPermissionRationale(target.permissionString)) {
                // 영구 거부 -> 앱 상세 설정으로 이동
                moveToAppDetailSettings(context)
            }
        }
        pendingPermission = null
    }

    val onPermissionClick: (PermissionDataModel) -> Unit = { permission ->
        val isGranted = grantedStates[permission] ?: false
        if (isGranted) {
            // 런타임 권한은 앱에서 회수 불가 -> 시스템 설정으로 이동
            if (permission.permissionString == Manifest.permission.POST_NOTIFICATIONS) {
                moveToNotificationSettings(context)
            } else {
                moveToAppDetailSettings(context)
            }
        } else {
            pendingPermission = permission
            permissionLauncher.launch(permission.permissionString)
        }
    }

    PermissionSettingPage(
        permissions = permissions,
        grantedStates = grantedStates,
        onPermissionClick = onPermissionClick,
        onBackClick = onBackClick,
    )
}

private fun moveToAppDetailSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = "package:${context.packageName}".toUri()
    }
    context.startActivity(intent)
}

private fun moveToNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}
