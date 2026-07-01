package com.estateslug.slug.permission

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts


class PermissionChecker(
    activity: ComponentActivity,
    private val onRequestResult: (essentialFailed: List<PermissionDataModel>, optionalFailed: List<PermissionDataModel>) -> Unit,
) {
    private val essential: List<PermissionDataModel> = PermissionDataModel.essentialPermissions
    private val optional: List<PermissionDataModel> = PermissionDataModel.optionalPermissions

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val essentialList: MutableList<PermissionDataModel> = essential.toMutableList()
        val optionalList = optional.toMutableList()
        permissions.entries.forEach { (permissionString, permissionAllowed) ->

            if (permissionAllowed) {
                if (essentialList.isNotEmpty()) {
                    val result = essentialList.removeByPermissionString(permissionString)
                    if (result) return@forEach
                }
                optionalList.removeByPermissionString(permissionString)
            }
        }
        onRequestResult(essentialList, optionalList)
    }

    fun requestPermission() =
        launcher.launch(PermissionDataModel.permissions.map { it.permissionString }.toTypedArray())

    private fun MutableList<PermissionDataModel>.removeByPermissionString(permissionString: String) =
        this.find { it.permissionString == permissionString }
            ?.let { this.remove(it) } ?: false

    companion object {
        fun isAllOfEssentialAllowed(activity: ComponentActivity): Boolean =
            PermissionDataModel.essentialPermissions
                .map { it.permissionString }
                .none { activity.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED }
    }
}
