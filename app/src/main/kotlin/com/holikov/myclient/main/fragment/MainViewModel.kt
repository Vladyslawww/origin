package com.holikov.myclient.main.fragment

import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.myclient.base.viewmodel.BaseViewModelImpl

interface MainViewModel : BaseViewModel

internal class MainViewModelImpl : MainViewModel, BaseViewModelImpl<StateMain, ActionMain>(StateMain.NoState) {

    override fun onLoadData() {}

    override fun onReduceState(action: ActionMain) = StateMain.NoState
}

internal sealed class StateMain : BaseViewState {
    object NoState : StateMain()
}

internal sealed class ActionMain : BaseAction {
    object NoAction : ActionMain()
}