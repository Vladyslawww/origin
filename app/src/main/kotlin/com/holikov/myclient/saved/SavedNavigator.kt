package com.holikov.myclient.saved

import androidx.fragment.app.FragmentManager
import com.holikov.myclient.base.navigator.BaseNavigator
import com.holikov.myclient.details.DetailsFragment
import com.holikov.myclient.main.bottomsheet.SaveRemoveBottomSheet
import com.holikov.myclient.search.model.AppGoodsItem
import java.util.concurrent.atomic.AtomicReference

interface SavedNavigator : BaseNavigator {

    fun details(id: Long) {
        manager.get()?.to(fragment = DetailsFragment.saved(id))
    }

    fun bottomSheet(item: AppGoodsItem) {
        SaveRemoveBottomSheet.saved(item).apply { show(manager.get(), this::class.java.name) }
    }
}

internal class SavedNavigatorImpl : SavedNavigator {
    override val manager = AtomicReference<FragmentManager>()
}

