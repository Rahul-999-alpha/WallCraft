package com.rahul.clearwalls.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.rahul.clearwalls.domain.model.Category
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.repository.FavoriteRepository
import com.rahul.clearwalls.domain.usecase.GetCategoriesUseCase
import com.rahul.clearwalls.domain.usecase.GetEditorPicksUseCase
import com.rahul.clearwalls.domain.usecase.GetWallpapersUseCase
import com.rahul.clearwalls.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWallpapersUseCase: GetWallpapersUseCase,
    private val getEditorPicksUseCase: GetEditorPicksUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val favoriteIds = favoriteRepository.getFavorites()
        .map { favorites -> favorites.map { it.id }.toSet() }

    private val _refreshTrigger = MutableStateFlow(System.currentTimeMillis())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val wallpapers: Flow<PagingData<Wallpaper>> =
        _refreshTrigger.flatMapLatest { getWallpapersUseCase() }
            .cachedIn(viewModelScope)
            .combine(favoriteIds) { pagingData, favIds ->
                pagingData.map { wallpaper ->
                    wallpaper.copy(isFavorite = favIds.contains(wallpaper.id))
                }
            }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val editorPicks: Flow<PagingData<Wallpaper>> =
        _refreshTrigger.flatMapLatest { getEditorPicksUseCase() }
            .cachedIn(viewModelScope)
            .combine(favoriteIds) { pagingData, favIds ->
                pagingData.map { wallpaper ->
                    wallpaper.copy(isFavorite = favIds.contains(wallpaper.id))
                }
            }

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

    fun refresh() {
        _refreshTrigger.value = System.currentTimeMillis()
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        viewModelScope.launch {
            toggleFavoriteUseCase(wallpaper)
        }
    }
}
