package com.warehouseinhand.slug.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipBoardModule {
    companion object {
        fun addTextToClipBoard(context: Context, textToClipData: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("simple text", textToClipData)
            clipboard.setPrimaryClip(clip)
        }
    }
}