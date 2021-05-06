package com.holikov.myclient.details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailsArgs(val id: Long, val flow: DetailFlow) : Parcelable

enum class DetailFlow { FROM_SEARCHED, FROM_SAVED }

