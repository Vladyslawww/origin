package com.holikov.myclient

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.holikov.data.dataModules
import com.holikov.domain.di.domainModules
import com.holikov.myclient.base.Initializer
import com.holikov.myclient.base.di.applyModules
import com.holikov.myclient.utils.ForegroundAppDetector
import com.holikov.myclient.utils.ForegroundAppDetectorImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.conf.global
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class App : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        invokeGlobalModules()
        invokeInitializers()
    }

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@App))
        bind<Context>() with provider { this@App }
        bind<ForegroundAppDetector>() with eagerSingleton {
            ForegroundAppDetectorImpl().apply {
                ProcessLifecycleOwner.get().lifecycle.addObserver(this@apply)
            }
        }
        applyModules(this)
    }


    private fun invokeGlobalModules() {
        Kodein.global.apply {
            addImport(androidXModule(this@App))
            addImport(dataModules)
            addImport(domainModules)
        }
    }

    private fun invokeInitializers() {
        val initializers by kodein.instance<Set<Initializer>>()
        initializers.forEach { it.invoke(this) }
    }

}
