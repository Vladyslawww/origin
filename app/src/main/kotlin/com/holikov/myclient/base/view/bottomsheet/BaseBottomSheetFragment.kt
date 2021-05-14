package com.holikov.myclient.base.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.holikov.base.base.view.fragment.StateHandler
import com.holikov.base.base.view.fragment.bottomsheet.InjectionBottomSheetFragment
import com.holikov.base.base.view.fragment.exceptions.CompositeExceptionDispatcher
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.base.base.viewmodel.ProgressEvent
import com.holikov.base.extensions.observeData
import com.holikov.data.network.exceptions.ExceptionHandler
import com.holikov.myclient.base.view.BaseActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import org.kodein.di.generic.instance
import timber.log.Timber

abstract class BaseBottomSheetFragment<in ST : BaseViewState, out VM : BaseViewModel> :
    InjectionBottomSheetFragment(),
    StateHandler<ST> {

    @get:LayoutRes
    protected abstract val layoutResourceId: Int
    abstract val viewModel: VM
    protected fun <T> Flow<T>.bind() = this.launchIn(lifecycleScope)
    protected fun <T> observe(liveData: LiveData<T>, body: (T) -> Unit) =
        viewLifecycleOwner.observeData(liveData, body)

    private val exceptionHandler by instance<ExceptionHandler>()
    private val compositeExceptionDispatcher = CompositeExceptionDispatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run(viewModel::passArgs)
        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(layoutResourceId, null).also {
            Timber.v("onCreateView ${javaClass.simpleName}")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeExceptionDispatcher.addDispatcher((activity as BaseActivity).exceptionDispatcher)
        subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("onDestroyView ${javaClass.simpleName}")
    }

    @Suppress("UNCHECKED_CAST")
    private fun subscribe() {
        observe(viewModel.errorsStream) {
            compositeExceptionDispatcher.dispatch(exceptionHandler.propagate(it))
        }

        observe(viewModel.progressChannel) {
            when (it) {
                ProgressEvent.Show -> showProgress()
                ProgressEvent.Hide -> hideProgress()
            }
        }

        observe(viewModel.liveState) { onStateChanged(it as ST) }
    }

    protected open fun getExceptionDispatcher() = compositeExceptionDispatcher
    protected open fun showProgress() {}
    protected open fun hideProgress() {}
}