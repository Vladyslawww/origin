package com.holikov.myclient.search

import android.os.Bundle
import android.view.View
import com.holikov.myclient.R
import com.holikov.myclient.base.extentions.hide
import com.holikov.myclient.base.extentions.hideKeyboard
import com.holikov.myclient.base.extentions.queryChanges
import com.holikov.myclient.base.extentions.show
import com.holikov.myclient.base.view.BaseFragment
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.flow.onEach
import org.kodein.di.generic.instance

internal class SearchFragment : BaseFragment<StateSearch, SearchViewModel, SearchNavigator>() {

    override val fragmentModule = SearchModule()
    override val navigator by instance<SearchNavigator>()
    override val viewModel by instance<SearchViewModel>()
    override val layoutResourceId = R.layout.search_fragment
    private val delegate = SearchDelegate().also(::addLifecycleListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView.queryChanges().onEach { viewModel.query(it.toString()) }.bind()
        observe(delegate.nextPage) { viewModel.loadNext() }
        observe(delegate.refresh) { viewModel.refresh() }
        observe(delegate.details, navigator::details)
        observe(delegate.saveRequest, navigator::bottomSheet)
    }

    override fun onStateChanged(state: StateSearch) {
        hideKeyboard(searchView)
        delegate.onLoaded(state.items)
    }

    override fun showProgress() = swipe.show()
    override fun hideProgress() = swipe.hide()
}