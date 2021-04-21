package com.holikov.data.categories.model.api

import com.squareup.moshi.Json

data class ApiCategories(
    @field:Json(name = "results") val results: List<ApiCategory>? = null
)
