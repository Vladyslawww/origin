package com.holikov.base.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> MutableLiveData<T>.toLiveData() = this as LiveData<T>

fun <T> LifecycleOwner.observeData(liveData: LiveData<T>, body: (T) -> Unit = {}) {
    liveData.removeObservers(this)
    liveData.observe(this, Observer { it?.let { t -> body(t) } })
}

fun <T> MutableLiveData<List<T>>.postCombine(values: List<T>, merge: Boolean = true) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    if (merge) {
        value.addAll(values)
    } else {
        value.removeAll(values)
    }
    this.postValue(value)
}

fun <T> MutableLiveData<List<T>>.postAdd(item: T) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    value.add(item)
    postValue(value)
}

fun <T> MutableLiveData<List<T>>.postAddAt(position: Int, item: T) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    value.add(position, item)
    postValue(value)
}

fun <T> MutableLiveData<List<T>>.postSetAt(position: Int, item: T) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    value[position] = item
    postValue(value)
}

fun <T> MutableLiveData<List<T>>.postMoveTo(from: Int, to: Int, item: T) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    value.removeAt(from)
    value.add(to, item)
    postValue(value)
}

fun <T> MutableLiveData<List<T>>.postDelete(item: T) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    value.remove(item)
    postValue(value)
}

fun <T> MutableLiveData<List<T>>.postDeleteAt(position: Int) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    value.removeAt(position)
    postValue(value)
}

fun <T> MutableLiveData<List<T>>.postDeleteFirst(predicate: (T) -> Boolean) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    val index = value.indexOfFirst { predicate.invoke(it) }
    if (index < 0) return
    value.removeAt(index)
    postValue(value)
}

fun <T> MutableLiveData<List<T>>.addAt(value: T, position: Int) {
    val values = ArrayList<T>(this.value ?: arrayListOf())
    values.add(position, value)
    this.postValue(values)
}

fun <T> MutableLiveData<List<T>>.combine(values: List<T>, merge: Boolean = true) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    if (merge) {
        value.addAll(values)
    } else {
        value.removeAll(values)
    }
    this.value = (value)
}

fun <T> MutableLiveData<List<T>>.replaceItem(oldValue: T, newValue: T) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    val index = value.indexOf(oldValue)
    if (index > -1) {
        value[index] = newValue
    } else {
        value.add(newValue)
    }
    this.value = (value)
}

fun <T> MutableLiveData<List<T>>.postReplaceItem(oldValue: T, newValue: T) {
    val value = ArrayList<T>(this.value ?: arrayListOf())
    val index = value.indexOf(oldValue)
    if (index > -1) {
        value[index] = newValue
    } else {
        value.add(newValue)
    }
    this.postValue(value)
}

inline fun <reified T: Any?> LiveData<T>.withValue(valueJob:(T) -> Unit) {
    this.value?.let(valueJob)
}

inline fun <reified T: Any?> LiveData<T>.withValueOr(valueJob:(T) -> Unit, doIfNull:()-> Unit = {}) {
    this.value?.let(valueJob)?.let { doIfNull.invoke() }
}



