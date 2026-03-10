package com.rahul.clearwalls.data.repository

import android.content.Context
import com.rahul.clearwalls.data.local.dao.AiGenerationDao
import com.rahul.clearwalls.data.local.entity.AiGenerationEntity
import com.rahul.clearwalls.data.local.entity.AiQuotaEntity
import com.rahul.clearwalls.data.remote.PollinationsAiService
import com.rahul.clearwalls.domain.model.AiGeneration
import com.rahul.clearwalls.domain.model.AiQuota
import com.rahul.clearwalls.domain.model.AiStyle
import com.rahul.clearwalls.domain.repository.AiGenerationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiGenerationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pollinationsAiService: PollinationsAiService,
    private val aiGenerationDao: AiGenerationDao
) : AiGenerationRepository {

    companion object {
        private const val MAX_FREE_PER_DAY = 3
        private const val MAX_BONUS_ADS_PER_DAY = 3
        private const val BONUS_PER_AD = 2
    }

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    override suspend fun generateWallpaper(
        prompt: String,
        style: AiStyle,
        isAmoled: Boolean
    ): AiGeneration = withContext(Dispatchers.IO) {
        val enhancedPrompt = buildString {
            append(prompt)
            append(", ")
            append(style.promptSuffix)
            if (isAmoled) append(", pure black background, high contrast, AMOLED optimized")
        }

        val imageBytes = pollinationsAiService.generateImage(enhancedPrompt)

        val id = UUID.randomUUID().toString()
        val file = File(context.filesDir, "ai_wallpapers/$id.png").apply {
            parentFile?.mkdirs()
            writeBytes(imageBytes)
        }

        val generation = AiGeneration(
            id = id,
            prompt = prompt,
            enhancedPrompt = enhancedPrompt,
            imageUrl = file.toURI().toString(),
            localPath = file.absolutePath,
            style = style,
            isAmoled = isAmoled
        )

        aiGenerationDao.insertGeneration(
            AiGenerationEntity(
                id = generation.id,
                prompt = generation.prompt,
                enhancedPrompt = generation.enhancedPrompt,
                imageUrl = generation.imageUrl,
                localPath = generation.localPath,
                style = generation.style.name,
                isAmoled = generation.isAmoled,
                createdAt = generation.createdAt
            )
        )

        generation
    }

    override fun getGenerationHistory(): Flow<List<AiGeneration>> =
        aiGenerationDao.getAllGenerations().map { entities ->
            entities.map { entity ->
                AiGeneration(
                    id = entity.id,
                    prompt = entity.prompt,
                    enhancedPrompt = entity.enhancedPrompt,
                    imageUrl = entity.imageUrl,
                    localPath = entity.localPath,
                    style = AiStyle.valueOf(entity.style),
                    isAmoled = entity.isAmoled,
                    createdAt = entity.createdAt
                )
            }
        }

    override suspend fun getQuota(): AiQuota {
        val today = todayString()
        val quota = aiGenerationDao.getQuota(today) ?: AiQuotaEntity(date = today)
        return AiQuota(
            freeRemaining = (MAX_FREE_PER_DAY - quota.freeUsed).coerceAtLeast(0),
            bonusRemaining = (quota.bonusEarned - quota.bonusUsed).coerceAtLeast(0),
            maxFreePerDay = MAX_FREE_PER_DAY,
            maxBonusAdsPerDay = MAX_BONUS_ADS_PER_DAY,
            bonusPerAd = BONUS_PER_AD,
            adsWatchedToday = quota.adsWatched
        )
    }

    override suspend fun consumeQuota(isFree: Boolean) {
        val today = todayString()
        val quota = aiGenerationDao.getQuota(today) ?: AiQuotaEntity(date = today)
        if (isFree) {
            aiGenerationDao.insertQuota(quota.copy(freeUsed = quota.freeUsed + 1))
        } else {
            aiGenerationDao.insertQuota(quota.copy(bonusUsed = quota.bonusUsed + 1))
        }
    }

    override suspend fun grantBonusQuota() {
        val today = todayString()
        val quota = aiGenerationDao.getQuota(today) ?: AiQuotaEntity(date = today)
        aiGenerationDao.insertQuota(
            quota.copy(
                bonusEarned = quota.bonusEarned + BONUS_PER_AD,
                adsWatched = quota.adsWatched + 1
            )
        )
    }

    override suspend fun deleteGeneration(id: String) {
        aiGenerationDao.deleteGeneration(id)
    }
}
