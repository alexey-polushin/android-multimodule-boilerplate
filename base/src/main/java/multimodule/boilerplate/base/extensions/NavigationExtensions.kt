package multimodule.boilerplate.base.extensions

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController

@SuppressLint("DiscouragedApi")
fun Fragment.activityNavController() =
    requireActivity().findNavController(
        resources.getIdentifier(
            "nav_host_fragment",
            "id",
            requireActivity().packageName
        )
    )

@SuppressLint("DiscouragedApi")
fun Fragment.flowNavigateSafely(
    navDirections: NavDirections,
    navOptions: NavOptions? = null
) {
    val navController: NavController = this.activityNavController()
    navController.navigate(navDirections, navOptions)
}

fun NavController.navigateSafely(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId) }
}

fun NavController.navigateSafely(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}