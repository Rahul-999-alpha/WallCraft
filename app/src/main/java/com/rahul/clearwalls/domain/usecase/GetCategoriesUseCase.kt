package com.rahul.clearwalls.domain.usecase

import com.rahul.clearwalls.domain.model.Category
import com.rahul.clearwalls.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(): List<Category> = repository.getCategories()
}
