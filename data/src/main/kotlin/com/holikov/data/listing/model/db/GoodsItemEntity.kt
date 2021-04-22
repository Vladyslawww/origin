package com.holikov.data.listing.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.holikov.domain.listing.model.GoodsItem

@Entity(tableName = "goods")
data class GoodsItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "listing_id") val listingId: Long,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "currency_code") val currencyCode: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "image") val image: String?
)

fun GoodsItemEntity.toDomain() = GoodsItem(
    listingId = listingId,
    description = description,
    title = title,
    price = price,
    currencyCode = currencyCode,
    url = url,
    image = image
)

fun GoodsItem.toEntity() = GoodsItemEntity(
    listingId = listingId,
    description = description,
    title = title,
    price = price,
    currencyCode = currencyCode,
    url = url,
    image = image
)