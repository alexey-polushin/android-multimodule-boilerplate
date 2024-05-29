package multimodule.boilerplate.data.domain.login.token

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import multimodule.boilerplate.core.value.wrapped.WrappedCaseInsensitiveStringValue
import javax.annotation.CheckReturnValue

@Parcelize
class AccessToken
private constructor(val data: String) :
    WrappedCaseInsensitiveStringValue<AccessToken>(data), Parcelable {
    @CheckReturnValue
    companion object {
        fun restore(value: String): AccessToken = AccessToken(value)
    }
}