package com.holikov.data.listing

import com.holikov.data.ApiContract
import com.holikov.data.listing.model.net.ApiGoodsPage
import com.holikov.data.listing.model.net.ApiImages
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ListingApi {

    @GET(ApiContract.Listings.ACTIVE_ITEMS)
    suspend fun activeItems(
        @Query("category") categoryName: String?,
        @Query("keywords") keywords: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int = ApiContract.Listings.PAGE_SIZE
    ): ApiGoodsPage?

    @GET(ApiContract.Listings.GET_ITEMS)
    suspend fun getItem(@Path("listing_id") listingId: Long): ApiGoodsPage?

    @GET(ApiContract.Listings.IMAGES_BY_ID)
    suspend fun getImage(@Path("listing_id") listingId: Long): ApiGoodsPage?
}