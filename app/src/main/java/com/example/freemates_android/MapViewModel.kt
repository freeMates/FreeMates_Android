package com.example.freemates_android

import androidx.lifecycle.ViewModel
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.model.map.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel : ViewModel() {

    sealed class SheetState {
        object Hidden : SheetState()
        object Collapsed : SheetState()
        data class PlacePreview(val place: Place) : SheetState()
        data class CategoryResult(val category: String, val places: List<RecommendItem>) : SheetState()
        data class FavoriteList(val favoritelist: List<com.example.freemates_android.model.map.FavoriteList>) : SheetState()
        data class FavoriteDetail(val list: FavoriteList) : SheetState()
    }

    private val _sheetState = MutableStateFlow<SheetState>(SheetState.Collapsed)
    val sheetState: StateFlow<SheetState> = _sheetState.asStateFlow()

    fun showPlacePreview(place: Place) {
        _sheetState.value = SheetState.PlacePreview(place)
    }

    fun showCategoryResult(category: String, places: List<RecommendItem>) {
        _sheetState.value = SheetState.CategoryResult(category, places)
    }

    fun showFavoriteList(lists: List<FavoriteList>) {
        _sheetState.value = SheetState.FavoriteList(lists)
    }

//    fun showFavoriteDetail(list: FavoriteList) {
//        _sheetState.value = SheetState.FavoriteDetail(list)
//    }
}