package com.rahul.clearwalls.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rahul.clearwalls.data.local.entity.AiGenerationEntity
import com.rahul.clearwalls.data.local.entity.AiQuotaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiGenerationDao {
    @Query("SELECT * FROM ai_generations ORDER BY createdAt DESC")
    fun getAllGenerations(): Flow<List<AiGenerationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneration(generation: AiGenerationEntity)

    @Query("DELETE FROM ai_generations WHERE id = :id")
    suspend fun deleteGeneration(id: String)

    @Query("SELECT * FROM ai_quota WHERE date = :date")
    suspend fun getQuota(date: String): AiQuotaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuota(quota: AiQuotaEntity)
}
