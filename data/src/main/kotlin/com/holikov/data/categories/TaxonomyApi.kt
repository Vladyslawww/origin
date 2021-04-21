package com.holikov.data.categories

import com.holikov.data.ApiContract
import com.holikov.data.categories.model.api.ApiCategories
import retrofit2.http.GET

interface TaxonomyApi {

    @GET(ApiContract.Taxonomy.CATEGORIES)
    suspend fun getCategories(): ApiCategories?
}