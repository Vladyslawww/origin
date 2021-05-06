package com.holikov.myclient.details


import android.view.View
import androidx.fragment.app.Fragment
import com.holikov.base.base.livedata.SingleLiveEvent
import com.holikov.base.base.livedata.unit
import com.holikov.myclient.base.extentions.cover
import com.holikov.myclient.R
import com.holikov.myclient.base.view.delegate.ViewDelegate
import com.holikov.myclient.search.model.AppGoodsItem
import com.holikov.myclient.utils.HTML
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.item_details_content.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.util.concurrent.atomic.AtomicReference

class DetailsDelegate : ViewDelegate() {

    val save = SingleLiveEvent<Unit>()
    val remove = SingleLiveEvent<Unit>()

    private val shouldSave = AtomicReference<Boolean>()

    override fun postCreateView(fragment: Fragment, view: View) {
        super.postCreateView(fragment, view)
        actionBtn.clicks().performAction().bind()
    }

    fun onLoaded(item: AppGoodsItem, shouldSave: Boolean) {
        detailsImage.cover(item.image)
        title.text = HTML.fromHtml(item.title)
        description.text = HTML.fromHtml(item.description)
        price.text = item.price
        this.shouldSave.set(shouldSave)
        actionBtn.setImageResource(if (shouldSave) R.drawable.ic_saved else R.drawable.ic_action_delete)
    }

    private fun Flow<Unit>.performAction() =
        onEach { if (shouldSave.get()) save.unit() else remove.unit() }

}