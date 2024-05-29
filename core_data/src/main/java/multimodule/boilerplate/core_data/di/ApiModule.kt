package multimodule.boilerplate.core_data.di

import multimodule.boilerplate.core_data.BuildConfig
import multimodule.boilerplate.core_data.configuration.ConfigurationManager
import multimodule.boilerplate.core_data.di.serialization.SerializerBodyConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.multiton
import org.kodein.di.singleton
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun apiModule(
    chuckerInterceptor: Interceptor? = null
) = DI.Module(name = "api") {
    bind<OkHttpClient>() with multiton {
        OkHttpClient
            .Builder()
            .writeTimeout(30, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .apply {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
                if (BuildConfig.DEBUG) {
                    chuckerInterceptor?.let { addInterceptor(it) }
                }
            }
            .build()
    }

    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .baseUrl(instance<ConfigurationManager>().getBaseUrlApi().toString())
            .client(instance())
            .addConverterFactory(SerializerBodyConverterFactory(instance()))
            .build()
    }
}