package com.warehouseinhand.slug.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.warehouseinhand.slug.login.SocialLoginUIModel


@Composable
internal fun MyPageRoute(
    padding: PaddingValues,
    viewModel: MyPageViewModel = hiltViewModel(),
) {

    val onSettingClick: () -> Unit = {}
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val lastLoginType by viewModel.lastLoginType.collectAsStateWithLifecycle()
    val onRecentViewClicked = {}
    val onInquiryClicked = {}

    Box(modifier = Modifier.padding(padding)) {
        MyPageScreen(
//            onSettingClick = onSettingClick, //TODO :NAV3 에서 변경되어야 하는부분
            userName = userName,
            socialLoginUIModel = SocialLoginUIModel.byType(type = lastLoginType),
            onRecentViewClicked = onRecentViewClicked,
            onInquiryClicked = onInquiryClicked,
        )
    }

}
