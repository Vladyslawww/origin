package com.holikov.data

import android.provider.MediaStore

object ApiContract {

    /** Taxonomy api
     * @see com.holikov.data.categories.TaxonomyApi for more details
     */
    object Taxonomy {
        private const val TAXONOMY = "taxonomy"
        const val CATEGORIES = "$TAXONOMY/categories"
    }

    /** Listings api
     * @see com.holikov.data.listing.ListingsApi for more details
     */
    object Listings {
        private const val LISTINGS = "listings"

        const val ACTIVE_ITEMS = "$LISTINGS/active"
        const val GET_ITEM = "$LISTINGS/{listing_id}"
        const val PAGE_SIZE = 15
        const val INCLUDE_IMAGE_VALUE = "Images:1:0"

    }

}