package com.holikov.base.extensions

import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun FragmentManager.launchOnUi(crossinline job: () -> Unit) {
    if (isUI()) job.invoke()
    else launchWith(UI_POOL) { job.invoke() }
}

fun isUI() = Looper.getMainLooper().thread == Thread.currentThread()

fun FragmentManager.isEmpty() = fragments.isEmpty()

inline fun <reified T : Fragment> FragmentManager.popTo() = popBackStack(T::class.java.name, 0)

inline fun <reified T : Fragment> FragmentManager.findAdded() =
    fragments.find { it is T && it.isAdded } as T?

inline fun <reified T : Fragment> FragmentManager.find() =
    fragments.filterIsInstance<T>().firstOrNull()