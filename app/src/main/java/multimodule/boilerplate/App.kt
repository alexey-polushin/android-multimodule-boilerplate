package multimodule.boilerplate

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import multimodule.boilerplate.base.di.baseModule
import multimodule.boilerplate.core_data.di.accountModule
import multimodule.boilerplate.core_data.di.apiModule
import multimodule.boilerplate.core_data.di.configurationModule
import multimodule.boilerplate.core_data.di.dbModule
import multimodule.boilerplate.di.basicApplicationModule
import multimodule.boilerplate.di.viewModelModule
import org.kodein.di.DI
import org.kodein.di.DIAware

class App: Application(), DIAware {
    override val di: DI by DI.lazy {
        importOnce(basicApplicationModule(this@App))
        import(apiModule(
            chuckerInterceptor = ChuckerInterceptor(this@App)
        ))
        import(dbModule)
        import(accountModule)
        import(configurationModule)
        import(baseModule)
        import(viewModelModule)
    }
}