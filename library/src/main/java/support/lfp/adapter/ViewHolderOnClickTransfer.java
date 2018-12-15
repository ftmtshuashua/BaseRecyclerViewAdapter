package support.lfp.adapter;

import android.view.View;

/**
 * <pre>
 * Tip:
 *      触摸事件分发
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/15 17:43
 * </pre>
 */
public class ViewHolderOnClickTransfer implements View.OnClickListener, View.OnLongClickListener {
    BaseRecyclerViewAdapter mAdapter;
    BaseViewHolder mHolder;
    int position;

    public ViewHolderOnClickTransfer() {
    }

    @Override
    public void onClick(View v) {
        if (mAdapter.getOnItemClickListener() != null) {
            mAdapter.getOnItemClickListener().onItemClick(mAdapter, mHolder, v, position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mAdapter.getOnItemLongClickListener() != null) {
            return mAdapter.getOnItemLongClickListener().onItemLongClick(mAdapter, mHolder, v, position);
        }
        return false;
    }
}