package com.estateslug.slug.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    private val _isNeedToShowBottomSheet: MutableSharedFlow<MainBottomSheetType> =
        MutableSharedFlow()
    val isNeedToShowBottomSheet get() = _isNeedToShowBottomSheet.asSharedFlow()

    fun requestToShowBottomSheet(type: MainBottomSheetType) {
        viewModelScope.launch {
            _isNeedToShowBottomSheet.emit(type)
        }
    }
}

sealed interface MainBottomSheetType {
    sealed interface HomeBottomSheetType : MainBottomSheetType {
        object LocationSelect : HomeBottomSheetType
        object ListSorting : HomeBottomSheetType
        object BuildingType : HomeBottomSheetType
        object AuctionState : HomeBottomSheetType
        object PriceRange : HomeBottomSheetType
    }

    object EMPTY : MainBottomSheetType
}