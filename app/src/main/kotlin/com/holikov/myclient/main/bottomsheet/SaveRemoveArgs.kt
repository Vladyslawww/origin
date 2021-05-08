package com.holikov.myclient.main.bottomsheet

import android.os.Parcelable
import com.holikov.myclient.search.model.AppGoodsItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SaveRemoveArgs(
    val item: AppGoodsItem,
    val flow: SaveRemoveFlow
) : Parcelable

enum class SaveRemoveFlow { FROM_SEARCH, FROM_SAVED }