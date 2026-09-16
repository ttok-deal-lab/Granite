package com.estateslug.slug.setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.enableLightEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLightEdgeToEdge()
        setContent {
            SlugTheme {
                SettingScreen(
                    onBackClick = { this.finish() }
                )
            }
        }
    }
}
