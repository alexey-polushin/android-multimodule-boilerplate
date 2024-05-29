package multimodule.boilerplate.core_data.api

import android.util.Log
import com.google.gson.Gson
import retrofit2.Response


fun <T : Any> Response<T>.handleResponse(): ResultWrapper<T> {
    if (this.isSuccessful) {
        this.body()?.let {
            return ResultWrapper.success(it)
        }
    }

    this.errorBody()?.let { body ->
        return try {
            val errorConverted = Gson().fromJson(body.string(), ErrorResponse::class.java)
            ResultWrapper.error(
                HttpUserVisibleException(
                    errorConverted.errorMessage,
                    errorConverted.errorData,
                    errorConverted.errorAction
                )
            )
        } catch (e: Exception) {
            Log.e("ResponseHandler", "Failed to parse error response. $e")
            ResultWrapper.error(e)
        }
    }

    return ResultWrapper.error(RuntimeException())
}

suspend fun <T : Any> returnSafely(
    block: suspend () -> ResultWrapper<T>
): ResultWrapper<T> {
    return try {
        block.invoke()
    } catch (e: Exception) {
        Log.e("ResponseHandler", e.stackTraceToString())
        ResultWrapper.error(exception = e)
    }
}
