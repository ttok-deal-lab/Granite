package com.estateslug.slug.permission

import android.Manifest
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.estateslug.slug.R

enum class PermissionDataModel(
    val isEssential: Boolean,
    val permissionString: String,
    val requiresApi: Int,
    @DrawableRes val iconId: Int,
    @StringRes val permissionNameId: Int,
    @StringRes val permissionDescriptionId: Int,
) {
    Location(
        isEssential = false,
        permissionString = Manifest.permission.ACCESS_COARSE_LOCATION,
        requiresApi = Build.VERSION_CODES.BASE,
        iconId = R.drawable.ic_color_location,
        permissionNameId = R.string.permission_notification_name,
        permissionDescriptionId = R.string.permission_location,
        ),

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    NOTIFICATION(
        isEssential = false,
        permissionString = Manifest.permission.POST_NOTIFICATIONS,
        iconId = R.drawable.ic_color_notification,
        permissionNameId = R.string.permission_notification_name,
        permissionDescriptionId = R.string.permission_notification,
        requiresApi = Build.VERSION_CODES.TIRAMISU,
    ),
    ;

    companion object {

        val permissions: List<PermissionDataModel> by lazy {
            val currentApiLevel = Build.VERSION.SDK_INT
            entries.filter { it.requiresApi <= currentApiLevel }
        }
        // 안드로이드 11(API 수준 30)부터 권한 요청을 두 번 이상 거부하면 앱은 그 권한을 다시 요청할 때 시스템 권한 대화상자를 표시하지 않음
        const val REQUEST_MAX_LIMIT = 2
        val essentialPermissions: List<PermissionDataModel> by lazy { permissions.filter { it.isEssential } }
        val optionalPermissions: List<PermissionDataModel> by lazy { permissions.filter { !it.isEssential } }
    }
}