package com.rahul.wallcraft.domain.usecase

import com.rahul.wallcraft.domain.model.AiGeneration
import com.rahul.wallcraft.domain.model.AiStyle
import com.rahul.wallcraft.domain.repository.AiGenerationRepository
import javax.inject.Inject

class GenerateAiWallpaperUseCase @Inject constructor(
    private val repository: AiGenerationRepository
) {
    suspend operator fun invoke(
        prompt: String,
        style: AiStyle,
        isAmoled: Boolean
    ): Result<AiGeneration> = try {
        repository.consumeQuota(isFree = true)
        val generation = repository.generateWallpaper(prompt, style, isAmoled)
        Result.success(generation)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
