package com.estateslug.slug.search.bridge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.R
import com.estateslug.slug.search.SearchViewModel
import com.estateslug.slug.ui.component.ProductListEmpty
import com.estateslug.slug.ui.component.image.ImageProcessor
import com.estateslug.slug.ui.component.image.ImageResource
import com.estateslug.slug.ui.theme.Neutral
import com.estateslug.slug.ui.theme.NeutralInverted
import com.estateslug.slug.ui.theme.NeutralSubtler
import com.estateslug.slug.ui.theme.NeutralWeak
import com.estateslug.slug.ui.theme.Primary
import com.estateslug.slug.ui.theme.SlugTypographyStyle
import com.estateslug.slug.util.blockingClickable

@Composable
fun SearchBridgeRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onSearch: (String) -> Unit
) {
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()
    val autoCompleteResults by viewModel.autoCompleteResults.collectAsStateWithLifecycle()
    val isSearchResultEmpty by viewModel.isSearchResultEmpty.collectAsStateWithLifecycle()
    val isSearchLoading by viewModel.isSearchLoading.collectAsStateWithLifecycle()

    SearchBridgeScreen(
        searchKeyword = searchKeyword,
        recentSearches = recentSearches,
        autoCompleteResults = autoCompleteResults,
        isSearchResultEmpty = isSearchResultEmpty,
        isSearchLoading = isSearchLoading,
        onRecentItemClick = { keyword ->
            viewModel.updateSearchKeyword(keyword)
            onSearch(keyword)
        },
        onRecentItemRemove = viewModel::removeRecentSearch,
        onClearAllRecentSearches = viewModel::clearAllRecentSearches,
        onAutoCompleteItemClick = { keyword ->
            viewModel.updateSearchKeyword(keyword)
            onSearch(keyword)
        }
    )
}

@Composable
fun SearchBridgeScreen(
    searchKeyword: String,
    recentSearches: List<String>,
    autoCompleteResults: List<String>,
    isSearchResultEmpty: Boolean = false,
    isSearchLoading: Boolean = false,
    onRecentItemClick: (String) -> Unit,
    onRecentItemRemove: (String) -> Unit,
    onClearAllRecentSearches: () -> Unit,
    onAutoCompleteItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralInverted)
    ) {
        when {
            isSearchLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            isSearchResultEmpty -> {
                ProductListEmpty("검색 결과가 없어요.")
            }
            searchKeyword.isNotEmpty() && autoCompleteResults.isNotEmpty() -> {
                AutoCompleteList(
                    keyword = searchKeyword,
                    results = autoCompleteResults,
                    onItemClick = onAutoCompleteItemClick
                )
            }
            recentSearches.isEmpty() -> {
                RecentSearchEmpty()
            }
            else -> {
                RecentSearchList(
                    recentSearches = recentSearches,
                    onItemClick = onRecentItemClick,
                    onItemRemove = onRecentItemRemove,
                    onClearAll = onClearAllRecentSearches
                )
            }
        }
    }
}

@Composable
private fun RecentSearchEmpty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(50.dp)){
                ImageProcessor(imageResource = ImageResource.Id(R.drawable.list_empty_50_50))
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "최근 검색 내역이 없어요.",
                style = SlugTypographyStyle.BodySmallMedium,
                color = NeutralSubtler
            )
        }
    }
}

@Composable
private fun SearchNoResult() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(NeutralWeak, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "!",
                    style = SlugTypographyStyle.TitleLargeBold,
                    color = NeutralSubtler
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "검색 결과가 없어요.",
                style = SlugTypographyStyle.BodySmallBold,
                color = Neutral
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "검색어를 한 글자 이상 작성해주세요.",
                style = SlugTypographyStyle.BodySmallMedium,
                color = NeutralSubtler
            )
        }
    }
}

