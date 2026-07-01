package com.estateslug.slug.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.component.skeleton.shimmerEffect
import com.estateslug.slug.ui.theme.NeutralWeak

@Composable
fun ProductItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect(),
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
                )
            }
        }
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.Bottom)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect(),
        )
    }
}

@Composable
fun ProductListSkeleton(
    modifier: Modifier = Modifier,
    count: Int = 5,
) {
    Column(modifier = modifier) {
        repeat(count) { index ->
            ProductItemSkeleton()
            if (index < count - 1) {
                HorizontalDivider(color = NeutralWeak)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemSkeletonPreview() {
    ProductItemSkeleton()
}

@Preview(showBackground = true)
@Composable
private fun ProductListSkeletonPreview() {
    ProductListSkeleton(count = 3)
}
