package com.warehouseinhand.slug.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warehouseinhand.slug.home.ProductItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
) : ViewModel() {
    private val _favoriteUiModelList: MutableStateFlow<List<ProductItemUiModel>> =
        MutableStateFlow(emptyList())
    val favoriteUiModelList get() = _favoriteUiModelList.asStateFlow()

    fun requestFavoriteList(){
        networkWithProgress {
            //TODO : getList!
            delay(500)
            _favoriteUiModelList.emit(ProductItemUiModel.testList)
        }
    }

    private val _isNeedToShowProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNeedToShowProgress get() = _isNeedToShowProgress.asStateFlow()

    private fun networkWithProgress(doWithAsyncBlock: suspend () -> Unit) {
        viewModelScope.launch {
            _isNeedToShowProgress.emit(true)

            doWithAsyncBlock.invoke()

            _isNeedToShowProgress.emit(false)
        }
    }
}
