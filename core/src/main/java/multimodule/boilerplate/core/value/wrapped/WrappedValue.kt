package multimodule.boilerplate.core.value.wrapped

import multimodule.boilerplate.core.EqualityComparable
import multimodule.boilerplate.core.debug.DebugRepresentation
import multimodule.boilerplate.core.debug.formatDebugSingle
import multimodule.boilerplate.core.value.core.AbstractValue

abstract class WrappedValue<Value : EqualityComparable<Value>, Self : WrappedValue<Value, Self>>(
    protected val value: Value
) : AbstractValue<Self>() {
    fun unwrap(): Value = value

    override fun internalEquals(other: Self): Boolean = value == other.value

    override fun internalHashCode(): Int = value.hashCode()

    override fun internalToString(): String =
        formatDebugSingle(DebugRepresentation.Compact, value.toString())
}