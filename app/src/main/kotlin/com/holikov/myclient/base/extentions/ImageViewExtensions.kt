package com.holikov.myclient.base.extentions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.transform.RoundedCornersTransformation
import com.holikov.myclient.R
import coil.api.load

const val imageItemSize = 700
const val coverSize = 1200
const val corners = 80f

fun ImageView.itemImage(url: String? = null, @DrawableRes placeHolder: Int = R.drawable.ic_placeholder_fallback) {
    load(url) {
        allowHardware(false)
        fallback(placeHolder)
        placeholder(placeHolder)
        transformations(RoundedCornersTransformation(corners))
        size(imageItemSize)
    }
}

fun ImageView.cover(url: String? = null, @DrawableRes placeHolder: Int = R.mipmap.ic_launcher) {
    load(url) {
        allowHardware(false)
        fallback(placeHolder)
        size(coverSize)
    }
}