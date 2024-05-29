package multimodule.boilerplate.base.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import multimodule.boilerplate.base.locale.LocaleManager
import multimodule.boilerplate.base.locale.LocaleManagerImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindings.subTypes
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import org.kodein.di.with
import org.kodein.type.jvmType

val baseModule = DI.Module(name = "BASE") {
    bind<LocaleManager>() with singleton {
        LocaleManagerImpl(
            context = instance()
        )
    }
}

fun basicActivityModule(activity: AppCompatActivity) = DI.Module("activity") {
    bind<Context>(overrides = true).subTypes() with { type ->
        when (type.jvmType) {
            Context::class.java -> provider { activity }
            AppCompatActivity::class.java -> provider { activity }
            else -> throw NoSuchElementException()
        }
    }
}

fun basicFragmentModule(fragment: Fragment) = DI.Module("fragment") {
    bind<Fragment>() with singleton { fragment }

    bind<Context>(overrides = true).subTypes() with { type ->
        when (type.jvmType) {
            Context::class.java -> provider { fragment.requireContext() }
            AppCompatActivity::class.java -> provider { fragment.requireActivity() }
            else -> throw NoSuchElementException()
        }
    }
}
