package com.holikov.base.base.list.paging

import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicReference

class PagingListCallback(private val trigger: AtomicReference<Boolean>) :
    RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        trigger.set(false)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        trigger.set(false)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart, itemCount, payload)
        trigger.set(false)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        trigger.set(false)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        trigger.set(false)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        trigger.set(false)
    }

}