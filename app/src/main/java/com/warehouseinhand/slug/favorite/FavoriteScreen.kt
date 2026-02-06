package com.warehouseinhand.slug.favorite

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.home.ProductList
import com.warehouseinhand.slug.home.ProductItemUiModel
import com.warehouseinhand.slug.ui.component.image.ImageProcessor
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.NeutralSubtler
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle


//TODO : 지역단위 선택 화면 만들기.

@Composable
fun FavoriteScreen(
    padding: PaddingValues,
    productUiModelList: LazyPagingItems<ProductItemUiModel>,
    onItemClicked: (ProductItemUiModel) -> Unit,
    onNotificationClick: () -> Unit,
    ) {
    Column(modifier = Modifier.padding(padding)) {
        FavoriteTopBar(
            onNotificationClick = onNotificationClick,
        )
        if (productUiModelList.loadState.refresh is LoadState.NotLoading && productUiModelList.itemCount == 0) {
            EmptyFavoriteScreen()
        } else {
            ProductList(productUiModelList, onItemClicked)
        }
    }
}

@Composable
fun EmptyFavoriteScreen() {
    Box(Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ImageProcessor(
                modifier = Modifier.requiredWidthIn(max = 50.dp),
                imageResource = ImageResource.Id(R.drawable.list_empty_50_50)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(R.string.home_favorite_empty_title),
                style = SlugTypographyStyle.BodySmallMedium,
                color = NeutralSubtler
            )
        }
    }
}


@Preview(device = PIXEL_7_PRO)
@Composable
fun PreviewHomeScreen() {
    val onNotificationClick: () -> Unit = {}
    val currentContext = LocalContext.current
    val onItemClicked: (ProductItemUiModel) -> Unit = { model ->
        Toast.makeText(currentContext, model.nameOfProduct, Toast.LENGTH_SHORT).show()
    }
    val productUiModelList = ProductItemUiModel.pagingItems()

    FavoriteScreen(
        padding = PaddingValues(),
        onNotificationClick = onNotificationClick,
        onItemClicked = onItemClicked,
        productUiModelList = productUiModelList,
    )
}
@Preview
@Composable
fun PreviewEmptyFavoriteScreen() {
    EmptyFavoriteScreen()
}