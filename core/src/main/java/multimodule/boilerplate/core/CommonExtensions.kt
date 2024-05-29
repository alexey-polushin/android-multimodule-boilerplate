package multimodule.boilerplate.core

import android.content.res.TypedArray
import java.util.Locale

fun Double.roundTo(decimalPlaces: Int): Double =
    "%.${decimalPlaces}f".format(Locale.ENGLISH, this).toDouble()

fun <T> lazyGet(initializer: () -> T) = lazy(LazyThreadSafetyMode.PUBLICATION, initializer)

inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
    getInt(index, -1).let {
        if (it >= 0) enumValues<T>()[it] else default
    }