package multimodule.boilerplate.core_data.di.serialization

import java.lang.reflect.Type

interface Serializer {
    fun serialize(
        targetType: Type,
        target: Any?
    ): String?
}