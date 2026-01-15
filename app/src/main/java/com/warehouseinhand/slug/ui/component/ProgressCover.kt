package com.warehouseinhand.slug.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.PrimaryBlack
import kotlinx.coroutines.delay

@Composable
fun ProgressCover(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.01f
        ),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .clickable { }
                .fillMaxSize()
                .background(color = PrimaryBlack.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Primary
            )
        }
    }
}

@Composable
@Preview
fun PreviewProgressCover() {
    var visible by remember { mutableStateOf(false) }
    ProgressCover(visible)
    LaunchedEffect(key1 = Unit) {
        delay(500L)
        visible = true
        delay(2000L)
        visible = false
    }
}