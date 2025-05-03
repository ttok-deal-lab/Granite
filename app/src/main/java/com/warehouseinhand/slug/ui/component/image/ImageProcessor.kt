package com.warehouseinhand.slug.ui.component.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageProcessor(
    modifier: Modifier = Modifier,
    imageResource: ImageResource,
    contentDescription: String? = null
) {
    when (imageResource) {
        is ImageResource.Url -> {
            //캐시 처리 할것!
            GlideImage(
                modifier = modifier,
                model = imageResource.url,
                contentDescription = contentDescription,
            )
        }

        is ImageResource.Id -> {
            Image(
                modifier = modifier,
                painter = painterResource(id = imageResource.id),
                contentDescription = contentDescription
            )
        }
    }
}