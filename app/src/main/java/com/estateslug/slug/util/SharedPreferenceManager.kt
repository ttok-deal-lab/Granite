package com.estateslug.slug.util

import android.content.Context

class SharedPreferenceManager {
    companion object {
        private const val TAG = "CacheManager"
        fun deleteNaverLoginData(context: Context) =
            kotlin.runCatching { context.deleteSharedPreferences("NaverOAuthLoginEncryptedPreferenceData") }
    }
}
