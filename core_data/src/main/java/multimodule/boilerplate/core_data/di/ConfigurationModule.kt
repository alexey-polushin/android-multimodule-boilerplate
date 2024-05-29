package multimodule.boilerplate.core_data.di

import multimodule.boilerplate.core_data.configuration.ConfigurationManagerImpl
import multimodule.boilerplate.core_data.configuration.ConfigurationManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val configurationModule = DI.Module(name = "configuration") {
    bind<ConfigurationManager>() with singleton {
        ConfigurationManagerImpl(
            _context = instance(),
        )
    }
}