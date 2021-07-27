package com.holikov.data.listing

import com.holikov.base.mappers.mapItem
import com.holikov.base.mappers.mapList
import com.holikov.data.listing.model.db.GoodsItemEntity
import com.holikov.data.listing.model.db.toDomain
import com.holikov.data.listing.model.db.toEntity
import com.holikov.data.listing.model.net.ApiGoodsItem
import com.holikov.data.listing.model.net.ApiImageUrls
import com.holikov.data.listing.model.net.ApiImages
import com.holikov.data.listing.model.net.toDomain
import com.holikov.domain.listing.GoodsRepository
import com.holikov.domain.listing.model.GoodsItem
import com.holikov.domain.listing.model.SearchPage
import com.holikov.data.listing.GoodsDao as LocalGoodsDataStore
import com.holikov.data.listing.ListingsApi as RemoteGoodsDataStore
import kotlinx.coroutines.flow.map


internal class GoodsRepositoryImpl(
    private val localGoodsDataStore: LocalGoodsDataStore,
    private val remoteGoodsDataStore: RemoteGoodsDataStore
) : GoodsRepository {

    override suspend fun remoteSearch(
        page: Int,
        categoryName: String?,
        keywords: String?
    ): SearchPage {
        val apiPage = remoteGoodsDataStore.activeItems(
            page = page,
            categoryName = categoryName,
            keywords = keywords.apiQuery()
        )
        return SearchPage(
            nextPage = apiPage?.pagination?.nextPage,
            results = apiPage?.results.process(ApiImageUrls.Quality.MEDIUM)
        )
    }

    override suspend fun remoteItem(listingId: Long): GoodsItem? {
        val item =
            remoteGoodsDataStore.getItem(listingId)?.results.process(ApiImageUrls.Quality.HIGH)
                .firstOrNull()
        return item?.copy(isSaved = isSaved(listingId))
    }

    override suspend fun localItem(listingId: Long) =
        localGoodsDataStore.getItem(listingId).firstOrNull()?.mapItem(GoodsItemEntity::toDomain)

    override suspend fun isSaved(listingId: Long) =
        localGoodsDataStore.getItem(listingId).isEmpty().not()

    override suspend fun save(good: GoodsItem) =
        localGoodsDataStore.insert(good.mapItem(GoodsItem::toEntity))

    override suspend fun update(good: GoodsItem) =
        localGoodsDataStore.update(good.mapItem(GoodsItem::toEntity))

    override fun savedFlow() =
        localGoodsDataStore.getAll().map { it.mapList(GoodsItemEntity::toDomain) }

    override suspend fun delete(listingId: Long) = localGoodsDataStore.deleteById(listingId)
    override suspend fun delete() = localGoodsDataStore.deleteAll()

    private suspend fun List<ApiGoodsItem>?.process(quality: ApiImageUrls.Quality) =
        mapList { it.toDomain(quality) }

    private fun String?.apiQuery() = if (this?.isEmpty() == true) null else this
}