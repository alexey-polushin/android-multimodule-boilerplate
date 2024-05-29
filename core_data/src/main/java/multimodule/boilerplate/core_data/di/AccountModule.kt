package multimodule.boilerplate.core_data.di

import multimodule.boilerplate.core_data.account.AccountManager
import multimodule.boilerplate.core_data.account.AndroidAccountManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val accountModule = DI.Module(name = "account") {
    bind<AccountManager>() with singleton {
        AndroidAccountManager(
            context = instance(),
        )
    }
}