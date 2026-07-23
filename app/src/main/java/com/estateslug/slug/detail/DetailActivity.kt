package com.estateslug.slug.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.estateslug.slug.ui.theme.SlugTheme
import com.estateslug.slug.util.PRODUCT_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    private val detailedViewModel: DetailedViewModel by viewModels()
    private var currentId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentId = intent?.getStringExtra(PRODUCT_ID) ?: ""

        if (currentId.isEmpty())
            currentId = savedInstanceState?.getString(PRODUCT_ID) ?: ""

        detailedViewModel.requestData(currentId)

        enableEdgeToEdge()

        setContent {
            val uiState by detailedViewModel.uiState.collectAsStateWithLifecycle()

            SlugTheme {
                DetailScreen(
                    uiState = uiState,
                    onBackButtonClicked = { this.finish() },
                    likeClicked = { detailedViewModel.onLikeChangeRequest() },
                    onRetry = { detailedViewModel.retry() }
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_ID, currentId)
    }

}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SlugTheme {
//        Greeting("Android")
//    }
//}