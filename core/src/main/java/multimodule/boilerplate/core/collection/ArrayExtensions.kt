package multimodule.boilerplate.core.collection

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import javax.annotation.CheckReturnValue

//region toPersistentList

@CheckReturnValue
fun <Value> Array<Value>.toPersistentList(): PersistentList<Value> =
    persistentListOf(*this)

@CheckReturnValue
fun CharArray.toPersistentList(): PersistentList<Char> =
    persistentListOf<Char>().addAll(asList())

@CheckReturnValue
fun ByteArray.toPersistentList(): PersistentList<Byte> =
    persistentListOf<Byte>().addAll(asList())

@CheckReturnValue
fun ShortArray.toPersistentList(): PersistentList<Short> =
    persistentListOf<Short>().addAll(asList())

@CheckReturnValue
fun IntArray.toPersistentList(): PersistentList<Int> =
    persistentListOf<Int>().addAll(asList())

@CheckReturnValue
fun LongArray.toPersistentList(): PersistentList<Long> =
    persistentListOf<Long>().addAll(asList())

@CheckReturnValue
fun FloatArray.toPersistentList(): PersistentList<Float> =
    persistentListOf<Float>().addAll(asList())

@CheckReturnValue
fun DoubleArray.toPersistentList(): PersistentList<Double> =
    persistentListOf<Double>().addAll(asList())

//endregion toPersistentList

//region toPersistentSet

@CheckReturnValue
fun <Value> Array<Value>.toPersistentSet(): PersistentSet<Value> =
    persistentSetOf(*this)

@CheckReturnValue
fun CharArray.toPersistentSet(): PersistentSet<Char> =
    persistentSetOf<Char>().addAll(asList())

@CheckReturnValue
fun ByteArray.toPersistentSet(): PersistentSet<Byte> =
    persistentSetOf<Byte>().addAll(asList())

@CheckReturnValue
fun ShortArray.toPersistentSet(): PersistentSet<Short> =
    persistentSetOf<Short>().addAll(asList())

@CheckReturnValue
fun IntArray.toPersistentSet(): PersistentSet<Int> =
    persistentSetOf<Int>().addAll(asList())

@CheckReturnValue
fun LongArray.toPersistentSet(): PersistentSet<Long> =
    persistentSetOf<Long>().addAll(asList())

@CheckReturnValue
fun FloatArray.toPersistentSet(): PersistentSet<Float> =
    persistentSetOf<Float>().addAll(asList())

@CheckReturnValue
fun DoubleArray.toPersistentSet(): PersistentSet<Double> =
    persistentSetOf<Double>().addAll(asList())

//endregion toPersistentSet

//region loopGet

@CheckReturnValue
fun <Item> Array<Item>.loopGet(index: Int): Item = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
fun CharArray.loopGet(index: Int): Char = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
fun ByteArray.loopGet(index: Int): Byte = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
fun ShortArray.loopGet(index: Int): Short = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
fun IntArray.loopGet(index: Int): Int = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
fun LongArray.loopGet(index: Int): Long = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
fun FloatArray.loopGet(index: Int): Float = get(getIndexFromLoopIndex(index, size))

@CheckReturnValue
fun DoubleArray.loopGet(index: Int): Double = get(getIndexFromLoopIndex(index, size))

//endregion loopGet

