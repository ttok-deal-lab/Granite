package com.estateslug.slug.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.component.skeleton.shimmerEffect
import com.estateslug.slug.ui.theme.Gray150
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralLight
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.SlugTheme

@Composable
fun DetailScreenSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(NeutralInverted)
            .verticalScroll(rememberScrollState())
    ) {
        // 이미지 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.7f)
                .shimmerEffect()
        )

        // 이름 + 좋아요 영역
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NameAndLikeSkeleton()
            LabelListSkeleton()
            PriceCardSkeleton()
        }

        // 구분선
        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)

        // 탭 영역
        TabSkeleton()

        HorizontalDivider(color = NeutralWeak)

        // 컨텐츠 영역 - 한 눈에 보기 카드
        CheckCardsSkeleton()

        HorizontalDivider(color = NeutralWeak, thickness = 10.dp)

        // 경매 히스토리 영역
        AuctionHistorySkeleton()
    }
}

@Composable
private fun NameAndLikeSkeleton() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 200.dp, height = 22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .size(width = 140.dp, height = 14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(width = 20.dp, height = 10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun LabelListSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun PriceCardSkeleton() {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(NeutralInverted)
            .border(shape = shape, width = 1.dp, color = Gray150)
    ) {
        // 윗부분 - 최저매각가격
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }

        // 아랫부분 - 감정가, 실거래가, 매각기일
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeutralLight)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(width = 70.dp, height = 14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(width = 90.dp, height = 14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
private fun TabSkeleton() {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 56.dp, height = 18.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .size(width = 56.dp, height = 18.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
    }
}

@Composable
private fun CheckCardsSkeleton() {
    Column(
        modifier = Modifier
            .background(NeutralInverted)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // "한 눈에 보기" 타이틀
        Box(
            modifier = Modifier
                .size(width = 90.dp, height = 18.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(3) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeutralLight)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 56.dp, height = 14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
private fun AuctionHistorySkeleton() {
    Column(
        modifier = Modifier
            .background(NeutralInverted)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 타이틀
        Box(
            modifier = Modifier
                .size(width = 90.dp, height = 18.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        // 히스토리 행들
        repeat(4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun PreviewDetailScreenSkeleton() {
    SlugTheme {
        DetailScreenSkeleton()
    }
}
