package com.warehouseinhand.slug.setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.warehouseinhand.slug.ui.theme.SlugTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlugTheme {
                SettingScreen(
                    onBackClick = { this.finish() }
                )
            }
        }
    }
}
