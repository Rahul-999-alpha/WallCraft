package com.rahul.wallcraft.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rahul.wallcraft.domain.model.Category
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.usecase.GetCategoriesUseCase
import com.rahul.wallcraft.domain.usecase.GetEditorPicksUseCase
import com.rahul.wallcraft.domain.usecase.GetWallpapersUseCase
import com.rahul.wallcraft.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWallpapersUseCase: GetWallpapersUseCase,
    private val getEditorPicksUseCase: GetEditorPicksUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val wallpapers: Flow<PagingData<Wallpaper>> =
        getWallpapersUseCase().cachedIn(viewModelScope)

    val editorPicks: Flow<PagingData<Wallpaper>> =
        getEditorPicksUseCase().cachedIn(viewModelScope)

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = getCategoriesUseCase()
            } catch (e: Exception) {
                _categories.value = emptyList()
            }
        }
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        viewModelScope.launch {
            toggleFavoriteUseCase(wallpaper)
        }
    }
}
