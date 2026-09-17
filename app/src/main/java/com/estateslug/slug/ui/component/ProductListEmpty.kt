package com.estateslug.slug.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.component.icon.ListEmptyIcon
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
fun ProductListEmpty(title: String) {
    Box(
        Modifier
            .fillMaxSize(1f), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ListEmptyIcon(modifier = Modifier.requiredWidthIn(max = 50.dp))
            Spacer(Modifier.height(12.dp))
            Text(
                title,
                style = SlugTypographyStyle.BodySmallMedium,
                color = SlugTheme.colors.neutralSubtler
            )
        }
    }
}
