package multimodule.boilerplate.core_data.configuration

import android.net.Uri

interface ConfigurationManager {
    fun getBaseUrlApi(): Uri
    fun getEnvironmentList(): List<Environment>
    fun setCurrentEnvironment(environment: Environment)
    fun getCurrentEnvironment(): Environment
}