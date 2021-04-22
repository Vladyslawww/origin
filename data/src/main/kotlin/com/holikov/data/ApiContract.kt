package com.holikov.data

object ApiContract {

    /** Taxonomy api
     * @see com.erzhanov.etsyclient.data.categories.TaxonomyApi for more details
     */
    object Taxonomy {
        private const val TAXONOMY = "taxonomy"
        const val CATEGORIES = "$TAXONOMY/categories"
    }

    /** Listings api
     * @see com.erzhanov.etsyclient.data.listing.ListingsApi for more details
     */
    object Listings {
        private const val LISTINGS = "listings"

        const val ACTIVE_ITEMS = "$LISTINGS/active"
        const val GET_ITEM = "$LISTINGS/{listing_id}"
        const val IMAGES_BY_ID = "$LISTINGS/{listing_id}/images"
        const val PAGE_SIZE = 15
    }

}