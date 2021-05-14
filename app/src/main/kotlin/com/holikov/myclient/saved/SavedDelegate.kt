package com.holikov.myclient.saved

import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import com.github.nitrico.lastadapter.LastAdapter
import com.holikov.base.base.list.AsyncDiffObservableList
import com.holikov.base.base.livedata.SingleLiveEvent
import com.holikov.myclient.BR
import com.holikov.myclient.R
import com.holikov.myclient.base.extentions.AppSkeletonItem
import com.holikov.myclient.base.extentions.clear
import com.holikov.myclient.base.extentions.set
import com.holikov.myclient.base.extentions.skeleton
import com.holikov.myclient.base.view.delegate.ViewDelegate
import com.holikov.myclient.databinding.ItemAppSearchResultBinding
import com.holikov.myclient.search.model.AppGoodsItem
import kotlinx.android.synthetic.main.saved_fragment.*

class SavedDelegate : ViewDelegate() {

    private val data = AsyncDiffObservableList<Parcelable>().skeleton()
    private val adapter = LastAdapter(data, BR.item).setup()
    val details = SingleLiveEvent<Long>()
    val deleteRequest = SingleLiveEvent<AppGoodsItem>()

    override fun postCreateView(fragment: Fragment, view: View) {
        super.postCreateView(fragment, view)
        savedItems.set(adapter)
    }

    override fun preDestroyView(fragment: Fragment, view: View) {
        super.preDestroyView(fragment, view)
        savedItems.clear()
    }

    fun onLoaded(items: List<Parcelable>) {
        data.update(items)
    }

    private fun LastAdapter.setup() = apply {
        map<AppSkeletonItem>(R.layout.item_app_skeleton)
        map<AppGoodsItem, ItemAppSearchResultBinding>(R.layout.item_app_search_result) {
            onClick { it.binding.item?.listingId?.run(details::postValue) }
            onLongClick { it.binding.item?.run(deleteRequest::postValue) }
        }
    }

    companion object {
        const val ENTITY_ID = "entity_id"
        const val REQUEST_CODE = 2222
    }
}