package com.warehouseinhand.slug.setting.withdraw

import androidx.compose.runtime.Composable

@Composable
internal fun WithdrawRoute(onBackClick: () -> Unit) {
    val onWithDrawClick: () -> Unit = { }
    WithdrawPage(
        onBackClick = onBackClick,
        onWithDrawClick = onWithDrawClick,
    )
}
