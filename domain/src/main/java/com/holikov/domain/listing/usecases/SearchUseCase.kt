package com.holikov.domain.listing.usecases

import com.holikov.domain.listing.GoodsRepository
import com.holikov.domain.listing.model.SearchPage

interface SearchUseCase {
    suspend fun search(page: Int, categoryName: String? = null, keywords: String? = null): SearchPage
}

internal class SearchUseCaseImpl(private val repository: GoodsRepository): SearchUseCase {

    override suspend fun search(page: Int, categoryName: String?, keywords: String?)
            = repository.remoteSearch(page, categoryName, keywords)

}
