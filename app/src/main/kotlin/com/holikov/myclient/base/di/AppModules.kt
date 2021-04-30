package com.holikov.myclient.base.di

import com.holikov.myclient.base.*
import com.holikov.base.extensions.WORKER_POOL
import com.holikov.data.dataModules
import com.holikov.data.network.exceptions.ExceptionHandler
import com.holikov.data.network.exceptions.NetworkExceptionHandler
import com.holikov.domain.AppInfoProvider
import com.holikov.domain.di.domainModules
import kotlinx.coroutines.CoroutineExceptionHandler
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import kotlin.coroutines.CoroutineContext
import com.holikov.myclient.AppInfoProviderImpl as AppInfoProviderImpl

fun applyModules(builder: Kodein.Builder) =
    builder.apply {
        import(appModule)
        import(dataModules)
        import(domainModules)
    }

private val appModule = Kodein.Module("appModule") {
    import(appInfoModule)
    import(errorHandlersModule)
    import(initializersModule)
}

private val errorHandlersModule = Kodein.Module("errorHandlers", false) {
    bind<CoroutineContext>() with singleton { WORKER_POOL + instance<CoroutineExceptionHandler>() }
    bind<ExceptionHandler>() with singleton { NetworkExceptionHandler(instance()) }
}

private val appInfoModule = Kodein.Module("appInfo") {
    bind<AppInfoProvider>() with singleton { AppInfoProviderImpl(instance()) }
}
private val initializersModule = Kodein.Module("initializers") {
    bind() from setBinding<Initializer>()
    bind<Initializer>().inSet() with singleton { stethoInitializer(instance()) }
    bind<Initializer>().inSet() with singleton { fabricInitializer(instance()) }
    bind<Initializer>().inSet() with singleton { timberInitializer(instance()) }
    bind<Initializer>().inSet() with singleton { firebaseInitializer() }
}


