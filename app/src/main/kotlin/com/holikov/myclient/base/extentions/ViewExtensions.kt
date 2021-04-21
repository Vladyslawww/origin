package com.holikov.myclient.base.extentions


import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.View.*
import androidx.annotation.DimenRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.flow.debounce
import ru.ldralighieri.corbind.appcompat.queryTextChanges

inline fun View.goneIf(crossinline predicate: () -> Boolean) {
    visibility = if (predicate.invoke()) GONE else VISIBLE
}

inline fun View.visibleIf(crossinline predicate: () -> Boolean) {
    visibility = if (predicate.invoke()) VISIBLE else GONE
}

inline fun View.visibleIfAnd(predicate: Boolean, crossinline action: () -> Boolean) {
    visibility = if (predicate) VISIBLE.also { action() } else GONE
}

inline fun View.visibleIfOr(crossinline predicate: () -> Boolean, orAction: () -> Unit) {
    visibility = if (predicate.invoke()) VISIBLE else GONE.also { orAction.invoke() }
}

inline fun View.goneIfOr(crossinline predicate: () -> Boolean, orAction: () -> Unit) {
    visibility = if (predicate.invoke()) VISIBLE.also { orAction.invoke() } else GONE
}

inline fun View.goneIfOr(predicate: Boolean, orAction: () -> Unit) {
    visibility = if (predicate) VISIBLE.also { orAction.invoke() } else GONE
}

inline fun View.invisibleIf(crossinline predicate: () -> Boolean) {
    visibility = if (predicate.invoke()) INVISIBLE else VISIBLE
}

inline fun View.invisibleIfOr(predicate: Boolean, orAction: () -> Unit) {
    visibility = if (predicate) INVISIBLE else VISIBLE.also { orAction.invoke() }
}

fun View.gone() {
    visibility = GONE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.visible() {
    visibility = VISIBLE
}

fun View.visible(visible: Boolean = true) {
    visibility = if (visible) VISIBLE else GONE
}

val View.isVisible: Boolean get() = this.visibility == VISIBLE

fun View.paddingStart(@DimenRes dimens: Int) =
    this.setPadding(resources.getDimensionPixelSize(dimens), paddingTop, paddingRight, paddingBottom)

fun View.paddingEnd(@DimenRes dimens: Int) =
    this.setPadding(paddingLeft, paddingTop, resources.getDimensionPixelSize(dimens), paddingBottom)

fun View.paddingTop(@DimenRes dimens: Int) =
    this.setPadding(paddingLeft, resources.getDimensionPixelSize(dimens), paddingRight, paddingBottom)

fun View.paddingBottom(@DimenRes dimens: Int) =
    this.setPadding(paddingLeft, paddingTop, paddingRight, resources.getDimensionPixelSize(dimens))


fun View.updateBackgroundCornerRadius(cornerRadius: Float) {
    (background as? GradientDrawable)?.cornerRadius = cornerRadius
}

fun View.updateBackgroundColor(color: Int) {
    (background as? GradientDrawable)?.setColor(color)
}

fun SwipeRefreshLayout.show() { isRefreshing = true }

fun SwipeRefreshLayout.hide() { isRefreshing = false }

/**
 * Performs the given action when this view is next laid out.
 *
 * @see doOnLayout
 */
inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            view.removeOnLayoutChangeListener(this)
            action(view)
        }
    })
}
const val DEBOUNCE_INTERVAL = 300L
fun SearchView.queryChanges() = queryTextChanges().debounce(DEBOUNCE_INTERVAL)

fun RecyclerView.set(listAdapter: RecyclerView.Adapter<*>) = adapter ?: run { adapter = listAdapter }
fun RecyclerView.clear() = run { adapter = null }

/**
 * Performs the given action when this view is laid out. If the view has been laid out and it
 * has not requested a layout, the action will be performed straight away, otherwise the
 * action will be performed after the view is next laid out.
 *
 * @see doOnNextLayout
 */
inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
        action(this)
    } else {
        doOnNextLayout {
            action(it)
        }
    }
}



