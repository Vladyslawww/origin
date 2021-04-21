package com.holikov.myclient.base.extentions


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.holikov.myclient.R
import com.holikov.myclient.base.Sizes


fun Context.drawable(@DrawableRes id: Int): Drawable = AppCompatResources.getDrawable(this, id)?:
throw NullPointerException("Drawable is null")

fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Context.colorState(id: Int) = ContextCompat.getColorStateList(this, id)?:
throw NullPointerException("ColorStateList is null")

fun Context.getSelectableBackground(): Drawable? {
    val outValue = TypedValue()
    theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
    return ContextCompat.getDrawable(this, outValue.resourceId)
}

inline fun <reified T> Context.getSystemServiceEx(serviceName: String): T {
    return getSystemService(serviceName) as T
}

fun Context.getDimens(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Context.getAttributeColor(attr: Int): Int {
    val attrs = intArrayOf(attr)
    val typedArray = obtainStyledAttributes(attrs)
    val color = typedArray.getResourceId(0, Color.TRANSPARENT)
    typedArray.recycle()
    return color(color)
}

fun Context.getAttributeColorStateList(attr: Int): ColorStateList {
    val attrs = intArrayOf(attr)
    val typedArray = obtainStyledAttributes(attrs)
    val color = typedArray.getResourceId(0, Color.TRANSPARENT)
    typedArray.recycle()
    return colorState(color)
}

val Context.screenWidthPx: Int get() = Sizes.screenWidthPx(this)

fun Context.openApplicationSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.resolveAttrValue(attrId: Int): TypedValue? {
    val typedValue = TypedValue()
    val attrResolved = theme.resolveAttribute(attrId, typedValue, true)
    return if (attrResolved) {
        typedValue
    } else {
        null
    }
}

inline fun Context.withStyleFromTheme(themeAttrId: Int, attrsSet: IntArray,
                                      block: TypedArray.() -> Unit) {
    val styleValue = TypedValue()
    val resolved = theme.resolveAttribute(themeAttrId, styleValue, true)

    if (resolved.not()) throw IllegalStateException("Style not defined in theme!")

    val typedArray = obtainStyledAttributes(styleValue.data, attrsSet)
    try {
        typedArray.block()
    } finally {
        typedArray.recycle()
    }
}

inline fun Context.withStyledAttributes(set: AttributeSet?, attrs: IntArray, defStyle: Int,
                                        defStyleAttr: Int = 0, block: TypedArray.() -> Unit) {
    set ?: return
    val typedArray = obtainStyledAttributes(set, attrs, defStyle, defStyleAttr)
    try {
        typedArray.block()
    } finally {
        typedArray.recycle()
    }
}

fun TypedArray.getResourceIdOrThrow(resId: Int): Int {
    if (hasValue(resId).not()) {
        throw IllegalArgumentException("Attribute not defined in set.")
    }
    return getResourceId(resId, -1)
}

fun Drawable.applyColor(context: Context, @ColorRes colorResId: Int): Drawable {
    return mutate().let {
        DrawableCompat.wrap(it).also {
            DrawableCompat.setTint(it, context.color(colorResId))
            DrawableCompat.setTintMode(it, PorterDuff.Mode.SRC_IN)
        }
    }
}



fun Context.longToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.longToast(@StringRes id: Int) =
    Toast.makeText(this, id, Toast.LENGTH_LONG).show()

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes id: Int) =
    Toast.makeText(this, id, Toast.LENGTH_SHORT).show()


