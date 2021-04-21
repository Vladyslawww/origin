package com.holikov.myclient.search.model

import android.os.Parcelable


data class AppGoodsItem(val listingId: Long,
                        val description: String,
                        val title: String,
                        val priceNumber: String,
                        val currencyCode: String,
                        val url: String,
                        val image: String?)  {
    val price: Int = 0
}