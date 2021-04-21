package com.holikov.domain.listing.model

data class GoodsItem(
    val listingId: Long,
    val description: String,
    val title: String,
    val price: String,
    val currencyCode: String,
    val url: String,
    val image: String?,
    val isSaved: Boolean? = null
)