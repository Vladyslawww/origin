package com.holikov.data.listing.model.net

import com.squareup.moshi.Json

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
    @field:Json(name = "currency_code") val currencyCode: String?

)

data class ApiPagination(
    @field:Json(name = "next_page") val nextPage: Int?
)

