package com.estateslug.slug.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

fun Context.openInquiryEmail() {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf("slugdeveloper@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "[민달팽이] 문의 메일")
        putExtra(Intent.EXTRA_TEXT, "문의하실 내용을 입력해주세요.")
    }
    try {
        startActivity(Intent.createChooser(emailIntent, "문의를 보낼 메일을 선택해주세요."));
    }catch (e: ActivityNotFoundException){
        Toast.makeText(this, "메일 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
    }
}