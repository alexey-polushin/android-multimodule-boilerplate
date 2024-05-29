package multimodule.boilerplate.core_data.configuration

import android.content.Context
import android.net.Uri


class ConfigurationManagerImpl
constructor(
    private val _context: Context
) : ConfigurationManager {

    companion object {
        private const val APP_PREFERENCES_ALIAS: String = "APP_PREFERENCES_ALIAS"
        private const val ENDPOINT_PREF: String = "ENDPOINT_PREF"
    }

    private var sharedPreferences =
        _context.getSharedPreferences(APP_PREFERENCES_ALIAS, Context.MODE_PRIVATE)

    override fun getBaseUrlApi(): Uri {
        val endpoint = getCurrentEnvironment()
       return Uri.parse(endpoint.baseUrl)
    }

    override fun getEnvironmentList(): List<Environment> = Environment.entries

    override fun setCurrentEnvironment(environment: Environment) {
        sharedPreferences.edit().putString(ENDPOINT_PREF, environment.name).apply()
    }

    override fun getCurrentEnvironment(): Environment {
        return when (sharedPreferences.getString(ENDPOINT_PREF, "")) {
            "Prod" -> Environment.Prod
            else -> Environment.Prod
        }
    }

}