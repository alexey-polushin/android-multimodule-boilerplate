package multimodule.boilerplate.core.collection

import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentHashSetOf
import kotlinx.collections.immutable.persistentSetOf
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun <Item> PersistentSet<Item>?.orEmptySet(): PersistentSet<Item> =
    this ?: persistentSetOf()

@CheckReturnValue
fun <Item> PersistentSet<Item>?.orEmptyHashSet(): PersistentSet<Item> =
    this ?: persistentHashSetOf()

@CheckReturnValue
fun <Item> PersistentSet<Item>.addAll(items: Iterable<Item>): PersistentSet<Item> {
    var result = this

    for (item in items) {
        result = result.add(item)
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentSet<Item>.addAll(items: Sequence<Item>): PersistentSet<Item> {
    var result = this

    for (item in items) {
        result = result.add(item)
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentSet<Item>.removeAll(items: Iterable<Item>): PersistentSet<Item> {
    var result = this

    for (item in items) {
        result = result.remove(item)
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentSet<Item>.removeAll(items: Sequence<Item>): PersistentSet<Item> {
    var result = this

    for (item in items) {
        result = result.remove(item)
    }

    return result
}

@CheckReturnValue
inline fun <Upstream, Downstream> PersistentSet<Upstream>.mapPersistent(mapper: (Upstream) -> Downstream): PersistentSet<Downstream> {
    var result = create<Downstream>()

    for (item in this) {
        result = result.add(mapper(item))
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentSet<*>.create(): PersistentSet<Item> {
    @Suppress("UNCHECKED_CAST")
    return clear() as PersistentSet<Item>
}