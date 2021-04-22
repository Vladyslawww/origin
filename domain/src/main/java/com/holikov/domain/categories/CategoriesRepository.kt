package com.holikov.domain.categories

import com.holikov.domain.categories.model.Category

interface CategoriesRepository {
    suspend fun categories(): List<Category>
}