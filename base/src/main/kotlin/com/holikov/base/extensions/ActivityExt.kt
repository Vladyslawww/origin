package com.holikov.base.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import kotlin.math.roundToInt

private const val ERROR_OFFSET_MARGIN = 50F

val Activity.rootView: View
    get() = findViewById(android.R.id.content)

val Activity.isKeyboardOpen: Boolean
    get() {
        val visibleBounds = Rect()
        this.rootView.getWindowVisibleDisplayFrame(visibleBounds)
        val heightDiff = rootView.height - visibleBounds.height()
        val marginOfError = ERROR_OFFSET_MARGIN.px.roundToInt()
        return heightDiff > marginOfError
    }