package multimodule.boilerplate.core.value.core

import multimodule.boilerplate.core.EqualityComparable
import javax.annotation.CheckReturnValue

interface Value<Self : Value<Self>> : EqualityComparable<Self> {
    @CheckReturnValue
    override fun toString(): String
}