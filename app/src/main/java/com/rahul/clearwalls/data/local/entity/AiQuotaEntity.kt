package com.rahul.clearwalls.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_quota")
data class AiQuotaEntity(
    @PrimaryKey val date: String,
    val freeUsed: Int = 0,
    val bonusEarned: Int = 0,
    val bonusUsed: Int = 0,
    val adsWatched: Int = 0
)
