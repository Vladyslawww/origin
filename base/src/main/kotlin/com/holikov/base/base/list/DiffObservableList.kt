package com.holikov.base.base.list

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.ListChangeRegistry
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class DiffObservableList<T> : AbstractList<T>(), ObservableList<T> {

    private val listeners = ListChangeRegistry()
    private val differ = AtomicReference<MutableList<T>>(mutableListOf())
    private val callback: SimpleDiffCallback = SimpleDiffCallback()
    private val listCallback = ObservableListUpdateCallback()

    override fun addOnListChangedCallback(@NonNull listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.add(listener)
    }

    override fun removeOnListChangedCallback(@NonNull listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.remove(listener)
    }

    override fun indexOf(element: T): Int {
        return differ.get().indexOf(element)
    }

    override fun lastIndexOf(element: T): Int {
        return differ.get().lastIndexOf(element)
    }

    @NonNull
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return differ.get().subList(fromIndex, toIndex)
    }

    @NonNull
    override fun listIterator(index: Int): MutableListIterator<T> {
        return differ.get().listIterator(index)
    }

    override fun get(index: Int): T {
        return differ.get()[index]
    }

    override val size: Int get() = differ.get().size

    fun update(items: List<T>) {
        callback.latch(items)
        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(listCallback)
        differ.set(items.toMutableList())
    }

    fun change(@Nullable newItems: List<T>) {
        val old = listeners.copyCallbacks()
        listeners.clear()
        differ.set(newItems.toMutableList())
        old.forEach(listeners::add)
    }

    internal inner class SimpleDiffCallback : DiffUtil.Callback() {

        private val newItems = AtomicReference<List<T>>(emptyList())

        fun latch(items: List<T>) {
            newItems.set(items)
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return differ.get()[oldItemPosition] == newItems.get()[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return differ.get()[oldItemPosition] == newItems.get()[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return differ.get().size
        }

        override fun getNewListSize(): Int {
            return newItems.get().size
        }
    }

    internal inner class ObservableListUpdateCallback : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            listeners.notifyChanged(this@DiffObservableList, position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            modCount += 1
            listeners.notifyInserted(this@DiffObservableList, position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            modCount += 1
            listeners.notifyRemoved(this@DiffObservableList, position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            listeners.notifyMoved(this@DiffObservableList, fromPosition, toPosition, 1)
        }
    }
}