@Composable
private fun RecentSearchList(
    recentSearches: List<String>,
    onItemClick: (String) -> Unit,
    onItemRemove: (String) -> Unit,
    onClearAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "최근 검색어",
                style = SlugTypographyStyle.BodyMediumBold,
                color = Neutral
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "전체삭제",
                style = SlugTypographyStyle.BodyMicroMedium,
                color = NeutralSubtler,
                modifier = Modifier.blockingClickable(onClick = onClearAll)
            )
        }

        LazyColumn {
            items(recentSearches) { keyword ->
                RecentSearchItem(
                    keyword = keyword,
                    onItemClick = { onItemClick(keyword) },
                    onRemoveClick = { onItemRemove(keyword) }
                )
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    keyword: String,
    onItemClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .blockingClickable(onClick = onItemClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageProcessor(
            modifier = Modifier.size(20.dp),
            imageResource = ImageResource.Id(R.drawable.ic_clock_18_18),
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = keyword,
            style = SlugTypographyStyle.BodyMediumMedium,
            color = Neutral,
            modifier = Modifier.weight(1f)
        )
        Icon(
            modifier = Modifier
                .size(20.dp)
                .blockingClickable(onClick = onRemoveClick),
            painter = painterResource(R.drawable.ic_close_18_18),
            tint = NeutralSubtler,
            contentDescription = "삭제"
        )
    }
}

@Composable
private fun AutoCompleteList(
    keyword: String,
    results: List<String>,
    onItemClick: (String) -> Unit
) {
    LazyColumn {
        items(results) { result ->
            AutoCompleteItem(
                keyword = keyword,
                result = result,
                onItemClick = { onItemClick(result) }
            )
        }
    }
}

@Composable
private fun AutoCompleteItem(
    keyword: String,
    result: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .blockingClickable(onClick = onItemClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.search_line_28_28),
            tint = NeutralSubtler,
            contentDescription = null
        )
        Spacer(Modifier.width(12.dp))
        HighlightedText(
            text = result,
            highlight = keyword
        )
    }
}

@Composable
private fun HighlightedText(
    text: String,
    highlight: String
) {
    val startIndex = text.indexOf(highlight, ignoreCase = true)
    if (startIndex >= 0) {
        Row {
            if (startIndex > 0) {
                Text(
                    text = text.substring(0, startIndex),
                    style = SlugTypographyStyle.BodyMediumMedium,
                    color = Neutral
                )
            }
            Text(
                text = text.substring(startIndex, startIndex + highlight.length),
                style = SlugTypographyStyle.BodyMediumBold,
                color = Primary
            )
            if (startIndex + highlight.length < text.length) {
                Text(
                    text = text.substring(startIndex + highlight.length),
                    style = SlugTypographyStyle.BodyMediumMedium,
                    color = Neutral
                )
            }
        }
    } else {
        Text(
            text = text,
            style = SlugTypographyStyle.BodyMediumMedium,
            color = Neutral
        )
    }
}

@Preview
@Composable
fun PreviewSearchBridgeScreenEmpty() {
    Surface {
        SearchBridgeScreen(
            searchKeyword = "",
            recentSearches = emptyList(),
            autoCompleteResults = emptyList(),
            onRecentItemClick = {},
            onRecentItemRemove = {},
            onClearAllRecentSearches = {},
            onAutoCompleteItemClick = {}
        )
    }
}

@Preview
@Composable
fun PreviewSearchBridgeScreenWithRecent() {
    Surface {
        SearchBridgeScreen(
            searchKeyword = "",
            recentSearches = listOf("안양시 동안구", "군포시", "의왕시", "수원시 권선구", "시흥시"),
            autoCompleteResults = emptyList(),
            onRecentItemClick = {},
            onRecentItemRemove = {},
            onClearAllRecentSearches = {},
            onAutoCompleteItemClick = {}
        )
    }
}

@Preview
@Composable
fun PreviewSearchBridgeScreenWithAutoComplete() {
    Surface {
        SearchBridgeScreen(
            searchKeyword = "서",
            recentSearches = emptyList(),
            autoCompleteResults = listOf("서울시", "서울시 강동구", "서울시 관악구"),
            onRecentItemClick = {},
            onRecentItemRemove = {},
            onClearAllRecentSearches = {},
            onAutoCompleteItemClick = {}
        )
    }
}
