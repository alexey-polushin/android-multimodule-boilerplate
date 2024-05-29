package multimodule.boilerplate.core.value.wrapped

import multimodule.boilerplate.core.debug.DebugRepresentation
import multimodule.boilerplate.core.debug.formatDebugSingle
import multimodule.boilerplate.core.value.core.AbstractValue
import javax.annotation.CheckReturnValue

abstract class WrappedCompatValue<Value : Comparable<Value>, Self : WrappedCompatValue<Value, Self>>(
    protected val value: Value
) : AbstractValue<Self>() {
    @CheckReturnValue
    fun unwrap(): Value = value

    @CheckReturnValue
    override fun internalEquals(other: Self): Boolean = value == other.value

    @CheckReturnValue
    override fun internalHashCode(): Int = value.hashCode()

    @CheckReturnValue
    override fun internalToString(): String =
        formatDebugSingle(DebugRepresentation.Compact, value.toString())
}