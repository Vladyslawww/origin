package com.holikov.base.base.list.paging

import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicReference

typealias PagingAdapter = () -> Unit

class PagingListener(private val pageSize: Int = 15) : RecyclerView.OnScrollListener() {

    private val isLoading = AtomicReference(false)

    private val isRegistered = AtomicReference(false)

    private val listCallBack = PagingListCallback(isLoading)

    private val pageListener = AtomicReference<PagingAdapter>()

    private val currentScroll = AtomicReference(0)
}