package com.holikov.myclient.base.extentions


import android.os.Parcelable
import androidx.databinding.ObservableArrayList
import com.holikov.base.base.list.AsyncDiffObservableList
import com.github.nitrico.lastadapter.StableId
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


fun <T> AsyncDiffObservableList<T>.addTo(new: List<T>) = plus(new).run { update(this) }

fun <T> AsyncDiffObservableList<T>.clearItems() = update(emptyList())

fun <T> ObservableArrayList<T>.update(new: List<T>) {
    clear()
    addAll(new)
}

fun <T> AsyncDiffObservableList<T>.mutate(list: MutableList<T>.() -> Unit): MutableList<T> {
    return list().toMutableList().apply(list)
}

fun <T> AsyncDiffObservableList<T>.change(predicate: List<T>.() -> Int, element: T) {
    val indexed = list().predicate()
    if (indexed == -1) return
    mutate { set(indexed, element) }.run(::update)
}

fun <T> AsyncDiffObservableList<T>.update(index: Int, element: T) {
    mutate { set(index, element) }.run(::update)
}

fun <T> AsyncDiffObservableList<T>.updateItems(new: List<T>) { update(new) }

fun <T> AsyncDiffObservableList<T>.addAt(index: Int, element: T) {
    mutate { add(index, element) }.run(::update)
}

fun <T> AsyncDiffObservableList<T>.addItem(element: T) {
    mutate { add(element) }.run(::update)
}

fun <T> AsyncDiffObservableList<T>.removeItem(element: T) {
    mutate { remove(element) }.run(::update)
}

fun <T> AsyncDiffObservableList<T>.moveItem(from: Int, to: Int, element: T) {
    mutate { removeAt(from); add(to, element) }.run(::update)
}

fun AsyncDiffObservableList<Parcelable>.skeleton(range: Int = 10) = apply {
    IntRange(0, range.dec()).map { AppSkeletonItem() }.toList<Parcelable>().let(::update)
}

@Parcelize
data class AppSkeletonItem(val id: String = ""): Parcelable, StableId {
    @IgnoredOnParcel
    override val stableId = id.hashCode().toLong()
}
