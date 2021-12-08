package com.acap.adapter.internal;

import android.view.View;

import com.acap.adapter.BaseRecyclerViewAdapter;
import com.acap.adapter.BaseViewHolder;

/**
 * <pre>
 * Tip:
 *      触摸事件分发
 *
 * Function:
 *
 * Created by A·Cap on 2018/12/15 17:43
 * </pre>
 */
public final class ViewHolderOnClickTransfer implements View.OnClickListener, View.OnLongClickListener {
    BaseRecyclerViewAdapter<?> mAdapter;
    BaseViewHolder<?> mHolder;

    private OnClickTriggerListener mOnClickTriggerListener;

    public ViewHolderOnClickTransfer() {
    }

    public void setOnClickTriggerListener(OnClickTriggerListener listener) {
        mOnClickTriggerListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickTriggerListener != null) {
            mOnClickTriggerListener.onTrigger(OnClickTriggerListener.TYPE_CLICK, v);
        }
        if (mAdapter.getOnItemClickListener() != null) {
            mAdapter.getOnItemClickListener().onItemClick(mAdapter, mHolder, v, mHolder.getBindingAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnClickTriggerListener != null) {
            mOnClickTriggerListener.onTrigger(OnClickTriggerListener.TYPE_LONG_CLICK, v);
        }
        if (mAdapter.getOnItemLongClickListener() != null) {
            return mAdapter.getOnItemLongClickListener().onItemLongClick(mAdapter, mHolder, v, mHolder.getBindingAdapterPosition());
        }
        return false;
    }

    public void bindOnClickListener(View contentView) {
        contentView.setOnClickListener(this);

    }

    public void bindOnLongClickListener(View contentView) {
        contentView.setOnLongClickListener(this);
    }

    /**
     * 当点击被触发
     */
    public interface OnClickTriggerListener {
        int TYPE_CLICK = 1;
        int TYPE_LONG_CLICK = 2;

        /**
         * 当触发点击
         *
         * @param type 类型 onClick:1 onLongClick:2
         * @param view
         */
        void onTrigger(int type, View view);
    }

}