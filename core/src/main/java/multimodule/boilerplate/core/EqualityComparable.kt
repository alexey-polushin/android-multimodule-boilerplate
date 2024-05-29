package multimodule.boilerplate.core

import javax.annotation.CheckReturnValue

interface EqualityComparable<T> {
    @CheckReturnValue
    fun equalTo(other: T?): Boolean

    @CheckReturnValue
    override operator fun equals(other: Any?): Boolean

    @CheckReturnValue
    override fun hashCode(): Int
}