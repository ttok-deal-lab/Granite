package com.warehouseinhand.slug.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.component.DDayChip
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.label.SlugLabelLarge
import com.warehouseinhand.slug.ui.theme.Critical
import com.warehouseinhand.slug.ui.theme.Neutral
import com.warehouseinhand.slug.ui.theme.NeutralMuted
import com.warehouseinhand.slug.ui.theme.NeutralSubtle
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.NeutralWeak
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle
import com.warehouseinhand.slug.util.blockingClickable
import com.warehouseinhand.slug.util.numberToCurrency
import kotlinx.coroutines.flow.MutableStateFlow

//TODO : 공통컴포넌트화 고려
@Composable
fun ProductList(
    uiModelList: LazyPagingItems<ProductItemUiModel>,
    onItemClicked: (ProductItemUiModel) -> Unit
) {
    LazyColumn {
        items(
            count = uiModelList.itemCount,
        ) { index ->
            val item = uiModelList.get(index) ?: return@items
            ProductItem(
                uiModel = item,
                onItemClicked = onItemClicked
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = NeutralWeak)
            )
        }
    }
}

@Composable
fun ProductList(
    uiModelList: List<ProductItemUiModel>,
    onItemClicked: (ProductItemUiModel) -> Unit
) {
    LazyColumn {
        items(
            items = uiModelList
        ) { item ->
            ProductItem(
                uiModel = item,
                onItemClicked = onItemClicked
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = NeutralWeak)
            )
        }
    }
}

@Composable
private fun ProductItem(
    uiModel: ProductItemUiModel,
    onItemClicked: (ProductItemUiModel) -> Unit
) {
    val priceOfProductByCurrency = remember { numberToCurrency(uiModel.priceOfProduct) }
    Row(
        modifier = Modifier
            .blockingClickable(onClick = { onItemClicked(uiModel) })
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = Primary)
        ) {
            ImageProcessor(
                imageResource = uiModel.buildingImage,
                contentScale = ContentScale.Crop,

                )
            DDayChip(uiModel.daysLeft)
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = priceOfProductByCurrency,
                    style = SlugTypographyStyle.BodyLargeBold,
                    color = Neutral
                )
                Text(
                    text = uiModel.nameOfProduct,
                    style = SlugTypographyStyle.BodyMicroMedium,
                    color = Neutral
                )
                Text(
                    text = uiModel.location,
                    style = SlugTypographyStyle.BodyMicroRegular,
                    color = NeutralSubtler
                )
            }
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                uiModel.infoChipList.forEach { uiModel ->
                    SlugLabelLarge(uiModel = uiModel)
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.Bottom)) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.ic_heart),
                tint = if (uiModel.isFavorite) Critical else NeutralMuted,
                contentDescription = "FavoriteIcon",
            )
            if (uiModel.favoritePersons > 0) {
                Text(
                    text = "${uiModel.favoritePersons}",
                    style = SlugTypographyStyle.BodyMicroMedium,
                    color = NeutralSubtle
                )
            }
        }
    }
}


@Composable
@Preview
fun PreviewProductItem() {
    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = { model ->
        Toast.makeText(currentContext, model.nameOfProduct, Toast.LENGTH_SHORT).show()
    }
    Surface {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            ProductItem(
                uiModel = ProductItemUiModel.testList.first(),
                onItemClicked = onItemClicked
            )
            ProductItem(
                uiModel = ProductItemUiModel.testList[1],
                onItemClicked = onItemClicked
            )
        }
    }
}

@Composable
@Preview
fun PreviewHomeProductList() {
    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = { model ->
        Toast.makeText(currentContext, model.nameOfProduct, Toast.LENGTH_SHORT).show()
    }
    val uiModelList = MutableStateFlow(PagingData.from(ProductItemUiModel.testList))
        .collectAsLazyPagingItems()
    Surface {
        ProductList(uiModelList, onItemClicked)
    }
}