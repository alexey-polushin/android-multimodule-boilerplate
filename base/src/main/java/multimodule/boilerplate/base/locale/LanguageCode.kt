package multimodule.boilerplate.base.locale

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

class LanguageCode private constructor(
    val code: String
) {
    fun toLocale(): Locale = Locale(code)

    companion object {
        val russian: LanguageCode
        val english: LanguageCode
        /*
        val chinese: LanguageCode
        val german: LanguageCode
        val french: LanguageCode
        val spanish: LanguageCode
         */

        val all: PersistentList<LanguageCode>

        fun tryFindByCode(code: String?): LanguageCode? =
            if (code.isNullOrEmpty()) {
                null
            } else {
                all.firstOrNull {
                    it.code == code
                }
            }

        init {
            var predefined: PersistentList<LanguageCode> = persistentListOf()

            fun define(
                code: String
            ): LanguageCode {
                val result = LanguageCode(
                    code = code
                )

                predefined = predefined.add(result)

                return result
            }

            russian = define("ru")
            english = define("en")

            /*
            chinese = define("zh")
            german = define("de")
            french = define("fr")
            spanish = define("es")
             */

            all = predefined
        }
    }

    override fun toString(): String {
        return code
    }
}
