package com.estateslug.slug.ui.component.image

import androidx.annotation.DrawableRes

sealed class ImageResource {
    data class Url(val url: String) : ImageResource()
    data class Id(@DrawableRes val id: Int) : ImageResource()
}