package com.holikov.base.base.viewmodel

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holikov.base.base.livedata.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

interface BaseViewModel {

    val liveState: LiveData<out BaseViewState>
    val errorsStream: LiveData<Throwable>
    val progressChannel: LiveData<ProgressEvent>
    fun onConnectionEstablished()
    fun loadData()
    fun loadNext()
    fun onCreate()
    fun passArgs(args: Bundle)
}

abstract class BaseStatelessViewModel : ViewModel(), BaseViewModel {

    protected open val handler = CoroutineExceptionHandler { _, exception ->
        errorsStream.postValue(exception)
    }

    protected var args = AtomicReference<Bundle>()

    override val progressChannel = SingleLiveEvent<ProgressEvent>()
    override val errorsStream = SingleLiveEvent<Throwable>()

    final override fun passArgs(args: Bundle) = this.args.set(args)
    final override fun loadData() = onLoadData()
    final override fun loadNext() = onLoadNext()
    override fun onCreate() {}
    override fun onConnectionEstablished() {}
    protected abstract fun onLoadData()
    protected open fun onLoadNext() {}

    protected fun <T> Flow<T>.bind() = this.launchIn(viewModelScope)

    protected fun launchOn(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(handler, block = block)
    }

    protected fun launchWithProgressOn(
        onStart: () -> Unit = { showProgress() },
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        onStart.invoke()
        return viewModelScope.launch(handler, block = block).hideProgress()
    }

    protected fun launchWithProgresable(withProgress: Boolean, block: suspend () -> Unit): Job {
        return if (withProgress) launchWithProgressOn { block.invoke() }
        else launchOn { block.invoke() }
    }

    protected open suspend fun <T> withProgressSuspend(function: suspend () -> T) {
        showProgress()
        try {
            function
        } finally {
            hideProgress()
        }
    }

    protected open fun showProgress() = progressChannel.postValue(ProgressEvent.Show)
    protected open fun hideProgress() = progressChannel.postValue(ProgressEvent.Hide)

    @CallSuper
    override fun onCleared() {
        hideProgress()
        Timber.v("onCleared ${javaClass.simpleName}")
        super.onCleared()
    }

    private fun Job.hideProgress(): Job {
        return onComplete { this@BaseStatelessViewModel.hideProgress() }
    }

    private fun Job.onComplete(completionHandler: CompletionHandler): Job {
        return this@onComplete.also {
            invokeOnCompletion { completionHandler.invoke(it) }
        }
    }
}

