package lfp.support.adapter.item

import android.content.Context
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
class ButtonViewModel(val mMsg: String, val mOnClick: () -> Unit) : MultipleViewModel(R.layout.layout_button) {
    override fun onUpdate(holder: BaseViewHolder<*>) {
        holder.setText(R.id.view_Button, mMsg)
    }

    override fun onClick(context: Context) {
        mOnClick()
    }
}
