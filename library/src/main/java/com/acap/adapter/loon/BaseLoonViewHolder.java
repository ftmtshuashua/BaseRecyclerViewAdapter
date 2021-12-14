package com.acap.adapter.loon;

import android.view.View;

import com.acap.adapter.BaseViewHolder;

/**
 * <pre>
 * Tip:
 *      BaseRecyclerViewAdapter的BaseViewHolder,懒人版
 *      将事件代理给BaseLoonRecyclerViewAdapter处理
 *
 * Function:
 *
 * Created by A·Cap on 2018/12/10 09:36
 * </pre>
 */


public class BaseLoonViewHolder<D> extends BaseViewHolder<D> {

    OnViewHolderUpdate mOnViewHolderUpdate;

    public BaseLoonViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onUpdateUI(D data) {
        if (mOnViewHolderUpdate != null) {
            mOnViewHolderUpdate.convert(this, data);
        }
    }

    public void setOnViewHolderUpdate(OnViewHolderUpdate l) {
        mOnViewHolderUpdate = l;
    }

    @Override
    protected void onRecycled() {
        super.onRecycled();
        if (mOnViewHolderUpdate != null) {
            mOnViewHolderUpdate.onRecycled(this, getSaveData());
        }
    }

    public interface OnViewHolderUpdate<K extends BaseLoonViewHolder, T> {
        /**
         * @param holder 加载数据的ViewHolder
         * @param data   绑定的数据
         */
        void convert(K holder, T data);

        /**
         * 当 holder 被回收
         */
        void onRecycled(K holder, T data);
    }

}