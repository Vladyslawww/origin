package com.holikov.myclient.splash

import androidx.fragment.app.Fragment
import com.holikov.base.di.KodeinViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

object SplashModule {

    operator fun invoke() = Kodein.Module(javaClass.simpleName) {
        bind<SplashViewModel>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
            KodeinViewModelProvider.of(context) { SplashViewModelImpl() }
        }
        bind<SplashNavigator>() with singleton { SplashNavigatorImpl() }
    }
}