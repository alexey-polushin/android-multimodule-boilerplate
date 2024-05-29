package multimodule.boilerplate.core_data.di.serialization

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer

interface JsonAdapter<T> : JsonSerializer<T>, JsonDeserializer<T>