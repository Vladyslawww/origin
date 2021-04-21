package com.holikov.myclient.base.extentions


import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment


fun Fragment.hideKeyboard(view: View? = null) = activity?.hideKeyboard(view)

fun Fragment.showKeyboard(view: View?) = activity?.showKeyboard(view)

fun Activity.hideKeyboard(view: View? = null) {
    val viewInner = view ?: currentFocus
    viewInner?.let { getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(it.windowToken, 0) }
}

fun View.hideKeyboard() {
    val inputMethod = context.getSystemService<InputMethodManager>()
    inputMethod?.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.showKeyboard(view: View?) {
    view?.let {
        view.requestFocus()
        val keyboard = getSystemService<InputMethodManager>()
        keyboard?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }
}

fun View.showKeyboard() {
    requestFocus()
    val keyboard = context.getSystemService<InputMethodManager>()
    keyboard?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}