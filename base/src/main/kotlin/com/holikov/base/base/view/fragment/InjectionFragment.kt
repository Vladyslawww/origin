package com.holikov.base.base.view.fragment

import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.kcontext

abstract class InjectionFragment : Fragment(), KodeinAware, FragmentModuleHolder {

    @SuppressWarnings("LeakingThisInConstructor")
    final override val kodeinContext = kcontext<Fragment>(context = this)

    final override val kodein: Kodein = Kodein.lazy {
        val parentKodein by closestKodein(requireNotNull(activity))
        extend(parentKodein, allowOverride = true)
        import(fragmentModule, allowOverride = true)
    }

}