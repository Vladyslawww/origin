package com.holikov.data.categories

import com.holikov.data.categories.model.api.ApiCategory
import com.holikov.data.categories.model.api.toDomain
import com.holikov.domain.categories.CategoriesRepository
import com.holikov.domain.categories.model.Category
import com.holikov.base.mappers.mapList

class CategoriesRepositoryImpl(private val taxonomyApi: TaxonomyApi) : CategoriesRepository {

    override suspend fun categories(): List<Category> {
        return taxonomyApi.getCategories()?.results.mapList(ApiCategory::toDomain)
    }
}
