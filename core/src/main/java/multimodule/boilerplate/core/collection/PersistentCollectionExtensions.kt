package multimodule.boilerplate.core.collection

import kotlinx.collections.immutable.PersistentCollection
import kotlinx.collections.immutable.persistentListOf
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun <Item> PersistentCollection<Item>?.orEmpty(): PersistentCollection<Item> =
    if (this !== null) this else persistentListOf()

@CheckReturnValue
fun <Item> PersistentCollection<Item>.addAll(items: Iterable<Item>): PersistentCollection<Item> {
    var result = this

    for (item in items) {
        result = result.add(item)
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentCollection<Item>.removeAll(items: Iterable<Item>): PersistentCollection<Item> {
    var result = this

    for (item in items) {
        result = result.remove(item)
    }

    return result
}