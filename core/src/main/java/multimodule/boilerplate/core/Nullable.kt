package multimodule.boilerplate.core


sealed class Nullable<out A> {
    abstract fun unwrap(): A?

    object Null : Nullable<Nothing>() {
        override fun unwrap(): Nothing? = null
    }

    data class Some<out A>(val value: A) : Nullable<A>() {
        override fun unwrap(): A = value
    }
}

fun <T> T?.toNullable(): Nullable<T> =
    this?.let { Nullable.Some(it) } ?: Nullable.Null