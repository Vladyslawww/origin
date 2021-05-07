package com.holikov.myclient.saved

import android.os.Bundle
import android.view.View
import com.holikov.myclient.R
import com.holikov.myclient.base.view.BaseFragment
import org.kodein.di.generic.instance

internal class SavedFragment : BaseFragment<StateSaved, SavedViewModel, SavedNavigator>() {

    override val fragmentModule = SavedModule()
    override val navigator by instance<SavedNavigator>()
    override val viewModel by instance<SavedViewModel>()
    override val layoutResourceId = R.layout.saved_fragment
    private val delegate = SavedDelegate().also(::addLifecycleListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(delegate.details, navigator::details)
        observe(delegate.deleteRequest, navigator::bottomSheet)
    }

    override fun onStateChanged(state: StateSaved) {
        delegate.onLoaded(state.items)
    }

}
}