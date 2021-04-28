package com.holikov.base.base.view.fragment.bottomsheet

import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.holikov.base.base.view.fragment.FragmentModuleHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.kcontext

abstract class InjectionBottomSheetFragment : BottomSheetDialogFragment(), KodeinAware,
    FragmentModuleHolder {

    @SuppressWarnings("LeakingThisInConstructor")
    final override val kodein: Kodein = Kodein.lazy {
        val parentCodein by closestKodein(requireNotNull(activity))
        extend(parentCodein, allowOverride = true)
        import(fragmentModule, allowOverride = true)

    }
}