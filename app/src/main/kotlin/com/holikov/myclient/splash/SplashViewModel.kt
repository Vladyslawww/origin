package com.holikov.myclient.splash

import androidx.lifecycle.LiveData
import com.holikov.base.base.livedata.SingleLiveEvent
import com.holikov.base.base.livedata.unit
import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.myclient.base.viewmodel.BaseViewModelImpl
import kotlinx.coroutines.delay

interface SplashViewModel : BaseViewModel {

    val toSignIn: LiveData<Unit>
    val toMain: LiveData<Unit>
}

internal class SplashViewModelImpl : SplashViewModel, BaseViewModelImpl<StateSplash, ActionSplash>(StateSplash()) {

    override val toSignIn = SingleLiveEvent<Unit>()
    override val toMain = SingleLiveEvent<Unit>()

    override fun onLoadData() {
        launchOn {
            delay(1000)
            sendAction(ActionSplash.DataLoadSuccess)
            toMain.unit()
        }
    }

    override fun onReduceState(action: ActionSplash): StateSplash =
        when (action) {
            is ActionSplash.DataLoadSuccess -> state.copy(isLoading = false)
            else -> throw IllegalStateException("not implemented yet")
        }
}

internal sealed class ActionSplash : BaseAction {
    object DataLoadSuccess : ActionSplash()
    object DataLoadFailure : ActionSplash()
}

internal data class StateSplash(
    val isLoading: Boolean = true
) : BaseViewState