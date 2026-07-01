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
import com.estateslug.slug.R
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
fun ProductListEmpty(title: String) {
    Box(
        Modifier
            .fillMaxSize(1f), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ImageProcessor(
                modifier = Modifier.requiredWidthIn(max = 50.dp),
                imageResource = ImageResource.Id(R.drawable.list_empty_50_50)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                title,
                style = SlugTypographyStyle.BodySmallMedium,
                color = NeutralSubtler
            )
        }
    }
}
