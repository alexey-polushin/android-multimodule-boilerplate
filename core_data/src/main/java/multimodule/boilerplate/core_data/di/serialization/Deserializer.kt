package multimodule.boilerplate.core_data.di.serialization

import java.lang.reflect.Type

interface Deserializer {
    fun deserialize(
        targetType: Type,
        source: String?
    ): Any?
}