package multimodule.boilerplate.core.value.wrapped

import multimodule.boilerplate.core.string.Stringable
import javax.annotation.CheckReturnValue

abstract class WrappedCaseSensitiveStringValue<Self : WrappedCaseSensitiveStringValue<Self>>(
    value: String
) : WrappedCompatValue<String, Self>(value), Stringable {
    @CheckReturnValue
    final override fun internalEquals(other: Self): Boolean =
        value.equals(other.value, ignoreCase = false)

    @CheckReturnValue
    final override fun internalHashCode(): Int =
        value.hashCode()

    @CheckReturnValue
    final override fun asString(): String = unwrap()
}