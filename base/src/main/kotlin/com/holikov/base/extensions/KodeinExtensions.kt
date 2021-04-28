package com.holikov.base.extensions

import android.content.Context
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

inline fun <reified T : Any> Context.getInstance(tag: Any? = null): T {
    val kodein by closestKodein(this)
    val result by kodein.instance<T>(tag = tag)
    return result
}

