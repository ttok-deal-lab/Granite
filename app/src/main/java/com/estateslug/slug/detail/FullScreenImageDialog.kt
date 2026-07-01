package com.estateslug.slug.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.image.LandscapistImage
import com.skydoves.landscapist.zoomable.ZoomableConfig
import com.skydoves.landscapist.zoomable.ZoomablePlugin
import com.skydoves.landscapist.zoomable.rememberZoomableState
import com.skydoves.landscapist.components.rememberImageComponent
import com.estateslug.slug.R
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.PrimaryBlack
import com.estateslug.slug.ui.theme.PrimaryWhite
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
fun FullScreenImageDialog(
    imageList: List<ImageResource>,
    initialPage: Int,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryBlack),
        ) {
            val pagerState = rememberPagerState(
                initialPage = initialPage,
                pageCount = { imageList.size },
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (val imageResource = imageList[page]) {
                    is ImageResource.Url -> {
                        val zoomableState = rememberZoomableState(
                            config = ZoomableConfig(
                                minZoom = 1f,
                                maxZoom = 4f,
                                doubleTapZoom = 2f,
                                enableDoubleTapZoom = true,
                            )
                        )
                        LandscapistImage(
                            imageModel = { imageResource.url },
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Fit,
                            ),
                            component = rememberImageComponent {
                                +ZoomablePlugin(state = zoomableState)
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    is ImageResource.Id -> {
                        Image(
                            painter = painterResource(id = imageResource.id),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }

            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(36.dp)
                    .background(
                        color = PrimaryBlack.copy(alpha = 0.5f),
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_18_18),
                    contentDescription = "닫기",
                    tint = PrimaryWhite,
                    modifier = Modifier.size(20.dp),
                )
            }

            // Page indicator
            Text(
                text = "${pagerState.currentPage + 1} / ${pagerState.pageCount}",
                style = SlugTypographyStyle.BodySmallBold,
                color = PrimaryWhite,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .background(
                        color = PrimaryBlack.copy(alpha = 0.5f),
                        shape = CircleShape,
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
@Preview
fun PreviewFullScreenImageDialog() {
    val imageList = listOf(
        ImageResource.Id(R.drawable.logo_metaopo),
        ImageResource.Id(R.drawable.logo_metaopo),
        ImageResource.Id(R.drawable.logo_metaopo),
    )
    SlugTheme {
        FullScreenImageDialog(
            imageList = imageList,
            initialPage = 0,
            onDismiss = {},
        )
    }
}
