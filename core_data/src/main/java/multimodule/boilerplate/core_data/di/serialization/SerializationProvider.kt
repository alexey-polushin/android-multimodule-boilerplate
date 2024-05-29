package multimodule.boilerplate.core_data.di.serialization

interface SerializationProvider {
    val deserializer: Deserializer
    val serializer: Serializer
}
