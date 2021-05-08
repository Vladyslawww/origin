package com.holikov.myclient.main.bottomsheet

import androidx.fragment.app.Fragment
import com.holikov.base.di.KodeinViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

object SaveRemoveModule {

    operator fun invoke() = Kodein.Module(javaClass.simpleName) {
        bind<SaveRemoveViewModel>()  with scoped<Fragment>(AndroidLifecycleScope).singleton {
            KodeinViewModelProvider.of(context) { SaveRemoveViewModelImpl(instance(), instance(), instance()) }
        }
    }
}