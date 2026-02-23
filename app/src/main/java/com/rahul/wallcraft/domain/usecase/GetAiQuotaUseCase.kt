package com.rahul.wallcraft.domain.usecase

import com.rahul.wallcraft.domain.model.AiQuota
import com.rahul.wallcraft.domain.repository.AiGenerationRepository
import javax.inject.Inject

class GetAiQuotaUseCase @Inject constructor(
    private val repository: AiGenerationRepository
) {
    suspend operator fun invoke(): AiQuota = repository.getQuota()
}
