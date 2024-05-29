package multimodule.boilerplate.feature_splash.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import multimodule.boilerplate.base.NavGraphDirections
import multimodule.boilerplate.base.base.BaseActivity
import multimodule.boilerplate.base.base.BaseFragment
import multimodule.boilerplate.base.extensions.flowNavigateSafely
import multimodule.boilerplate.feature_splash.databinding.FragmentSplashScreenBinding


class SplashFeatureFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
           (requireActivity() as? BaseActivity)?.skipSplashScreen()
            flowNavigateSafely(
                NavGraphDirections.navigateToFeatureMainFlowFragment()
            )
        }, 1500)
    }

}