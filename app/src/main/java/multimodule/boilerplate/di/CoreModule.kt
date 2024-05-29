package multimodule.boilerplate.di

import android.app.Application
import android.content.Context
import multimodule.boilerplate.base.format.ContextDisplayFormat
import multimodule.boilerplate.core.format.DisplayFormat
import multimodule.boilerplate.core_data.di.serialization.GsonSerializationProvider
import multimodule.boilerplate.core_data.di.serialization.SerializationProvider
import multimodule.boilerplate.core_data.di.serialization.adapters.registerGlobalAdapters
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindings.subTypes
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import org.kodein.di.with
import org.kodein.type.jvmType

fun basicApplicationModule(application: Application) = DI.Module("application") {
    bind<Context>().subTypes() with { type ->
        when (type.jvmType) {
            Context::class.java -> provider { application }
            Application::class.java -> provider { application }
            else -> throw NoSuchElementException()
        }
    }

    bind<SerializationProvider>() with singleton {
        GsonSerializationProvider.build {
            it
                .registerGlobalAdapters()
        }
    }

    bind<DisplayFormat>() with provider {
        ContextDisplayFormat(
            instance()
        )
    }
}