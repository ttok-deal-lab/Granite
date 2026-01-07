package com.warehouseinhand.slug.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.warehouseinhand.slug.detail.DetailActivity
import com.warehouseinhand.slug.login.LogInActivity
import com.warehouseinhand.slug.setting.SettingActivity


fun startDetailActivity(
    context: Context,
//    id: String // TODO : API 연결 이후 설정 필요.
) {
    val toDetailedActivity = Intent(context, DetailActivity::class.java)
    //TODO : putExtra로 id등 전달
    context.startActivity(toDetailedActivity)
}
fun startSettingActivity(
    context: Context,
//    id: String // TODO : API 연결 이후 설정 필요.
) {
    val toActivity = Intent(context, SettingActivity::class.java)
    //TODO : putExtra로 id등 전달
    context.startActivity(toActivity)
}

fun moveToLoginWithBackStackClear(context: Context, intentBlock: Intent.() -> Unit = {}) {
    Log.d("ActivityMove", "MoveToLogin!")
    val intent = Intent(context, LogInActivity::class.java)
    intent.flags =
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    intent.intentBlock()
    context.startActivity(intent)
    if (context is Activity)
        context.finish()
}


fun moveToStore(context: Context) =
    with(context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("http://play.google.com/store/apps/details?id=com.warehouseinhand.slug")
        startActivity(intent)
    }
