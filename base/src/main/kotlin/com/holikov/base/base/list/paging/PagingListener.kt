package com.holikov.base.base.list.paging

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicReference

typealias PagingAdapter = () -> Unit

class PagingListener(private val pageSize: Int = 15) : RecyclerView.OnScrollListener() {

    private val isLoading = AtomicReference(false)

    private val isRegistered = AtomicReference(false)

    private val listCallback = PagingListCallback(isLoading)

    private val pageListener = AtomicReference<PagingAdapter>()

    private val currentScroll = AtomicReference(0)

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        if (isRegistered.getAndSet(true).not()) view.adapter?.registerAdapterDataObserver(listCallback)

        if (currentScroll.getAndSet(dx) < dx) return

        if (isLoading.get() || view.isComputingLayout == true) return

        val layoutManager = view.layoutManager ?: return

        val itemCount = layoutManager.itemCount
        val lastVisiblePosition = (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
            ?: (layoutManager as? GridLayoutManager)?.findLastVisibleItemPosition()
            ?: throw IllegalArgumentException("Paging not supported for ${view.layoutManager}")
        val offset = lastVisiblePosition.plus(layoutManager.offset())

        if (offset > itemCount && itemCount % pageSize >= 0) onNext()

    }

    private fun RecyclerView.LayoutManager.offset(): Int {
        return (this as? GridLayoutManager)?.spanCount?.times(2) ?: 5
    }

    private fun onNext() {
        isLoading.set(true)
        pageListener.get()?.invoke()
    }

    fun attach(pageListener: PagingAdapter): PagingListener {
        this.pageListener.set(pageListener)
        return this
    }
}


fun RecyclerView.paging(listener: PagingAdapter) = PagingAttacher(this, listener).also(::addOnAttachStateChangeListener)
fun RecyclerView.clearPaging(listener: PagingAttacher) = removeOnAttachStateChangeListener(listener)

class PagingAttacher(view: RecyclerView, listener: PagingAdapter) : View.OnAttachStateChangeListener {

    private val list = AtomicReference(view)

    private val pagination = AtomicReference(PagingListener().attach(listener))

    init {
        list.get()?.addOnScrollListener(pagination.get())
    }

    override fun onViewDetachedFromWindow(p0: View?) {
        list.get()?.apply {
            removeOnScrollListener(pagination.get())
            removeOnAttachStateChangeListener(this@PagingAttacher)
        }
    }

    override fun onViewAttachedToWindow(p0: View?) = Unit
}