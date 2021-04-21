package com.holikov.data.categories.model.api

import com.holikov.domain.categories.model.Category
import com.squareup.moshi.Json


data class ApiCategory(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "short_name") val shortName: String
)

fun ApiCategory.toDomain() =
    Category(
        name = name,
        shortName = shortName
    )
