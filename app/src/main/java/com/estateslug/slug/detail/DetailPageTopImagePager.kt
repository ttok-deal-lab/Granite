package com.estateslug.slug.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.Gray150
import com.estateslug.slug.ui.theme.PrimaryBlack
import com.estateslug.slug.ui.theme.PrimaryWhite
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle

@Composable
fun DetailPageTopImagePager(imageList: List<ImageResource>) {
    var showFullScreen by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(
        pageCount = { imageList.size })
    Column {
        Box {
            HorizontalPager(pagerState) { index ->
                Box(
                    Modifier
                        .aspectRatio(1.7f)
                        .background(color = Gray150)
                        .clickable {
                            selectedIndex = index
                            showFullScreen = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    ImageProcessor(
                        modifier = Modifier.fillMaxSize(),
                        imageResource = imageList[index],
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .absoluteOffset(x = (-12).dp, y = (-14).dp)
                    .background(
                        shape = RoundedCornerShape(14.dp),
                        color = PrimaryBlack.copy(alpha = 0.4f)
                    )
                    .padding(
                        vertical = 5.dp,
                        horizontal = 10.dp
                    )
            ) {// TODO : 색상 추가 확인 필요
                Text(
                    text = (pagerState.currentPage + 1).toString(),
                    style = SlugTypographyStyle.BodyTinyBold,
                    color = PrimaryWhite
                )
                Text(
                    text = " / ${pagerState.pageCount}",
                    style = SlugTypographyStyle.BodyTinyBold,
                    color = PrimaryWhite.copy(alpha = 0.6f)
                )
            }
        }
    }

    if (showFullScreen) {
        FullScreenImageDialog(
            imageList = imageList,
            initialPage = selectedIndex,
            onDismiss = { showFullScreen = false },
        )
    }
}

@Composable
@Preview
fun PreviewDetailPageTop() {
    val imageList: List<ImageResource> = listOf(
        ImageResource.Id(R.drawable.logo_metaopo),
        ImageResource.Id(R.drawable.logo_metaopo),
        ImageResource.Id(R.drawable.logo_metaopo),
    )
    SlugTheme {
        DetailPageTopImagePager(imageList)
    }
}
