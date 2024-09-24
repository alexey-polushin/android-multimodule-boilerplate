package multimodule.boilerplate.base.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import multimodule.boilerplate.base.R


private val defaultNavOptions = navOptions {
    anim {
        /*
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
         */
    }
}

private val emptyNavOptions = navOptions {}

class BaseNavHostFragment : NavHostFragment() {

    @Deprecated("Deprecated")
    @Suppress("DEPRECATION")
    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.navigatorProvider.addNavigator(
            FragmentNavigatorWithDefaultAnimations(requireContext(), childFragmentManager, id)
        )
    }

}

@Navigator.Name("fragment")
class FragmentNavigatorWithDefaultAnimations(
    context: Context,
    manager: FragmentManager,
    containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        val shouldUseTransitionsInstead = navigatorExtras != null
        val navOptionsItem = if (shouldUseTransitionsInstead) navOptions
        else navOptions.fillEmptyAnimationsWithDefaults()
        super.navigate(entries, navOptionsItem, navigatorExtras)
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val shouldUseTransitionsInstead = navigatorExtras != null
        val navOptionsItem = if (shouldUseTransitionsInstead) navOptions
        else navOptions.fillEmptyAnimationsWithDefaults()
        return super.navigate(destination, args, navOptionsItem, navigatorExtras)
    }

    private fun NavOptions?.fillEmptyAnimationsWithDefaults(): NavOptions =
        this?.copyNavOptionsWithDefaultAnimations() ?: defaultNavOptions

    private fun NavOptions.copyNavOptionsWithDefaultAnimations(): NavOptions =
        let { originalNavOptions ->
            navOptions {
                launchSingleTop = originalNavOptions.shouldLaunchSingleTop()
                popUpTo(originalNavOptions.popUpToId) {
                    inclusive = originalNavOptions.isPopUpToInclusive()
                }
                anim {
                    enter =
                        if (originalNavOptions.enterAnim == emptyNavOptions.enterAnim) defaultNavOptions.enterAnim
                        else originalNavOptions.enterAnim
                    exit =
                        if (originalNavOptions.exitAnim == emptyNavOptions.exitAnim) defaultNavOptions.exitAnim
                        else originalNavOptions.exitAnim
                    popEnter =
                        if (originalNavOptions.popEnterAnim == emptyNavOptions.popEnterAnim) defaultNavOptions.popEnterAnim
                        else originalNavOptions.popEnterAnim
                    popExit =
                        if (originalNavOptions.popExitAnim == emptyNavOptions.popExitAnim) defaultNavOptions.popExitAnim
                        else originalNavOptions.popExitAnim
                }
            }
        }

}