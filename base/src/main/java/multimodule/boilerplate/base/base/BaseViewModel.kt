package multimodule.boilerplate.base.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel(),
    LifecycleEventObserver {

    open fun onPause() {}

    open fun onResume() {}

    open fun onCreate() {}

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_CREATE -> onCreate()
            else -> {}
        }
    }
}