package multimodule.boilerplate.base.base

import android.content.Intent
import multimodule.boilerplate.base.locale.LanguageCode

interface BaseFragmentInterface {
    fun onBackPressed(): Boolean
    fun updateFragmentLocale(languageCode: LanguageCode) {}
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun forceFlowClose()
    fun handleRequestError(error: Throwable, errorDataAction: () -> Unit = {}) {}
}