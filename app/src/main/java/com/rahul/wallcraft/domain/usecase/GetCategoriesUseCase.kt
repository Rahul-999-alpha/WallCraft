package com.rahul.wallcraft.domain.usecase

import com.rahul.wallcraft.domain.model.Category
import com.rahul.wallcraft.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(): List<Category> = repository.getCategories()
}
