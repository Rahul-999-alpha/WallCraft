package com.rahul.clearwalls.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.usecase.SearchWallpapersUseCase
import com.rahul.clearwalls.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchWallpapersUseCase: SearchWallpapersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<Wallpaper>> = _query
        .debounce(Constants.SEARCH_DEBOUNCE_MS)
        .distinctUntilChanged()
        .filter { it.length >= 2 }
        .flatMapLatest { query ->
            searchWallpapersUseCase(query)
        }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        viewModelScope.launch {
            toggleFavoriteUseCase(wallpaper)
        }
    }
}
