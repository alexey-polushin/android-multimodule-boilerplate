package multimodule.boilerplate.core_data.api

class HttpUserVisibleException(
    val errorMessage: String?,
    val errorData: Any?,
    val errorAction: List<String>?,
) : RuntimeException()