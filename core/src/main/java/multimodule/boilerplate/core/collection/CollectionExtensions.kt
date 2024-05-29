@file:Suppress("unused")

package multimodule.boilerplate.core.collection

fun <Key, Value> Map<Key, Value>.getDefaultIfNull(
    key: Key,
    default: Value
): Value = get(key) ?: default