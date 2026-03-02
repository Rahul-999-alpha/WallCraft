package com.rahul.clearwalls.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rahul.clearwalls.domain.model.Category
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.usecase.GetCategoriesUseCase
import com.rahul.clearwalls.domain.usecase.GetWallpapersUseCase
import com.rahul.clearwalls.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWallpapersUseCase: GetWallpapersUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(
        savedStateHandle.get<String>("category")
    )
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val wallpapers: Flow<PagingData<Wallpaper>> = _selectedCategory
        .flatMapLatest { category ->
            getWallpapersUseCase(category = category)
        }
        .cachedIn(viewModelScope)

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = getCategoriesUseCase()
            } catch (_: Exception) {}
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        viewModelScope.launch {
            toggleFavoriteUseCase(wallpaper)
        }
    }
}
