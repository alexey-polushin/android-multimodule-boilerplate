package multimodule.boilerplate.base.base

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import multimodule.boilerplate.base.di.basicFragmentModule
import multimodule.boilerplate.base.error.ErrorMessages
import multimodule.boilerplate.base.extensions.activityNavController
import multimodule.boilerplate.core_data.api.HttpUserVisibleException
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.android.x.closestDI

abstract class BaseFragment : Fragment(), DIAware, BaseFragmentInterface {
    final override val di: DI by subDI(closestDI()) {
        importOnce(basicFragmentModule(this@BaseFragment), allowOverride = true)
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun forceFlowClose() {
        val navHostFragment = parentFragment as BaseNavHostFragment?
        val parent = navHostFragment?.parentFragment as BaseFlowFragment?
        parent?.forceFlowClose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (onBackPressed()) {
                if (!findNavController().navigateUp()) {
                    if (!activityNavController().popBackStack()) {
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    override fun handleRequestError(
        error: Throwable, errorDataAction: () -> Unit
    ) {
        if (error is HttpUserVisibleException) {
            if (error.errorData != null) {
                errorDataAction()
            } else {
                showErrorDialog(error.errorMessage ?: getString(ErrorMessages.ofException(error)))
            }
        } else {
            showErrorDialog(getString(ErrorMessages.ofException(error)))
        }
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(
            requireContext()
        ).setMessage(message)
            .show()
    }

    fun findFlowNavController(): NavController? {
        val navHostFragment = parentFragment as BaseNavHostFragment?
        val parent = navHostFragment?.parentFragment as BaseFlowFragment?
        return parent?.findNavController()
    }
}