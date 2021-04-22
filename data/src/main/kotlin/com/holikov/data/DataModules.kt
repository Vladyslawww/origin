package com.holikov.data

import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.holikov.data.categories.CategoriesRepositoryImpl
import com.holikov.data.categories.TaxonomyApi
import com.holikov.data.listing.GoodsRepositoryImpl
import com.holikov.data.network.AuthenticationInterceptor
import com.holikov.data.network.TransportExceptionInterceptor
import com.holikov.domain.AppInfoProvider
import com.holikov.domain.categories.CategoriesRepository
import com.holikov.domain.listing.GoodsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import com.holikov.data.db.GoodsDatabase
import com.holikov.data.listing.GoodsDao
import com.holikov.data.listing.ListingsApi
import com.holikov.data.network.exceptions.NetworkApiErrorConverter
import com.holikov.data.network.exceptions.NetworkApiErrorConverterImpl


val dataModules = Kodein.Module("dataModules", false) {
    import(networkModule)
    import(dbModule)
    import(prefsModule)
    import(repositoryModule)
    import(coroutinesModule)
}

private val dbModule = Kodein.Module("dbModule") {
    bind() from singleton { GoodsDatabase.create(instance()) }
    bind<GoodsDao>() with singleton { instance<GoodsDatabase>().goodsDao() }
}

const val MAIN_RETROFIT = "mainRetrofit"
private val networkModule = Kodein.Module("network") {

    bind() from singleton { TransportExceptionInterceptor() }
    bind() from singleton { AuthenticationInterceptor(instance<AppInfoProvider>().apiKey) }
    bind<Retrofit.Builder>() with singleton { Retrofit.Builder() }
    bind<HttpLoggingInterceptor>() with singleton { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }

    bindOkHttpBuilder()

    bind<OkHttpClient>() with singleton {
        instance<OkHttpClient.Builder>()
            .addInterceptor(instance<AuthenticationInterceptor>())
            .addInterceptor(instance<TransportExceptionInterceptor>())
            .build()
    }

    bind<Retrofit>(MAIN_RETROFIT) with singleton {
        instance<Retrofit.Builder>()
            .baseUrl(instance<AppInfoProvider>().endpoint)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(instance()).build()
    }

    bind<NetworkApiErrorConverter>() with singleton { NetworkApiErrorConverterImpl(instance(MAIN_RETROFIT)) }

    bindRetrofitApi<TaxonomyApi>()
    bindRetrofitApi<ListingsApi>()

}

private const val PERMISSIONS_PREFS = "prefs_permissions"
private const val CONFIG_PREFS = "config_permissions"


private val prefsModule = Kodein.Module("prefs") {

    bind<SharedPreferences>(PERMISSIONS_PREFS) with singleton { instance<Context>().getSharedPreferences(PERMISSIONS_PREFS, Context.MODE_PRIVATE) }
    bind<SharedPreferences>(CONFIG_PREFS) with singleton { instance<Context>().getSharedPreferences(CONFIG_PREFS, Context.MODE_PRIVATE) }

}

private val repositoryModule = Kodein.Module("repositories") {
    bind<CategoriesRepository>() with singleton { CategoriesRepositoryImpl(instance()) }
    bind<GoodsRepository>() with singleton { GoodsRepositoryImpl(instance(), instance()) }
}


private const val ERROR_IGNORE_CONTEXT = "error_ignore_context"
private val coroutinesModule = Kodein.Module("coroutines") {
    bind<CoroutineExceptionHandler>(ERROR_IGNORE_CONTEXT) with singleton { CoroutineExceptionHandler { _, e -> System.err.println("Error in global scope ${e.localizedMessage}") } }
}
const val POOL_SIZE = 8
const val TIMEOUT: Long = 2

private fun Kodein.Builder.bindOkHttpBuilder() {
    bind<OkHttpClient.Builder>() with singleton {
        OkHttpClient.Builder().apply {
            dispatcher(Dispatcher().apply { maxRequests = POOL_SIZE })
            connectTimeout(TIMEOUT, TimeUnit.MINUTES).readTimeout(TIMEOUT, TimeUnit.MINUTES).writeTimeout(TIMEOUT, TimeUnit.MINUTES)
            if (instance<AppInfoProvider>().isDebug)
                addNetworkInterceptor(StethoInterceptor()).addInterceptor(instance<HttpLoggingInterceptor>())
        }
    }
}

private inline fun <reified T : Any> Kodein.Builder.bindRetrofitApi(tag: String = MAIN_RETROFIT) {
    bind<T>() with singleton {
        val retrofit: Retrofit = instance(tag)
        retrofit.create(T::class.java)
    }
}

