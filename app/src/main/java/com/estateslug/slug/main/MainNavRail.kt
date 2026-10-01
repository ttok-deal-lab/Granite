package com.estateslug.slug.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.ui.theme.SlugTypographyStyle

/**
 * 넓은 창(600dp 이상 — 펼친 폴더블·태블릿, 가로로 든 폰 포함)에서 하단 바 대신 좌측에 놓는 네비게이션 레일.
 * 탭이 한 페인을 써도 그대로다.
 * MainBottomBar와 같은 아이템·색·선택 로직을 세로 배치로만 바꾼 것 — 스타일 변경 시 양쪽을 함께 수정할 것.
 */
@Composable
fun MainNavRail(
    selectedItem: BottomBarItemUiModel,
    onClick: (BottomBarItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spaceSize = 28.dp
    Column(
        modifier = modifier
            .fillMaxHeight()
            .statusBarsPadding()
            .width(80.dp),
        verticalArrangement = Arrangement.spacedBy(spaceSize),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier)
        BottomBarItemUiModel.entries.forEach {
            MainNavRailItem(
                modifier = Modifier.minimumInteractiveComponentSize(),
                isSelected = selectedItem == it,
                onClick = { onClick(it) },
                bottomBarItemUiModel = it,
            )
        }
    }
}

@Composable
private fun MainNavRailItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    bottomBarItemUiModel: BottomBarItemUiModel,
    onClick: () -> Unit,
) {
    val size = 28.dp
    // MainBottomBar와 같은 매핑 — 미선택은 neutralMuted가 아니라 iconUnselected(다크에서 대비 확보)
    val selectedColor: Color = SlugTheme.colors.primary
    val unSelectedColor: Color = SlugTheme.colors.iconUnselected
    val color by animateColorAsState(
        if (isSelected) selectedColor else unSelectedColor,
        label = "color"
    )

    Column(
        modifier = modifier
            .selectable(
                selected = isSelected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BottomIcon(
            size = size, color = color,
            bottomBarItemUiModel = bottomBarItemUiModel
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(bottomBarItemUiModel.title),
            style = SlugTypographyStyle.BodyMicroMedium,
            color = color
        )
    }
}

@Composable
@Preview(heightDp = 600)
private fun PreviewMainNavRail() {
    MainNavRail(
        selectedItem = BottomBarItemUiModel.HOME,
        onClick = {},
    )
}
