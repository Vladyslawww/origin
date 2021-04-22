package com.holikov.domain.listing.model

data class SearchPage(
    val results: List<GoodsItem>,
    val nextPage: Int?
)