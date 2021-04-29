package com.holikov.domain.listing.usecases

import com.holikov.domain.listing.GoodsRepository

interface RemoveAllUseCase {
    suspend fun remove()
}

internal class RemoveAllUseCaseImpl(private val repository: GoodsRepository): RemoveAllUseCase {
    override suspend fun remove() = repository.delete()
}