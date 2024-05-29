package multimodule.boilerplate.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

fun Context.dpToPx(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()

fun Context.pxToDp(value: Int) =
    (value / Resources.getSystem().displayMetrics.density).toInt()

val Context.statusBarHeight: Int
    @SuppressLint("InternalInsetResource")
    get() {
        return resources.getDimensionPixelSize(
            resources.getIdentifier("status_bar_height", "dimen", "android")
        )
    }