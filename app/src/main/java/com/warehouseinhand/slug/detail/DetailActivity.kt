package com.warehouseinhand.slug.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.warehouseinhand.slug.R
import com.warehouseinhand.slug.detail.subpage.BuildingInfoUiModel
import com.warehouseinhand.slug.detail.subpage.LesseeInfo
import com.warehouseinhand.slug.detail.subpage.auction.AuctionInfoUiModel
import com.warehouseinhand.slug.ui.component.image.ImageResource
import com.warehouseinhand.slug.ui.theme.SlugTheme
import com.warehouseinhand.slug.util.PRODUCT_ID
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
                Scaffold(modifier = Modifier.systemBarsPadding(), topBar = {
                    DetailTopBar({
                        this.finish()
                    }, {}, uiState.detailSimpleInformation.topTitle)
                }) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        DetailScreen(
                            detailSimpleInformationUiModel = uiState.detailSimpleInformation,
                            listOfLessee = uiState.lessees,
                            auctionInfoUiModel = uiState.auctionInfo
                        )
                    }
                }
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