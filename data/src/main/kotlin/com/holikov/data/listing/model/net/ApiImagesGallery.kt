package com.holikov.data.listing.model.net

import com.squareup.moshi.Json

data class ApiImages(
    @field:Json(name = "results") val images: List<ApiImageUrls>?
)

data class ApiImageUrls(
    @field:Json(name = "listing_id") val listingId: Int,
    @field:Json(name = "url_570xN") val medium: String,
    @field:Json(name = "url_170x135") val low: String,
    @field:Json(name = "url_fullxfull") val original: String
) {


    fun get(quality: Quality) =
        when(quality) {
            Quality.LOW -> low
            Quality.MEDIUM -> medium
            Quality.HIGH -> original
        }

    enum class Quality {
        LOW, MEDIUM, HIGH
    }
}
