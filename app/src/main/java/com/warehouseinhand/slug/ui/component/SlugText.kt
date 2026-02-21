package com.warehouseinhand.slug.ui.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlin.String

sealed class SlugText() {
    @Composable
    abstract operator fun invoke(): String
    data class Text(val text: String) : SlugText() {
        @Composable
        override operator fun invoke(): String = text
    }

    data class Id(@StringRes val id: Int) : SlugText() {
        @Composable
        override operator fun invoke(): String = stringResource(id)
    }
}