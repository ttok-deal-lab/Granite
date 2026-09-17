package com.estateslug.slug.ui.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.image.LandscapistImage
import com.estateslug.slug.ui.component.skeleton.shimmerEffect

@Composable
fun ImageProcessor(
    modifier: Modifier = Modifier,
    imageResource: ImageResource,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    /** 단색 벡터 아이콘의 색을 테마 토큰으로 바꿀 때 지정한다. 다색 자산(사진·일러스트·2톤 아이콘)에는 쓰지 않는다. */
    tint: Color? = null,
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
                contentDescription = contentDescription,
                colorFilter = tint?.let { ColorFilter.tint(it) }
            )
        }
    }
}