package com.holikov.domain.listing.usecases

import com.holikov.domain.listing.GoodsRepository

interface IsSavedUseCase {
    suspend fun isSaved(listingId: Long): Boolean
}

internal class IsSavedUseCaseImpl(private val repository: GoodsRepository): IsSavedUseCase {
    override suspend fun isSaved(listingId: Long) = repository.isSaved(listingId)
    }
