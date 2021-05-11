package com.holikov.myclient.search

import androidx.fragment.app.FragmentManager
import com.holikov.myclient.base.navigator.BaseNavigator
import com.holikov.myclient.details.DetailsFragment
import com.holikov.myclient.main.bottomsheet.SaveRemoveBottomSheet
import com.holikov.myclient.search.model.AppGoodsItem
import java.util.concurrent.atomic.AtomicReference

interface SearchNavigator : BaseNavigator {

    fun details(id: Long) {
        manager.get()?.to(fragment = DetailsFragment.search(id))
    }

    fun bottomSheet(item: AppGoodsItem) {
        SaveRemoveBottomSheet.search(item).apply { show(manager.get(), this::class.java.name) }
    }
}

internal class SearchNavigatorImpl : SearchNavigator {
    override val manager = AtomicReference<FragmentManager>()
}
