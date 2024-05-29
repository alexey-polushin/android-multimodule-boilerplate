package multimodule.boilerplate.base.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import multimodule.boilerplate.base.locale.LanguageCode

abstract class BaseFlowFragment(
    @LayoutRes layoutId: Int,
    @IdRes private val navHostFragmentId: Int
) : Fragment(layoutId), BaseFragmentInterface, BaseFlowFragmentInterface {

    protected lateinit var navController: NavController

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(navHostFragmentId) as BaseNavHostFragment
        navController = navHostFragment.navController
        setupNavigation()
    }

    private fun getCurrentFragment(): Fragment {
        val navHostFragment =
            childFragmentManager.findFragmentById(navHostFragmentId) as BaseNavHostFragment
        return navHostFragment.getChildFragmentManager().fragments.first { it is BaseFragment }
    }

    protected open fun setupNavigation() {}

    override fun onBackPressed(): Boolean {
        return (getCurrentFragment() as? BaseFragmentInterface)?.onBackPressed() ?: false
    }

    override fun updateFragmentLocale(languageCode: LanguageCode) {
        (getCurrentFragment() as? BaseFragmentInterface)?.updateFragmentLocale(languageCode)
    }

    override fun forceFlowClose() {
        findNavController().popBackStack()
    }

    override fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        (getCurrentFragment() as? BaseFragmentInterface)?.handleActivityResult(
            requestCode,
            resultCode,
            data
        )
    }
}