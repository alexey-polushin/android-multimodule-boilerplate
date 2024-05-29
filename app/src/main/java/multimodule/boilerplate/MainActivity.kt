package multimodule.boilerplate

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import multimodule.boilerplate.base.base.BaseActivity
import multimodule.boilerplate.base.base.BaseFragmentInterface
import multimodule.boilerplate.base.base.BaseNavHostFragment
import multimodule.boilerplate.base.locale.LanguageCode
import multimodule.boilerplate.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val languageCode = LanguageCode.tryFindByCode(newConfig.locales.get(0).language)
        languageCode?.let {
            localeManager.updateActivityLocale(it)
            (getCurrentFragment() as? BaseFragmentInterface)?.updateFragmentLocale(it)
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as BaseNavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(multimodule.boilerplate.base.R.navigation.nav_graph)
        navGraph.setStartDestination(multimodule.boilerplate.base.R.id.featureSplashFlowFragment)
        navController.graph = navGraph
    }

    private fun getCurrentFragment(): Fragment {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as BaseNavHostFragment
        return navHostFragment.getChildFragmentManager().fragments[0]
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        (getCurrentFragment() as? BaseFragmentInterface)?.handleActivityResult(
            requestCode,
            resultCode,
            data
        )
    }
}