package com.rahul.clearwalls.domain.usecase

import com.rahul.clearwalls.domain.model.AiQuota
import com.rahul.clearwalls.domain.repository.AiGenerationRepository
import javax.inject.Inject

class GetAiQuotaUseCase @Inject constructor(
    private val repository: AiGenerationRepository
) {
    suspend operator fun invoke(): AiQuota = repository.getQuota()
}
