package multimodule.boilerplate.base.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import multimodule.boilerplate.base.di.basicActivityModule
import multimodule.boilerplate.base.locale.LocaleManager
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.android.subDI
import org.kodein.di.instance

private const val KEEP_SPLASH_SCREEN_PARAM = "KEEP_SPLASH_SCREEN_PARAM"

abstract class BaseActivity: AppCompatActivity(), DIAware {

    final override val di: DI by subDI(closestDI()) {
        importOnce(basicActivityModule(this@BaseActivity), allowOverride = true)
    }

    protected val localeManager: LocaleManager by instance()

    protected var keepSplashScreen = true

    private val _pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Принудительное отключение темной темы
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEEP_SPLASH_SCREEN_PARAM, keepSplashScreen)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        keepSplashScreen = savedInstanceState.getBoolean(KEEP_SPLASH_SCREEN_PARAM, false)
    }

    fun skipSplashScreen() {
        keepSplashScreen = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            _pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun showKeyboard(view: View) {
        view.doOnLayout {
            WindowCompat.getInsetsController(window, it).show(WindowInsetsCompat.Type.ime())
        }
    }

    fun hideKeyboard(view: View) {
        view.doOnLayout {
            WindowCompat.getInsetsController(window, it).hide(WindowInsetsCompat.Type.ime())
        }
    }
}