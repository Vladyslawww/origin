package com.holikov.myclient.base.extentions


import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

inline fun bundleOf(block: Bundle.() -> Unit): Bundle {
    val bundle = Bundle()
    block(bundle)
    return bundle
}


fun Bundle.clear(vararg keys: String) = keys.forEach { remove(it) }

// Only for single int
fun Bundle.int(value: Int?, key: String) = value?.apply { putInt(key, value) }.let { this }

fun Bundle.int(value: Int?) = int(value, Int::class.java.simpleName)

fun Bundle.long(value: Long?, key: String) = value?.run { putLong(key, value) }.let { this }

fun Bundle.long(value: Long?) = long(value, Long::class.java.simpleName)

// Only for single string
fun Bundle.string(value: String?, key: String) = value?.apply { putString(key, value) }.let { this }

// Only for single charSequence
fun Bundle.charSequence(value: CharSequence?, key: String) = value?.apply { putCharSequence(key, value) }.let { this }

// Only for single string
fun Bundle.string(value: String?) = string(value, String::class.java.simpleName)

// Only for single charSequence
fun Bundle.charSequence(value: CharSequence?) = charSequence(value, CharSequence::class.java.simpleName)

fun Bundle.bool(value: Boolean, key: String) = apply { putBoolean(key, value) }.let { this }

// Only for single bool
fun Bundle.bool(value: Boolean) = bool(value, Boolean::class.java.simpleName)

// Only for single string
fun Bundle.uri(value: Uri?) = parcelable(value)

fun Bundle.getInt() = getInt(Int::class.java.simpleName)

fun Bundle.getLong(): Long? = getLong(Long::class.java.simpleName)

fun Bundle.getString(): String = requireNotNull(getString(String::class.java.simpleName))

fun Bundle.getCharSequence(): CharSequence = requireNotNull(getCharSequence(CharSequence::class.java.simpleName))

fun Bundle.getStringOrNull(key: String): String? = getString(key)

fun Bundle.getStringOrNull(): String? = getString(String::class.java.simpleName)

fun Bundle.getBool() = getBoolean(Boolean::class.java.simpleName)

inline fun <reified T: Parcelable> Bundle.parcelable(params: T?) = apply { putParcelable(T::class.java.simpleName, params) }

inline fun <reified T: Parcelable> Bundle.parcelable() = requireNotNull(getParcelable<T>(T::class.java.simpleName))

inline fun <reified T: Serializable> Bundle.serializable(params: T?) = apply { putSerializable(T::class.java.simpleName, params) }

inline fun <reified T: Serializable> Bundle.serializable() = getSerializable(T::class.java.simpleName) as T

inline fun <reified T: Parcelable> Bundle.list(list: List<T>) = apply { putParcelableArrayList(T::class.java.simpleName, ArrayList(list)) }

inline fun <reified T: Parcelable> Bundle.list() = getParcelableArrayList<T>(T::class.java.simpleName).orEmpty()

inline fun <reified T: Parcelable> Bundle.listNullable() = getParcelableArrayList<T>(T::class.java.simpleName)

inline fun <reified T: Parcelable> Bundle.parcelableOrNull() = getParcelable(T::class.java.simpleName) as T?

inline fun <reified T: Serializable> Bundle.serializableOrNull() = getSerializable(T::class.java.simpleName) as T?

