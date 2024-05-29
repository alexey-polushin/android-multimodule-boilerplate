package multimodule.boilerplate.core.value.wrapped

import multimodule.boilerplate.core.string.Stringable
import java.util.Locale
import javax.annotation.CheckReturnValue

abstract class WrappedCaseInsensitiveStringValue<Self : WrappedCaseInsensitiveStringValue<Self>>(
    value: String
) : WrappedCompatValue<String, Self>(value), Stringable {
    @CheckReturnValue
    final override fun internalEquals(other: Self): Boolean =
        value.equals(other.value, ignoreCase = true)

    @CheckReturnValue
    final override fun internalHashCode(): Int =
        value.uppercase(Locale.ROOT).hashCode()

    @CheckReturnValue
    final override fun asString(): String = unwrap()
}