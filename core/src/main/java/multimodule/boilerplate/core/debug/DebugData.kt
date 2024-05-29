package multimodule.boilerplate.core.debug

import multimodule.boilerplate.core.lazyGet
import multimodule.boilerplate.core.contract.contracted
import kotlinx.collections.immutable.ImmutableCollection
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

class DebugData
private constructor(
    val type: Type,
    val description: String?,
    val isCollection: Boolean,
    private val _items: ImmutableCollection<Any?>?,
    val representation: DebugRepresentation
) {
    val items: ImmutableCollection<Any?> by lazyGet {
        contracted(
            pre = {
                it
                    .value(isCollection, "isCollection")
                    .mustBeTrue()
                    .because("only the collection type has elements")
            }
        ) {
            _items!!
        }
    }

    companion object {
        @CheckReturnValue
        fun single(
            type: Type,
            representation: DebugRepresentation,
            description: String? = null
        ) = DebugData(type, description, false, null, representation)

        @CheckReturnValue
        fun collection(
            type: Type,
            items: ImmutableCollection<Any?>,
            representation: DebugRepresentation,
            description: String? = null
        ) = DebugData(type, description, true, items, representation)
    }
}