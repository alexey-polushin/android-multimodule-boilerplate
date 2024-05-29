package multimodule.boilerplate.core.debug

import javax.annotation.CheckReturnValue

interface DebugRepresentable {
    @CheckReturnValue
    fun toString(representation: DebugRepresentation): String

    @CheckReturnValue
    override fun toString(): String
}