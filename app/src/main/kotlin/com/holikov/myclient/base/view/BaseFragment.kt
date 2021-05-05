package com.holikov.myclient.base.view

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





}