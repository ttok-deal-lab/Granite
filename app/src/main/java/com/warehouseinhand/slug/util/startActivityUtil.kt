package com.warehouseinhand.slug.util

import android.content.Context
import android.content.Intent
import com.warehouseinhand.slug.detail.DetailActivity


fun startDetailActivity(
    context: Context,
//    id: String // TODO : API 연결 이후 설정 필요.
) {
    val toDetailedActivity = Intent(context, DetailActivity::class.java)
    //TODO : putExtra로 id등 전달
    context.startActivity(toDetailedActivity)
}