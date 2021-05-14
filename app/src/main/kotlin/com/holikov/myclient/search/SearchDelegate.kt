package com.holikov.myclient.search

import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import com.github.nitrico.lastadapter.LastAdapter
import com.holikov.base.base.list.AsyncDiffObservableList
import com.holikov.base.base.list.paging.PagingAttacher
import com.holikov.base.base.list.paging.clearPaging
import com.holikov.base.base.list.paging.paging
import com.holikov.base.base.livedata.SingleLiveEvent
import com.holikov.base.base.livedata.unit
import com.holikov.myclient.BR
import com.holikov.myclient.R
import com.holikov.myclient.base.extentions.*
import com.holikov.myclient.base.view.delegate.ViewDelegate
import com.holikov.myclient.databinding.ItemAppSearchResultBinding
import com.holikov.myclient.search.model.AppGoodsItem
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.swiperefreshlayout.refreshes

class SearchDelegate : ViewDelegate() {

    val nextPage = SingleLiveEvent<Unit>()
    val refresh = SingleLiveEvent<Unit>()
    val details = SingleLiveEvent<Long>()
    val saveRequest = SingleLiveEvent<AppGoodsItem>()
    private val data = AsyncDiffObservableList<Parcelable>().skeleton()
    private val adapter = LastAdapter(data, BR.item).setup()
    private lateinit var attacher: PagingAttacher

    override fun postCreateView(fragment: Fragment, view: View) {
        super.postCreateView(fragment, view)
        searchResults.set(adapter)
        attacher = searchResults.paging { nextPage.unit() }
        swipe.refreshes().onEach { refresh.unit() }.bind()
    }

    fun onLoaded(items: List<Parcelable>) {
        swipe.hide()
        data.update(items)
    }

    override fun preDestroyView(fragment: Fragment, view: View) {
        super.preDestroyView(fragment, view)
        searchResults.clear()
        searchResults.clearPaging(attacher)
    }
    
private fun LastAdapter.setup() = apply {
    map<AppSkeletonItem>(R.layout.item_app_skeleton)
    map<AppGoodsItem, ItemAppSearchResultBinding>(R.layout.item_app_search_result) {
        onClick { it.binding.item?.listingId?.run(details::postValue) }
        onLongClick { it.binding.item?.run(saveRequest::postValue) }
    }
}
}