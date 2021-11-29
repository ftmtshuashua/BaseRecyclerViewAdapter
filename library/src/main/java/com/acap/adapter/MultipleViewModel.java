package com.acap.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;


/**
 * 复炸布局的ViewModel
 */
public abstract class MultipleViewModel {
    RecyclerView.Adapter mAdapter;

    public void onAttach(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    public RecyclerView.Adapter getAdapter(){
        return mAdapter;
    }
    /**
     * 给定Model在View中的的布局文件
     */
    int layoutId;

    public MultipleViewModel(int layoutId) {
        this.layoutId = layoutId;
    }

    /**
     * 获得布局文件ID
     */
    public int getLayoutId() {
        return layoutId;
    }

    /**
     * Adapter回调更新,将Model中的数据更新到holder中
     *
     * @param holder
     */
    public abstract void onUpdate(BaseViewHolder holder);

    /**
     * 当Model对应的UI被点击的时候，响应点击事件到Model中
     *
     * 当Adapter的setOnItemClickListener方法被调用时候，这个方法将不再回调
     */
    public void onClick(Context context) {

    }

}
