package com.holikov.myclient.base.view.delegate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.holikov.base.base.view.delegate.FragmentLifeCycleListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import java.util.concurrent.atomic.AtomicReference

open class ViewDelegate: LayoutContainer, FragmentLifeCycleListener {

    private val viewRef = AtomicReference<View>()
    private val scope = AtomicReference<CoroutineScope>()

    override val containerView: View? get() = viewRef.get()

    override fun postCreateView(fragment: Fragment, view: View) {
        super.postCreateView(fragment, view)
        viewRef.set(view)
        scope.set(fragment.lifecycleScope)
    }

    override fun postDestroyView(fragment: Fragment) {
        super.postDestroyView(fragment)
        viewRef.set(null)
        scope.set(null)
        clearFindViewByIdCache()
    }

    protected fun context() = viewRef.get().context

    protected fun <T> Flow<T>.bind() = scope.get()?.run { launchIn(this) }

    internal fun <T: View> findById(id: Int) = viewRef.get()?.findViewById<T>(id)

}