package com.holikov.myclient.base.di

import com.holikov.myclient.AppInfoProviderImpl
import com.holikov.myclient.base.*
import com.holikov.base.extensions.WORKER_POOL
import com.holikov.data.network.exceptions.ExceptionHandler
import com.holikov.data.network.exceptions.NetworkExceptionHandler
import com.holikov.domain.AppInfoProvider
import com.holikov.domain.di.domainModules
import com.holikov.data.dataModules
import kotlinx.coroutines.CoroutineExceptionHandler
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import kotlin.coroutines.CoroutineContext

fun applyModules(builder: Kodein.Builder) =
    builder.apply {
        import(appModule),
        import(dataModules),
        import(domainModules)
    }

private val appModule = Kodein.Module("appModule") {

}