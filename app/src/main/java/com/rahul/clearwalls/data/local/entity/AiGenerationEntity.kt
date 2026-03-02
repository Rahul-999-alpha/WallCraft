package com.rahul.clearwalls.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_generations")
data class AiGenerationEntity(
    @PrimaryKey val id: String,
    val prompt: String,
    val enhancedPrompt: String,
    val imageUrl: String,
    val localPath: String?,
    val style: String,
    val isAmoled: Boolean,
    val createdAt: Long
)
