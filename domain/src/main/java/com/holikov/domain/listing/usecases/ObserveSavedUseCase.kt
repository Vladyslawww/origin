package com.holikov.domain.listing.usecases

import com.holikov.domain.listing.GoodsRepository
import com.holikov.domain.listing.model.GoodsItem
import kotlinx.coroutines.flow.Flow

interface ObserveSavedUseCase {
    fun savedFlow(): Flow<List<GoodsItem>>
}

internal class ObserveSavedUseCaseImpl(private val repository: GoodsRepository): ObserveSavedUseCase {
    override fun savedFlow() = repository.savedFlow()
}

