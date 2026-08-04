package com.estateslug.slug.detail.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.estateslug.slug.main.Route
import kotlinx.serialization.Serializable

@Serializable
data class RouteDetail(val productId: String) : Route

/**
 * 상세 destination. NavController를 내리지 않고 람다만 받으므로
 * Main NavHost 외의 호스트(검색·최근 본, 추후 2-pane)에도 그대로 등록할 수 있다.
 */
fun NavGraphBuilder.detailNavGraph(
    onBack: () -> Unit,
) {
    composable<RouteDetail>(
        // Activity 전환감 패리티: 우측에서 슬라이드 인/아웃
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
    ) {
        DetailRoute(onBack = onBack)
    }
}
