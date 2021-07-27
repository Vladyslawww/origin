package com.holikov.data.listing.model.net

import com.squareup.moshi.Json
import com.holikov.domain.listing.model.GoodsItem

data class ApiGoodsPage(
    @field:Json(name = "results") val results: List<ApiGoodsItem>?,
    @field:Json(name = "pagination") val pagination: ApiPagination?
)

data class ApiGoodsItem(
    @field:Json(name = "listing_id") val listingId: Long,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "price") val price: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "currency_code") val currencyCode: String?,
    @field:Json(name = "Images") val images: List<ApiImageUrls>?
)

data class ApiPagination(
    @field:Json(name = "next_page") val nextPage: Int?
)

fun ApiGoodsItem.toDomain(quality: ApiImageUrls.Quality) =
    GoodsItem(
        listingId = listingId,
        description = description ?: "",
        title = title ?: "",
        price = price ?: "",
        currencyCode = currencyCode ?: "",
        url = url ?: "",
        image = images?.firstOrNull()?.get(quality)
    )