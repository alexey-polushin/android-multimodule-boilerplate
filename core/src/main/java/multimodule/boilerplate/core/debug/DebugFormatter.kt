package multimodule.boilerplate.core.debug

import multimodule.boilerplate.core.collection.toPersistentList
import multimodule.boilerplate.core.format.Formatter
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.toImmutableList
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

object DebugFormatter : Formatter<DebugData> {
    @CheckReturnValue
    private fun Appendable._appendType(
        type: Type,
        representation: DebugRepresentation
    ) {
        append(
            if (type is Class<*>) {
                when (representation) {
                    DebugRepresentation.Full -> type.toString()
                    DebugRepresentation.Compact -> type.simpleName
                }
            } else {
                type.toString()
            }
        )
        if (type is ParameterizedType) {
            append('<')

            for (typeParameter in type.actualTypeArguments) {
                _appendType(type, representation)
            }

            append('>')
        }
    }

    @CheckReturnValue
    override fun format(data: DebugData): String = buildString {
        _appendType(data.type, data.representation)

        val hasDescription = !data.description.isNullOrEmpty()
        if (hasDescription || data.isCollection) {
            append('{')

            if (data.isCollection) {
                append("size = ")
                append(data.items.size)

                if (hasDescription) {
                    append(" | ")
                }
            }

            if (hasDescription) {
                append(data.description)
            }

            append('}')
        }

        if (data.isCollection) {
            append('[')

            for (indexedItem in data.items.withIndex()) {
                val value = indexedItem.value
                if (value === null) {
                    append("null")
                } else {
                    append('`')

                    append(value.toDebugString(data.representation))

                    append('`')
                }

                if (indexedItem.index < data.items.size - 1) {
                    append(", ")
                }
            }

            append(']')
        }
    }
}

@CheckReturnValue
fun Any.formatDebugSingle(
    representation: DebugRepresentation,
    description: String? = null
): String = DebugFormatter.format(
    DebugData.single(
        this@formatDebugSingle.javaClass,
        representation,
        description
    )
)

@CheckReturnValue
fun Any.formatDebugCollection(
    items: ImmutableCollection<Any?>,
    representation: DebugRepresentation,
    description: String? = null
): String = DebugFormatter.format(
    DebugData.collection(
        this@formatDebugCollection.javaClass,
        items,
        representation,
        description
    )
)

@CheckReturnValue
fun Any.toDebugString(representation: DebugRepresentation = DebugRepresentation.Compact): String {
    return when (this) {
        is DebugRepresentable -> toString(representation)
        is Collection<*> -> formatDebugCollection(this.toImmutableList(), representation)
        is Array<*> -> formatDebugCollection(this.toPersistentList(), representation)
        else -> toString()
    }
}