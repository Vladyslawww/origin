package com.holikov.base.extensions

import android.content.res.Resources
import android.util.TypedValue
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlin.Exception

fun tryOrIgnore(action: () -> Unit) {
    try {
        action()
    } catch (ignore: Exception) {
    }
}

fun <T> tryOr(action: () -> T): T? {
    return try {
        action()
    } catch (ignore: Exception) {
        return null
    }
}

fun <T> tryOr(action: () -> T, error: (Exception) -> T): T {
    return try {
        action()
    } catch (exception: Exception) {
        return error(exception)
    }
}

suspend fun <T> tryAwaitOr(action: suspend () -> Deferred<T>): T? {
    return try {
        action().await()
    } catch (ignore: Exception) {
        return null
    }
}

suspend fun tryJobOrIgnore(action: Job) {
    return try {
        action.join()
    } catch (ignore: Exception) {
    }
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

inline val Float.px: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)