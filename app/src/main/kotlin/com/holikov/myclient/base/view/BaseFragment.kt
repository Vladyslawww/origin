package com.holikov.myclient.base.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.holikov.base.base.view.fragment.InjectionFragment
import com.holikov.base.base.view.fragment.StateHandler
import com.holikov.base.base.viewmodel.BaseViewState
import com.holikov.myclient.base.navigator.BaseNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import androidx.lifecycle.lifecycleScope
import com.holikov.base.base.view.delegate.FragmentLifeCycleListener
import com.holikov.base.base.view.fragment.exceptions.CompositeExceptionDispatcher
import com.holikov.base.extensions.observeData
import com.holikov.data.network.exceptions.ExceptionHandler
import org.kodein.di.generic.instance
import retrofit2.http.Body
import timber.log.Timber
import com.holikov.base.base.viewmodel.BaseViewModel
import com.holikov.base.base.viewmodel.ProgressEvent

abstract class BaseFragment<in ST : BaseViewState, out VM : BaseViewModel, out NR : BaseNavigator> : InjectionFragment(), StateHandler<ST> {

    @get:LayoutRes
    protected abstract val layoutResourceId: Int
    abstract val viewModel: VM
    abstract val navigator: NR
    protected fun <T> Flow<T>.bind() = this.launchIn(lifecycleScope)
    protected fun <T> observe(liveData: LiveData<T>, body: (T) -> Unit) = viewLifecycleOwner.observeData(liveData, body)

    private val exceptionHandler by instance<ExceptionHandler>()
    private val lifecycleListeners = HashSet<FragmentLifeCycleListener>()
    private val compositeExceptionDispatcher = CompositeExceptionDispatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run(viewModel::passArgs)
        viewModel.onCreate()
        Timber.v("onCreate ${javaClass.simpleName}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResourceId, null).also {
            Timber.v("onCreateView ${javaClass.simpleName}")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleListeners.forEach { it.postCreateView(this, view) }
        compositeExceptionDispatcher.addDispatcher((activity as BaseActivity).exceptionDispatcher)
        subscribe()
        navigator.attach(activity!!.supportFragmentManager)
    }

    override fun onStart() {
        super.onStart()
        Timber.v("onStart ${javaClass.simpleName}")
    }

    override fun onResume() {
        super.onResume()
        Timber.v("onResume ${javaClass.simpleName}")
    }

    override fun onStop() {
        super.onStop()
        Timber.v("onStop ${javaClass.simpleName}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleListeners.forEach { it.postDestroyView(this) }
        navigator.release()
        Timber.v("onDestroyView ${javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy ${javaClass.simpleName}")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lifecycleListeners.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    protected fun addLifecycleListener(lifecycleListener: FragmentLifeCycleListener) {
        lifecycleListeners.add(lifecycleListener)
    }

    protected fun removeLifecycleListener(lifecycleListener: FragmentLifeCycleListener) {
        lifecycleListeners.remove(lifecycleListener)
    }

    @Suppress("UNCHECKED_CAST")
    private fun subscribe() {

        observe((activity as BaseActivity).connectionLiveData) { connected ->
            if (connected && isResumed) viewModel.onConnectionEstablished()
        }

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
