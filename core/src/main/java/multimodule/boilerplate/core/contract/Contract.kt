package common.library.core.contract

import javax.annotation.CheckReturnValue

interface Contract {
    fun assert()

    @CheckReturnValue
    fun check(): Boolean
}