package com.holikov.domain.listing.usecases

import com.holikov.domain.listing.GoodsRepository
import com.holikov.domain.listing.model.GoodsItem

interface SaveItemUseCase {
    suspend fun save(goods: GoodsItem)
}

internal class SaveItemUseCaseImpl(private val repository: GoodsRepository): SaveItemUseCase {
    override suspend fun save(goods: GoodsItem) = repository.save(goods)
}