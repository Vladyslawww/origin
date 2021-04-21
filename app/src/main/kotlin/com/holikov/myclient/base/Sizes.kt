package com.holikov.myclient.base



import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


object Sizes {

    private const val MDPI_DENSITY = 160f

    fun dpToPx(context: Context, dip: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(),
            displayMetrics).toInt()
    }

    fun pxToDp(context: Context, pixels: Int): Float {
        val displayMetrics = context.resources.displayMetrics
        return pixels / (displayMetrics.densityDpi / MDPI_DENSITY)
    }

    fun screenWidthPx(context: Context): Int {
        if (context !is Activity)
            throw IllegalArgumentException("Pass activity context to measure screen")
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun screenHeightPx(context: Context): Int {
        if (context !is Activity)
            throw IllegalArgumentException("Pass activity context to measure screen")
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}
