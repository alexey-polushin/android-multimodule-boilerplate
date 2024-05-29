package multimodule.boilerplate.core.collection

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun <Item> List<Item>.loopGet(index: Int): Item = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
/** Заменяет элемент в списке на [newValue] если [block] - true */
fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}

@CheckReturnValue
    /** Заменяет элемент в списке на [newValue] если [block] - true */
fun <T> PersistentList<T>.replace(newValue: T, block: (T) -> Boolean): PersistentList<T> {
    return map {
        if (block(it)) newValue else it
    }.toPersistentList()
}