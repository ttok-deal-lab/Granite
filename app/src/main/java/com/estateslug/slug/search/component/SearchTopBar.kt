package com.estateslug.slug.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.estateslug.slug.R
import com.estateslug.slug.ui.theme.Gray300
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.Primary
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
fun SearchTopBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCloseClick: ()-> Unit,
    onKeywordClearClick: () -> Unit,
    onSearch: (String) -> Unit,
    autoFocus: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(NeutralInverted)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .blockingClickable(onClick = onBackClick)
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_top_back_24_24),
                tint = Neutral,
                contentDescription = "뒤로가기"
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp)
                .background(NeutralWeak, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                textStyle = SlugTypographyStyle.BodyMediumMedium.copy(color = Neutral),
                cursorBrush = SolidColor(Primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch(searchText) }
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "경매 번호, 주소 검색",
                                style = SlugTypographyStyle.BodyMediumMedium,
                                color = NeutralSubtler
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (searchText.isNotEmpty()) {
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .blockingClickable(onClick = onKeywordClearClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(R.drawable.ic_delete_22_22),
                        tint = Gray300,
                        contentDescription = "지우기"
                    )
                }
            }
        }

        Spacer(Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .blockingClickable(onClick = onCloseClick)
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "닫기",
                style = SlugTypographyStyle.BodyMediumMedium,
                color = Neutral
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchTopBarEmpty() {
    SearchTopBar(
        searchText = "",
        onSearchTextChange = {},
        onBackClick = {},
        onKeywordClearClick = {},
        onSearch = {},
        onCloseClick = {}
    )
}

@Preview
@Composable
fun PreviewSearchTopBarWithText() {
    SearchTopBar(
        searchText = "서울시",
        onSearchTextChange = {},
        onBackClick = {},
        onKeywordClearClick = {},
        onSearch = {},
        onCloseClick = {}
    )
}
