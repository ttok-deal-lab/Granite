package com.warehouseinhand.slug.setting.terms

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.warehouseinhand.slug.main.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.termsNavGraph(onBackClick: () -> Unit) {
    composable<RouteTerms> {
        TermsRoute(onBackClick)
    }
}

@Serializable
data object RouteTerms : Route
