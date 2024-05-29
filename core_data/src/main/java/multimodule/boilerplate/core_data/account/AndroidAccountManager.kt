package multimodule.boilerplate.core_data.account

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import multimodule.boilerplate.data.domain.login.token.AccessToken
import javax.annotation.CheckReturnValue


class AndroidAccountManager
@RequiresPermission(
    allOf = [
        Manifest.permission.GET_ACCOUNTS,
        "android.permission.AUTHENTICATE_ACCOUNTS"
    ]
)
constructor(
    context: Context
) : AccountManager {

    companion object {
        private const val CRYPTO_ALIAS: String = "CRYPTO_ALIAS"
        private const val APP_PREFERENCES_ALIAS: String = "APP_PREFERENCES_ALIAS"
        private const val ACCESS_TOKEN_PREF: String = "ACCESS_TOKEN_PREF"
    }

    private var masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var cryptoSharedPreferences = EncryptedSharedPreferences.create(
        context,
        CRYPTO_ALIAS,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private var sharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES_ALIAS, Context.MODE_PRIVATE)

    @CheckReturnValue
    override fun getAccessToken(): AccessToken? {
        val accessToken = cryptoSharedPreferences.getString(ACCESS_TOKEN_PREF, null)
        return accessToken?.let { AccessToken.restore(it) }
    }

    override fun setAccessToken(accessToken: AccessToken) {
        cryptoSharedPreferences.edit().putString(ACCESS_TOKEN_PREF, accessToken.unwrap()).apply()
    }

    override fun removeAccessToken() {
        cryptoSharedPreferences.edit().remove(ACCESS_TOKEN_PREF).apply()
    }

    override fun logout() {
        removeAccessToken()
    }
}