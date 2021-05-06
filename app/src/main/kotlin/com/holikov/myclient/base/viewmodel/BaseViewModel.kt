package com.holikov.myclient.base.viewmodel

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseStatelessViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.myclient.base.extentions.parcelable
import kotlin.properties.Delegates

abstract class BaseViewModelImpl<ViewState : BaseViewState, ViewAction : BaseAction>(initialState: ViewState) :
    BaseStatelessViewModel() {

    override val liveState = MutableLiveData<ViewState>()

    protected var state by Delegates.observable(initialState) { _, _, new ->
        liveState.value = new
    }

    fun sendAction(viewAction: ViewAction) {
        state = onReduceState(viewAction)
    }

    protected abstract fun onReduceState(action: ViewAction): ViewState

    protected inline fun <reified T : Parcelable> getArgs() =
        requireNotNull(args.getAndSet(null)) { "Needed arguments not passed to VM" }.parcelable<T>()

}
