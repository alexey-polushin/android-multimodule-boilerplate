package multimodule.boilerplate.core.collection

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun <Item> PersistentList<Item>?.orEmptyList(): PersistentList<Item> =
    this ?: persistentListOf()

@CheckReturnValue
fun <Item> PersistentList<Item>.addAll(items: Iterable<Item>): PersistentList<Item> {
    var result = this

    for (item in items) {
        result = result.add(item)
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentList<Item>.addAll(items: Sequence<Item>): PersistentList<Item> {
    var result = this

    for (item in items) {
        result = result.add(item)
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentList<Item>.removeAll(items: Iterable<Item>): PersistentList<Item> {
    var result = this

    for (item in items) {
        result = result.remove(item)
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentList<Item>.removeAll(items: Sequence<Item>): PersistentList<Item> {
    var result = this

    for (item in items) {
        result = result.remove(item)
    }

    return result
}

@CheckReturnValue
inline fun <Upstream, Downstream> PersistentList<Upstream>.mapPersistent(mapper: (Upstream) -> Downstream): PersistentList<Downstream> {
    var result = create<Downstream>()

    for (item in this) {
        result = result.add(mapper(item))
    }

    return result
}

@CheckReturnValue
fun <Item> PersistentList<*>.create(): PersistentList<Item> {
    @Suppress("UNCHECKED_CAST")
    return clear() as PersistentList<Item>
}

fun <T> PersistentList<T>.nullIfEmpty() : PersistentList<T>? {
    return if (this.isEmpty()) null else this
}