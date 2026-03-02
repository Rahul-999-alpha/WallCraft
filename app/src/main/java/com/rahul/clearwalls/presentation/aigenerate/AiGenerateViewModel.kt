package com.rahul.clearwalls.presentation.aigenerate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.clearwalls.domain.model.AiGeneration
import com.rahul.clearwalls.domain.model.AiQuota
import com.rahul.clearwalls.domain.model.AiStyle
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.model.WallpaperSource
import com.rahul.clearwalls.domain.repository.AiGenerationRepository
import com.rahul.clearwalls.domain.usecase.GenerateAiWallpaperUseCase
import com.rahul.clearwalls.domain.usecase.GetAiQuotaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiGenerateViewModel @Inject constructor(
    private val generateAiWallpaperUseCase: GenerateAiWallpaperUseCase,
    private val getAiQuotaUseCase: GetAiQuotaUseCase,
    private val aiGenerationRepository: AiGenerationRepository
) : ViewModel() {

    private val _prompt = MutableStateFlow("")
    val prompt: StateFlow<String> = _prompt.asStateFlow()

    private val _selectedStyle = MutableStateFlow(AiStyle.GENERAL)
    val selectedStyle: StateFlow<AiStyle> = _selectedStyle.asStateFlow()

    private val _isAmoled = MutableStateFlow(false)
    val isAmoled: StateFlow<Boolean> = _isAmoled.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _quota = MutableStateFlow<AiQuota?>(null)
    val quota: StateFlow<AiQuota?> = _quota.asStateFlow()

    private val _generatedWallpaper = MutableStateFlow<AiGeneration?>(null)
    val generatedWallpaper: StateFlow<AiGeneration?> = _generatedWallpaper.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

    val history = aiGenerationRepository.getGenerationHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshQuota()
    }

    fun onPromptChange(prompt: String) {
        _prompt.value = prompt
    }

    fun onStyleSelected(style: AiStyle) {
        _selectedStyle.value = style
    }

    fun toggleAmoled() {
        _isAmoled.value = !_isAmoled.value
    }

    fun generate() {
        val currentPrompt = _prompt.value.trim()
        if (currentPrompt.isBlank()) {
            viewModelScope.launch { _events.emit("Please enter a prompt") }
            return
        }

        val currentQuota = _quota.value
        if (currentQuota != null && !currentQuota.canGenerate) {
            viewModelScope.launch { _events.emit("No generations remaining. Watch an ad for more!") }
            return
        }

        viewModelScope.launch {
            _isGenerating.value = true
            generateAiWallpaperUseCase(currentPrompt, _selectedStyle.value, _isAmoled.value)
                .onSuccess { generation ->
                    _generatedWallpaper.value = generation
                    refreshQuota()
                    _events.emit("Wallpaper generated!")
                }
                .onFailure { error ->
                    _events.emit("Generation failed: ${error.message}")
                }
            _isGenerating.value = false
        }
    }

    fun onAdWatched() {
        viewModelScope.launch {
            aiGenerationRepository.grantBonusQuota()
            refreshQuota()
            _events.emit("Bonus generations unlocked!")
        }
    }

    fun clearResult() {
        _generatedWallpaper.value = null
    }

    private fun refreshQuota() {
        viewModelScope.launch {
            _quota.value = getAiQuotaUseCase()
        }
    }

    fun generationToWallpaper(generation: AiGeneration): Wallpaper = Wallpaper(
        id = "ai_${generation.id}",
        source = WallpaperSource.AI_GENERATED,
        title = generation.prompt,
        thumbnailUrl = generation.imageUrl,
        previewUrl = generation.imageUrl,
        fullUrl = generation.localPath?.let { "file://$it" } ?: generation.imageUrl,
        width = 1080,
        height = 1920,
        tags = listOf("ai", "generated", generation.style.name.lowercase()),
        isAmoled = generation.isAmoled
    )
}
