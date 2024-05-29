package multimodule.boilerplate.core_data.api

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errorMessage")
    val errorMessage: String?,

    @SerializedName("errorData")
    val errorData: Any?,

    @SerializedName("errorAction")
    val errorAction: List<String>?,
)