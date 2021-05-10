package com.holikov.myclient.saved

import android.os.Parcelable
import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.base.mappers.mapList
import com.holikov.domain.listing.model.GoodsItem
import com.holikov.domain.listing.usecases.ObserveSavedUseCase
import com.holikov.domain.listing.usecases.RemoveAllUseCase
import com.holikov.myclient.base.viewmodel.BaseViewModelImpl
import com.holikov.myclient.search.model.toApp
import kotlinx.coroutines.flow.onEach


interface SavedViewModel : BaseViewModel {
    fun removeAll()
}

internal class SavedViewModelImpl(
    private val savedUseCase: ObserveSavedUseCase,
    private val removeAllUseCase: RemoveAllUseCase
) : SavedViewModel, BaseViewModelImpl<StateSaved, ActionSaved>(StateSaved()) {

    override fun onCreate() {
        super.onCreate()
        if (state.noState) loadData()
    }

    override fun onLoadData() {
        savedUseCase.savedFlow().onEach { sendAction(ActionSaved.UpdateLoaded(it.process())) }.bind()
    }

    override fun onReduceState(action: ActionSaved) =
        when (action) {
            is ActionSaved.UpdateLoaded -> state.copy(items = action.items)
            else -> StateSaved()
        }

    override fun removeAll() {
        launchOn { removeAllUseCase.remove() }
    }

    private fun List<GoodsItem>.process() = mapList(GoodsItem::toApp)

}

internal data class StateSaved(
    val items: List<Parcelable> = emptyList()
) : BaseViewState {
    val noState = items.isEmpty()
}

internal sealed class ActionSaved : BaseAction {
    data class UpdateLoaded(val items: List<Parcelable>) : ActionSaved()
}