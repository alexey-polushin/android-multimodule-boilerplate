package multimodule.boilerplate.core.collection

import javax.annotation.CheckReturnValue

@CheckReturnValue
fun getIndexFromLoopIndex(index: Int, size: Int): Int {
    val modIndex = index.rem(size)
    return if (modIndex >= 0) {
        modIndex
    } else {
        modIndex + size
    }
}
