package com.warehouseinhand.slug.mypage

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.warehouseinhand.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.myPageNavGraph(
    padding: PaddingValues,
) {
    composable<RouteMyPageHome> {
        MyPageRoute(
            padding = padding
        )
    }
}

@Serializable
data object RouteMyPageHome: Route

