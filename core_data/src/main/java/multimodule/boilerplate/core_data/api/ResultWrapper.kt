package multimodule.boilerplate.core_data.api

sealed class ResultWrapper<out T> {
    object Loading : ResultWrapper<Nothing>()

    data class Error(val exception: Throwable) : ResultWrapper<Nothing>()

    data class Success<out T>(val data: T) : ResultWrapper<T>()

    companion object {
        fun <T : Any> success(data: T): Success<T> = Success(data)

        fun error(exception: Throwable): Error = Error(exception)

        fun loading(): Loading = Loading
    }
}