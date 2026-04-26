package com.warehouseinhand.slug.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.AuthEventBus
import com.warehouseinhand.slug.login.LogInActivity
import com.warehouseinhand.slug.ui.theme.SlugTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authEventBus: AuthEventBus

    @Inject
    lateinit var localUserDataRepository: LocalUserDataRepository

    private var isHandlingExpiry = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeSessionExpired()
        setContent {
            SlugTheme {
                MainScreen()
            }
        }
    }

    private fun observeSessionExpired() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authEventBus.events.collect {
                    handleSessionExpired()
                }
            }
        }
    }

    private suspend fun handleSessionExpired() {
        if (isHandlingExpiry) return
        isHandlingExpiry = true

        withContext(Dispatchers.IO) {
            localUserDataRepository.removeAllData()
        }

        Toast.makeText(
            this,
            "세션이 만료되었습니다. 다시 로그인해주세요.",
            Toast.LENGTH_LONG,
        ).show()

        val intent = Intent(this, LogInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }
}
