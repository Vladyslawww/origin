package com.holikov.myclient.details

import android.os.Bundle
import android.view.View
import com.holikov.myclient.R
import com.holikov.myclient.base.extentions.bundleOf
import com.holikov.myclient.base.extentions.parcelable
import com.holikov.myclient.base.view.BaseFragment
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

internal class DetailsFragment : BaseFragment<StateDetail, DetailsViewModel, DetailsNavigator>() {

    override val layoutResourceId = R.layout.details_fragment
    override val viewModel by instance<DetailsViewModel>()
    override val navigator by instance<DetailsNavigator>()
    override val fragmentModule = DetailsModule()
    private val delegate = DetailsDelegate().also(::addLifecycleListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(delegate.remove) { viewModel.remove() }
        observe(delegate.save) { viewModel.save() }
    }

    override fun onStateChanged(state: StateDetail) {
        delegate.onLoaded(state.item, state.shouldSave)
    }

    companion object {
        private fun instance(args: DetailsArgs) = DetailsFragment().apply { arguments = bundleOf { parcelable(args) } }

        fun search(id: Long) = instance(DetailsArgs(id, DetailFlow.FROM_SEARCH))
        fun saved(id: Long) = instance(DetailsArgs(id, DetailFlow.FROM_SAVED))
    }

}