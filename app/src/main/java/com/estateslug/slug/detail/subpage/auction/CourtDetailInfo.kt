package com.estateslug.slug.detail.subpage.auction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.detail.subpage.ItemTitle
import com.estateslug.slug.ui.theme.Critical
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.SlugTypographyStyle


@Composable
fun CourtDetailInfo(courtDetailInfo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = NeutralInverted)
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ItemTitle("법원 상세내역")
        Text(
            text = courtDetailInfo,
            style = SlugTypographyStyle.BodySmallMedium,
            color = NeutralSubtler
        )
    }
}

@Composable
@Preview
fun PreviewCourtDetailInfo() {
    CourtDetailInfo(
        courtDetailInfo = "1동의 건물의 표시\n" +
                "서울특별시 서초구 서초동 1557-8\n" +
                "주건축물\n" +
                "[도로명주소]\n" +
                "서울특별시 서초구 반포대로 26길 35\n" +
                "철근콘크리트구조 철근콘크리트지붕\n" +
                "6층 공동주택\n" +
                "지하 2층 199.52㎡\n" +
                "지하 1층 209.17㎡\n" +
                "1층 44.34㎡\n" +
                "1층 38.03㎡\n" +
                "1층 16.66㎡\n" +
                "1층 9.06㎡\n" +
                "2층 202.04㎡\n" +
                "3층 164.06㎡\n" +
                "4층 162.48㎡\n" +
                "5층 139.15㎡\n" +
                "6층 129.96㎡\n" +
                "\n" +
                "전유부분의 건물의 표시\n"
    )
}