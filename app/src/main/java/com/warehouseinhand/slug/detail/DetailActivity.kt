package com.warehouseinhand.slug.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.SlugTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val imageList: List<ImageResource> = listOf(
            ImageResource.Id(R.drawable.logo_metaopo),
            ImageResource.Id(R.drawable.logo_metaopo),
            ImageResource.Id(R.drawable.logo_metaopo),
        )
        val topTitle = "관천로 22길 52"

        setContent {
            SlugTheme {
                Scaffold(modifier = Modifier.systemBarsPadding(), topBar = {
                    DetailTopBar({}, {}, topTitle)
                }) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        DetailScreen(imageList)
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SlugTheme {
//        Greeting("Android")
//    }
//}