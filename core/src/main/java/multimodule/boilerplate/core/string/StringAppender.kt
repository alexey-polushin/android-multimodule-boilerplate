package multimodule.boilerplate.core.string

import javax.annotation.CheckReturnValue

interface StringAppender {
    @CheckReturnValue
    fun append(chars: CharSequence?): StringAppender

    @CheckReturnValue
    fun append(
        chars: CharSequence?,
        start: Int,
        end: Int
    ): StringAppender

    @CheckReturnValue
    fun append(char: Char): StringAppender

    @CheckReturnValue
    fun append(
        value: String,
        vararg formatArgs: Any
    ): StringAppender
}