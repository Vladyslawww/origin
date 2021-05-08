package com.holikov.myclient.main.bottomsheet

import android.os.Bundle
import android.view.View
import com.holikov.myclient.R
import com.holikov.myclient.base.extentions.*
import com.holikov.myclient.base.view.bottomsheet.BaseBottomSheetFragment
import com.holikov.myclient.search.model.AppGoodsItem
import kotlinx.android.synthetic.main.modal_bottom_sheet.*
import kotlinx.coroutines.flow.onEach
import org.kodein.di.generic.instance
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges
import com.holikov.myclient.main.bottomsheet.StateSaveRemove.*

internal class SaveRemoveBottomSheet : BaseBottomSheetFragment<StateSaveRemove, SaveRemoveViewModel>() {

    override val fragmentModule = SaveRemoveModule()
    override val layoutResourceId = R.layout.modal_bottom_sheet
    override val viewModel by instance<SaveRemoveViewModel>()
    private var action: (() -> Unit)? = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionItem.clicks().onEach { action?.invoke() }.bind()
        actionText.textChanges()
    }

    override fun onStateChanged(state: StateSaveRemove) {
        when (state) {
            is StatusLoadedState -> state.bindUI().also { actionItem.visible(); skeleton.gone() }
            is ItemSaved -> saved()
            is ItemRemoved -> removed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        action = null
    }

    private fun StatusLoadedState.bindUI() {
        when {
            shouldRemove -> bindRemove()
            shouldSave -> bindSave()
        }
    }

    private fun bindSave() {
        actionIcon.setImageResource(R.drawable.ic_saved)
        actionText.text = getString(R.string.save_item)
        action = { viewModel.save() }
    }

    private fun bindRemove() {
        actionIcon.setImageResource(R.drawable.ic_action_delete)
        actionText.text = getString(R.string.remove_item)
        action = { viewModel.remove() }
    }

    private fun removed() {
        activity?.toast(R.string.successfully_removed)
        dismiss()
    }

    private fun saved() {
        activity?.toast(R.string.successfully_saved)
        dismiss()
    }

    companion object {

        private fun instance(args: SaveRemoveArgs) = SaveRemoveBottomSheet().apply { arguments = bundleOf { parcelable(args) } }

        fun search(item: AppGoodsItem) = instance(SaveRemoveArgs(item, SaveRemoveFlow.FROM_SEARCH))
        fun saved(item: AppGoodsItem) = instance(SaveRemoveArgs(item, SaveRemoveFlow.FROM_SAVED))

    }

}