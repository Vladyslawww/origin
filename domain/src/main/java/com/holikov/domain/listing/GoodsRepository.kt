package com.holikov.domain.listing

import com.holikov.domain.listing.model.GoodsItem
import com.holikov.domain.listing.model.SearchPage
import kotlinx.coroutines.flow.Flow

interface GoodsRepository {
    fun savedFlow(): Flow<List<GoodsItem>>
    suspend fun remoteSearch(page: Int, categoryName: String?, keywords: String?): SearchPage
    suspend fun remoteItem(listingId: Long): GoodsItem?
    suspend fun localItem(listingId: Long): GoodsItem?
    suspend fun isSaved(listingId: Long): Boolean
    suspend fun save(good: GoodsItem)
    suspend fun update(good: GoodsItem)
    suspend fun delete(listingId: Long)
    suspend fun delete()
}