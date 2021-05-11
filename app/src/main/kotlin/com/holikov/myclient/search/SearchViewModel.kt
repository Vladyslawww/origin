package com.holikov.myclient.search

import android.os.Parcelable
import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.base.mappers.mapList
import com.holikov.domain.listing.model.GoodsItem
import com.holikov.domain.listing.model.SearchPage
import com.holikov.domain.listing.usecases.SearchUseCase
import com.holikov.myclient.base.extentions.AppSkeletonItem
import com.holikov.myclient.base.viewmodel.BaseViewModelImpl
import com.holikov.myclient.search.model.AppGoodsItem
import com.holikov.myclient.search.model.toApp
import java.util.concurrent.atomic.AtomicReference
import com.holikov.myclient.search.ActionSearch.FirstPageLoaded
import com.holikov.myclient.search.ActionSearch.NextPageLoaded



interface SearchViewModel : BaseViewModel {
    fun query(text: String)
    fun refresh()
}

internal class SearchViewModelImpl(
    private val searchUseCase: SearchUseCase
) : SearchViewModel, BaseViewModelImpl<StateSearch, ActionSearch>(StateSearch()) {

    private val next = AtomicReference<Int>()
    private val query = AtomicReference<String>(null)

    override fun onCreate() {
        super.onCreate()
        if (state.noState) loadData()
    }

    override fun onLoadData() {
        launchWithProgressOn {
            searchUseCase.search(page = 1, keywords = query.get())
                .run { sendAction(ActionSearch.FirstPageLoaded(process())) }
        }
    }

    override fun onLoadNext() {
        next.get() ?: return
        launchOn {
            searchUseCase.search(page = next.get(), keywords = query.get())
                .run { sendAction(NextPageLoaded(process())) }
        }
    }

    override fun query(text: String) {
        val old = query.getAndSet(text)
        if (old != text) refresh()
    }

    override fun refresh() = loadData()

    override fun onReduceState(action: ActionSearch) =
        when (action) {
            is FirstPageLoaded -> state.copy(items = action.items)
            is NextPageLoaded -> state.copy(items = state.items.filterIsInstance<AppGoodsItem>() + action.items)
            else -> StateSearch()
        }

    private fun SearchPage.process(): List<Parcelable> {
        next.set(nextPage)
        return results.mapList(GoodsItem::toApp).paging(nextPage)
    }

    private fun List<Parcelable>.paging(nextPage: Int?) =
        nextPage?.run { this@paging + AppSkeletonItem(id = "${System.currentTimeMillis()}") } ?: this
}

internal sealed class ActionSearch : BaseAction {
    data class FirstPageLoaded(val items: List<Parcelable>) : ActionSearch()
    data class NextPageLoaded(val items: List<Parcelable>) : ActionSearch()
}

internal data class StateSearch(
    val items: List<Parcelable> = emptyList()
) : BaseViewState {
    val noState = items.isEmpty()
}