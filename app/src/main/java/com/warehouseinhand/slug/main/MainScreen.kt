package com.warehouseinhand.slug.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Composable
fun MainScreen(modifier: Modifier) {
    var selectedItem: BottomBarItemUiModel by remember { mutableStateOf(BottomBarItemUiModel.HOME) }
    val onClick: (BottomBarItemUiModel) -> Unit = { selectedItem = it }

    Scaffold(
        content = { padding ->
            val navController = rememberNavController()
            val startRoute = "MAIN TEST"
            //MainNavHost
            NavHost(
                navController = navController,
                startDestination = startRoute
            ) {

            }
        },
        bottomBar = {
            MainBottomBar(
                selectedItem = selectedItem,
                onClick = onClick
            )
        }
    )
}

