package com.holikov.domain.listing.usecases

import com.holikov.domain.listing.GoodsRepository
import com.holikov.domain.listing.model.GoodsItem

interface RemoteItemUseCase {
    suspend fun get(listingId: Long, shouldUpdate: Boolean = false): GoodsItem?
}

internal class RemoteItemUseCaseImpl(private val repository: GoodsRepository): RemoteItemUseCase {
    override suspend fun get(listingId: Long, shouldUpdate: Boolean) =
        repository.remoteItem(listingId)?.also { if (shouldUpdate) repository.update(it) }
}