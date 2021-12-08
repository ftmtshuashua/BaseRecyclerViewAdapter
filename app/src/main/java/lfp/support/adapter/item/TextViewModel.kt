package lfp.support.adapter.item

import com.acap.adapter.BaseViewHolder
import com.acap.adapter.multiple.MultipleViewModel
import lfp.support.adapter.R


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/8 17:47
 * </pre>
 */
class TextViewModel(val mMsg: String) : MultipleViewModel(R.layout.layout_textview) {
    override fun onUpdate(holder: BaseViewHolder<*>) {
        holder.setText(R.id.view_Info, mMsg)
    }
}
