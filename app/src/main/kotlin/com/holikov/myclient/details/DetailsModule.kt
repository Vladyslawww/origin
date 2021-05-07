package com.holikov.myclient.details

import androidx.fragment.app.Fragment
import com.holikov.base.di.KodeinViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

object DetailsModule {

    operator fun invoke() = Kodein.Module(javaClass.simpleName) {
        bind<DetailsViewModel>() with scoped<Fragment>(AndroidLifecycleScope).singleton {
            KodeinViewModelProvider.of(context) { DetailsViewModelImpl(instance(), instance(), instance(), instance()) }
        }
        bind<DetailsNavigator>() with singleton { DetailsNavigatorImpl() }
    }
}