package com.rahul.wallcraft.domain.repository

import com.rahul.wallcraft.domain.model.AiGeneration
import com.rahul.wallcraft.domain.model.AiQuota
import com.rahul.wallcraft.domain.model.AiStyle
import kotlinx.coroutines.flow.Flow

interface AiGenerationRepository {
    suspend fun generateWallpaper(prompt: String, style: AiStyle, isAmoled: Boolean): AiGeneration
    fun getGenerationHistory(): Flow<List<AiGeneration>>
    suspend fun getQuota(): AiQuota
    suspend fun consumeQuota(isFree: Boolean)
    suspend fun grantBonusQuota()
    suspend fun deleteGeneration(id: String)
}
