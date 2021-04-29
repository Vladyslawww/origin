package com.holikov.domain.listing.usecases

import com.holikov.domain.listing.GoodsRepository

interface RemoveItemUseCase {
    suspend fun remove(listingId: Long)
}

internal class RemoveItemUseCaseImpl(private val repository: GoodsRepository): RemoveItemUseCase {
    override suspend fun remove(listingId: Long) = repository.delete(listingId)
}