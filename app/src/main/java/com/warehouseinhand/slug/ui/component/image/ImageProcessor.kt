package com.warehouseinhand.slug.ui.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.image.LandscapistImage
import com.warehouseinhand.slug.ui.component.skeleton.shimmerEffect

@Composable
fun ImageProcessor(
    modifier: Modifier = Modifier,
    imageResource: ImageResource,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null
) {
    when (imageResource) {
        is ImageResource.Url -> {
            //캐시 처리 할것!
            LandscapistImage(
                imageModel = { imageResource.url },
                imageOptions = ImageOptions(
                    contentScale = contentScale,
                    contentDescription = contentDescription,
                ),
                loading = {
                    Box(modifier = modifier
                        .fillMaxSize()
                        .shimmerEffect())
                }
            )
        }

        is ImageResource.Id -> {
            Image(
                modifier = modifier,
                contentScale = contentScale,
                painter = painterResource(id = imageResource.id),
                contentDescription = contentDescription
            )
        }
    }
}