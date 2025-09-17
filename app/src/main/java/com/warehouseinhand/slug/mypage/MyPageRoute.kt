package com.warehouseinhand.slug.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.warehouseinhand.slug.home.HomeViewModel


@Composable
internal fun MyPageRoute(
    padding: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Box(Modifier.fillMaxSize()){
        Text("MYPAGE")
    }
}
