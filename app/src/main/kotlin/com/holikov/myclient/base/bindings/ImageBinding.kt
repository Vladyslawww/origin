package com.holikov.myclient.base.bindings

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.holikov.myclient.base.extentions.itemImage

object ImageBinding {

    @JvmStatic
    @BindingAdapter("app:itemImage")
    fun itemImage(view: AppCompatImageView, url: String?) = view.itemImage(url)
}