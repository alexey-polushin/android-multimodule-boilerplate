package multimodule.boilerplate.core_data.account

import multimodule.boilerplate.data.domain.login.token.AccessToken
import javax.annotation.CheckReturnValue

interface AccountManager {
    @CheckReturnValue
    fun getAccessToken(): AccessToken?
    fun setAccessToken(accessToken: AccessToken)
    fun removeAccessToken()

    fun logout()
}