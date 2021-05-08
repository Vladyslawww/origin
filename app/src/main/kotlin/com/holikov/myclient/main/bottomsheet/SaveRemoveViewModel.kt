package com.holikov.myclient.main.bottomsheet

import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.base.mappers.mapItem
import com.holikov.domain.listing.usecases.IsSavedUseCase
import com.holikov.domain.listing.usecases.RemoveItemUseCase
import com.holikov.domain.listing.usecases.SaveItemUseCase
import com.holikov.myclient.base.viewmodel.BaseViewModelImpl
import com.holikov.myclient.search.model.AppGoodsItem
import com.holikov.myclient.search.model.toDomain

interface SaveRemoveViewModel : BaseViewModel {
    fun remove()
    fun save()
}

internal class SaveRemoveViewModelImpl(
    private val isSavedUseCase: IsSavedUseCase,
    private val removeItemUseCase: RemoveItemUseCase,
    private val saveItemUseCase: SaveItemUseCase
) : SaveRemoveViewModel, BaseViewModelImpl<StateSaveRemove, ActionSaveRemove>(StateSaveRemove.NoState) {

    private val saveRemoveArgs by lazy { getArgs<SaveRemoveArgs>() }

    private val flow get() = saveRemoveArgs.flow
    private val item get() = saveRemoveArgs.item

    override fun onCreate() {
        super.onCreate()
        if (state is StateSaveRemove.NoState) loadData()
    }

    override fun onLoadData() {
        launchOn {
            val action = when (flow) {
                SaveRemoveFlow.FROM_SEARCH -> ActionSaveRemove.ItemStatusLoaded(shouldSave = isSavedUseCase.isSaved(item.listingId).not())
                SaveRemoveFlow.FROM_SAVED -> ActionSaveRemove.ItemStatusLoaded(shouldSave = false)
            }
            sendAction(action)
        }
    }

    override fun onReduceState(action: ActionSaveRemove) =
        when (action) {
            is ActionSaveRemove.ItemStatusLoaded -> StateSaveRemove.StatusLoadedState(shouldSave = action.shouldSave, shouldRemove = action.shouldSave.not())
            is ActionSaveRemove.ItemSaved -> StateSaveRemove.ItemSaved
            is ActionSaveRemove.ItemRemoved -> StateSaveRemove.ItemRemoved
        }

    override fun remove() {
        launchOn {
            removeItemUseCase.remove(item.listingId)
            sendAction(ActionSaveRemove.ItemRemoved)
        }
    }

    override fun save() {
        launchOn {
            saveItemUseCase.save(item.mapItem(AppGoodsItem::toDomain))
            sendAction(ActionSaveRemove.ItemSaved)
        }
    }
}

internal sealed class ActionSaveRemove : BaseAction {
    data class ItemStatusLoaded(val shouldSave: Boolean = false) : ActionSaveRemove()
    object ItemSaved : ActionSaveRemove()
    object ItemRemoved : ActionSaveRemove()
}

internal sealed class StateSaveRemove : BaseViewState {
    data class StatusLoadedState(val shouldSave: Boolean = false, val shouldRemove: Boolean = false) : StateSaveRemove()
    object ItemSaved : StateSaveRemove()
    object ItemRemoved : StateSaveRemove()
    object NoState : StateSaveRemove()
}




