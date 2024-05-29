package multimodule.boilerplate.base.error

import multimodule.boilerplate.base.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.annotation.CheckReturnValue

object ErrorMessages {
    @CheckReturnValue
    tailrec fun ofException(error: Throwable?): Int {
        return when (error) {
            is UnknownHostException,
            is SocketTimeoutException,
            is ConnectException -> R.string.common__error_no_connection

            is RuntimeException -> {
                val cause = error.cause
                if (cause != null) {
                    ofException(cause)
                } else {
                    R.string.common__error_unknown
                }
            }
            else -> R.string.common__error_unknown
        }
    }
}