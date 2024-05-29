package multimodule.boilerplate.base.locale

import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

interface LocaleManager {
    fun setLocale(languageCode: LanguageCode, activity: AppCompatActivity)
    fun updateActivityLocale(languageCode: LanguageCode)
    fun getCurrentLocale(): Locale
}