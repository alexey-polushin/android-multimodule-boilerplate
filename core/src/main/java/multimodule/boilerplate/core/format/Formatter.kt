package multimodule.boilerplate.core.format

import androidx.annotation.AnyThread
import javax.annotation.CheckReturnValue

interface Formatter<in Data> {
    @AnyThread
    @CheckReturnValue
    fun format(data: Data): String
}