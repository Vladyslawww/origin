package com.holikov.myclient.saved

import android.os.Parcelable
import com.holikov.base.base.viewmodel.BaseAction
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState

interface SavedViewModel : BaseViewModel{}

internal data class StateSaved(val items: List<Parcelable> = emptyList()) : BaseViewState {
    val noState = items.isEmpty()
}

internal sealed class ActionSaved : BaseAction {
    data class UpdateLoaded(val items: List<Parcelable>) : ActionSaved()
}