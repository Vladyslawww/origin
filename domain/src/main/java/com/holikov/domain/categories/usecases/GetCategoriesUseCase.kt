package com.holikov.domain.categories.usecases

import com.holikov.domain.categories.CategoriesRepository
import com.holikov.domain.categories.model.Category

interface GetCategoriesUseCase {
    suspend fun categories(): List<Category>
}

internal class GetCategoriesUseCaseImpl(private val repository: CategoriesRepository): GetCategoriesUseCase {
    override suspend fun categories() = repository.categories()
}