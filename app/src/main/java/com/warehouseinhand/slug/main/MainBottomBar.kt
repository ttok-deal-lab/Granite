package com.warehouseinhand.slug.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.theme.NeutralMuted
import com.warehouseinhand.slug.ui.theme.Primary
import com.warehouseinhand.slug.ui.theme.SlugTypographyStyle

@Composable
fun MainBottomBar(
    selectedItem: BottomBarItemUiModel,
    onClick: (BottomBarItemUiModel) -> Unit
) {
    Row(modifier = Modifier.height(64.dp)) {
        BottomBarItemUiModel.entries
            .forEach {
                MainBottomBarItem(
                    isSelected = selectedItem == it,
                    onClick = { onClick(it) },
                    bottomBarItemUiModel = it
                )
            }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    isSelected: Boolean,
    bottomBarItemUiModel: BottomBarItemUiModel,
    onClick: () -> Unit
) {
    //fixed
    val size = 28.dp
    val selectedColor: Color = Primary
    val unSelectedColor: Color = NeutralMuted
    val color = remember { Animatable(unSelectedColor) }
    LaunchedEffect(isSelected) {
        color.animateTo(
            if (isSelected) selectedColor else unSelectedColor,
            animationSpec = spring()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .selectable(
                selected = isSelected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BottomIcon(
            size = size, color = color.value,
            bottomBarItemUiModel = bottomBarItemUiModel
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(bottomBarItemUiModel.title),
            style = SlugTypographyStyle.BodyMicroMedium,
            color = color.value
        )
    }
}

// 분리 되어야 하거나 공통 컴포넌트화 시켜야 할때는 Sealed Class를 이용한 형태로 변경해야함
enum class BottomBarItemUiModel(
    @StringRes val title: Int,
    @DrawableRes val tintAbleResource: Int,
    @DrawableRes val backgroundResource: Int = tintAbleResource,
    @StringRes val iconDescription: Int,
) {
    HOME(
        title = R.string.home_bottom_nav_home_title,
        tintAbleResource = R.drawable.ic_home,
        iconDescription = R.string.home_bottom_nav_home_description
    ),
    FAVORITES(
        title = R.string.home_bottom_nav_favorites_title,
        tintAbleResource = R.drawable.ic_heart,
        iconDescription = R.string.home_bottom_nav_favorites_description
    ),
    CREW(
        title = R.string.home_bottom_nav_crew_title,
        tintAbleResource = R.drawable.ic_note_crew_front,
        backgroundResource = R.drawable.ic_note_crew_background,
        iconDescription = R.string.home_bottom_nav_crew_description
    ),
    MYPAGE(
        title = R.string.home_bottom_nav_mypage_title,
        tintAbleResource = R.drawable.ic_person,
        iconDescription = R.string.home_bottom_nav_mypage_description
    ),
    ;
}

@Composable
private fun BottomIcon(size: Dp, color: Color, bottomBarItemUiModel: BottomBarItemUiModel) {
    with(bottomBarItemUiModel) {
        Box(modifier = Modifier.size(size)) {
            if (backgroundResource != tintAbleResource)
                Icon(
                    painter = painterResource(backgroundResource),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            Icon(
                painter = painterResource(tintAbleResource),
                tint = color,
                contentDescription = stringResource(iconDescription)
            )
        }
    }

}

@Composable
@Preview(device = Devices.PIXEL_7_PRO)
fun PreviewMainBottomBarItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
    ) {
        MainBottomBarItem(
            isSelected = true,
            bottomBarItemUiModel = BottomBarItemUiModel.HOME,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun PreviewMainBottomBar() {
    var selectedItem: BottomBarItemUiModel by remember { mutableStateOf(BottomBarItemUiModel.HOME) }
    val onClick: (BottomBarItemUiModel) -> Unit = {
        selectedItem = it
    }
    MainBottomBar(selectedItem = selectedItem, onClick = onClick)
}
