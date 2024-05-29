package multimodule.boilerplate.core.string

import javax.annotation.CheckReturnValue

interface Stringable {
    @CheckReturnValue
    fun asString(): String
}