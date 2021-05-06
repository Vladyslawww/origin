package com.holikov.myclient.search.model

import android.os.Parcelable
import com.holikov.domain.listing.model.GoodsItem
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppGoodsItem(val listingId: Long,
                        val description: String,
                        val title: String,
                        val priceNumber: String,
                        val currencyCode: String,
                        val url: String,
                        val image: String?) : Parcelable {
    @IgnoredOnParcel
    val price = "$priceNumber $currencyCode"
    companion object {
        fun empty() = AppGoodsItem(0, "", "", "", "", "", "")
    }
}

fun GoodsItem.toApp() = AppGoodsItem(
    listingId = listingId,
    description = description,
    title = title,
    priceNumber = price,
    currencyCode = currencyCode,
    url = url,
    image = image
)

fun AppGoodsItem.toDomain() = GoodsItem(
    listingId = listingId,
    description = description,
    title = title,
    price = priceNumber,
    currencyCode = currencyCode,
    url = url,
    image = image
)