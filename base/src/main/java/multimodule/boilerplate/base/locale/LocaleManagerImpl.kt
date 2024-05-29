package multimodule.boilerplate.base.locale

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LocaleManagerImpl(
    private val context: Context
) : LocaleManager {
    private fun commonUpdateLocale(languageCode: LanguageCode): Configuration {
        val locale = languageCode.toLocale()
        Locale.setDefault(locale)
        val conf: Configuration = context.applicationContext.resources.configuration
        conf.setLocale(locale)
        conf.setLayoutDirection(locale)
        context.applicationContext.createConfigurationContext(conf)
        return conf
    }

    override fun setLocale(languageCode: LanguageCode, activity: AppCompatActivity) {
        val conf = commonUpdateLocale(languageCode)
        activity.onConfigurationChanged(conf)
    }

    @Suppress("DEPRECATION")
    override fun updateActivityLocale(languageCode: LanguageCode) {
        val conf = commonUpdateLocale(languageCode)
        context.resources.updateConfiguration(conf, context.resources.displayMetrics)
    }

    override fun getCurrentLocale(): Locale {
        return context.applicationContext.resources.configuration.locales.get(0)
    }

}