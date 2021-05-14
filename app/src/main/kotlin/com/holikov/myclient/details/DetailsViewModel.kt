package com.holikov.myclient.details

import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.base.mappers.mapItem
import com.holikov.domain.listing.model.GoodsItem
import com.holikov.domain.listing.usecases.IsSavedUseCase
import com.holikov.domain.listing.usecases.RemoteItemUseCase
import com.holikov.domain.listing.usecases.RemoveItemUseCase
import com.holikov.domain.listing.usecases.SaveItemUseCase
import com.holikov.myclient.base.viewmodel.BaseViewModelImpl
import com.holikov.myclient.details.ActionDetail.*
import com.holikov.myclient.search.model.AppGoodsItem
import com.holikov.myclient.search.model.toApp
import com.holikov.myclient.search.model.toDomain

interface DetailsViewModel : BaseViewModel {
    fun remove()
    fun save()
    fun update()
}

internal class DetailsViewModelImpl(
    private val isSavedUseCase: IsSavedUseCase,
    private val removeItemUseCase: RemoveItemUseCase,
    private val remoteItemUseCase: RemoteItemUseCase,
    private val saveItemUseCase: SaveItemUseCase
) : DetailsViewModel, BaseViewModelImpl<StateDetail, ActionDetail>(StateDetail()) {

    private val detailsArgs by lazy { getArgs<DetailsArgs>() }
    private val flow get() = detailsArgs.flow
    private val itemId get() = detailsArgs.id

    override fun onCreate() {
        super.onCreate()
        if (state.noState) loadData()
    }


    override fun onLoadData() {
        launchOn {
            val isSaved = isSavedUseCase.saved()
            remoteItemUseCase.get(itemId, isSaved)?.run {
                sendAction(ItemLoaded(isSaved.not(), this.mapItem(GoodsItem::toApp)))
            } ?: run { sendAction(DataLoadFailure) }
        }
    }

    override fun onReduceState(action: ActionDetail) =
        when (action) {
            is ItemLoaded -> state.copy(shouldSave = action.shouldSave, item = action.item)
            is ItemRemoved -> state.copy(shouldSave = true)
            is ItemSaved -> state.copy(shouldSave = false)
            else -> throw IllegalStateException("Illegal action")
        }

    override fun remove() {
        launchOn {
            removeItemUseCase.remove(state.item.listingId)
            sendAction(ItemRemoved)
        }
    }

    override fun save() {
        launchOn {
            saveItemUseCase.save(state.item.mapItem(AppGoodsItem::toDomain))
            sendAction(ItemSaved)
        }
    }

    override fun update() = save()

    private suspend fun IsSavedUseCase.saved() =
        when (flow) {
            DetailFlow.FROM_SAVED -> true
            DetailFlow.FROM_SEARCH -> isSaved(itemId)
        }

}

internal sealed class ActionDetail : BaseAction {
    data class ItemLoaded(val shouldSave: Boolean, val item: AppGoodsItem) : ActionDetail()
    object ItemSaved : ActionDetail()
    object ItemRemoved : ActionDetail()
    object DataLoadFailure : ActionDetail()
}

internal data class StateDetail(val shouldSave: Boolean = false, val item: AppGoodsItem = AppGoodsItem.empty()) : BaseViewState {
    val noState = item.listingId <= 0
}