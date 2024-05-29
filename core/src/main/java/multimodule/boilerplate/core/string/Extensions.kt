package multimodule.boilerplate.core.string

import java.util.Locale
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun String?.nullIfEmpty(): String? = if (isNullOrEmpty()) null else this

@CheckReturnValue
fun Double?.nullIfZero(): Double? = if (this == 0.0) null else this

@CheckReturnValue
fun Float.nullIfZero(): Float? = if (this == 0f) null else this

@CheckReturnValue
fun String?.nullIfBlank(): String? = if (isNullOrBlank()) null else this

@CheckReturnValue
fun CharSequence.endsWithDigit(): Boolean = lastOrNull()?.isDigit() == true

@CheckReturnValue
fun String.capitalize(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }