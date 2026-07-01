package com.estateslug.slug.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.estateslug.slug.detail.DetailActivity
import com.estateslug.slug.login.LogInActivity
import com.estateslug.slug.mypage.recent.RecentItemsActivity
import com.estateslug.slug.search.SearchActivity
import com.estateslug.slug.setting.SettingActivity
import androidx.core.net.toUri


fun startDetailActivity(
    context: Context,
    id: String // TODO : API 연결 이후 설정 필요.
) {
    val toDetailedActivity = Intent(context, DetailActivity::class.java)
        .apply {
            putExtra(PRODUCT_ID, id)
        }
    //TODO : putExtra로 id등 전달
    context.startActivity(toDetailedActivity)
}

const val PRODUCT_ID = "PRODUCT_ID"
fun startSettingActivity(
    context: Context,
//    id: String // TODO : API 연결 이후 설정 필요.
) {
    val toActivity = Intent(context, SettingActivity::class.java)
    //TODO : putExtra로 id등 전달
    context.startActivity(toActivity)
}

fun startRecentItemsActivity(
    context: Context,
) {
    val toActivity = Intent(context, RecentItemsActivity::class.java)
    context.startActivity(toActivity)
}

fun startSearchActivity(
    context: Context,
) {
    val toActivity = Intent(context, SearchActivity::class.java)
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


fun moveToStore(context: Context) {
    with(context) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "market://details?id=${context.packageName}".toUri()
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { startActivity(intent) }.onFailure {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=${context.packageName}".toUri()
                )
            )
        }
    }
}