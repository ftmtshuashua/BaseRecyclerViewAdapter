package com.acap.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acap.adapter.interior.AdapterObservable;
import com.acap.adapter.internal.ViewHolderOnClick;
import com.acap.adapter.internal.ViewHolderOnClickTransfer;
import com.acap.adapter.slide.SlideControl;
import com.acap.adapter.slide.SlideFrameLayout;


/**
 * <pre>
 * Tip:
 *      RecyclerView万能适配器
 *
 *
 * Function:
 *      isSearched()            :判断是否为搜索模式
 *
 * Created by A·Cap on 22018/5/9
 * @param <D> 数据源类型
 * </pre>
 */
public abstract class BaseRecyclerViewAdapter<D> extends AdapterObservable<D> {
    private RecyclerView mRecyclerView;
    // Item 的点击监听
    private ViewHolderOnClick mViewHolderOnClick;
    // 滚动监听
    private final SlideControl mSlideControl = new SlideControl();


    private ViewHolderOnClick getViewHolderOnClick() {
        if (mViewHolderOnClick == null) mViewHolderOnClick = new ViewHolderOnClick();
        return mViewHolderOnClick;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(mSlideControl);
        mRecyclerView = null;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(mSlideControl);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<D> holder, int position) {
        View contentView = holder.getContentView();
        View itemView = holder.itemView;


        ViewHolderOnClickTransfer mOnClick = null;
        // 点击和长按监听器
        if (contentView != null) {
            if (getOnItemClickListener() != null || getOnItemLongClickListener() != null) {
                mOnClick = getViewHolderOnClick().generate(this, holder);
                if (getOnItemClickListener() != null) mOnClick.bindOnClickListener(contentView);
                if (getOnItemLongClickListener() != null) mOnClick.bindOnLongClickListener(contentView);
            }
        }

        // 侧滑菜单自动收回
        if (itemView instanceof SlideFrameLayout) {
            mSlideControl.bindSlideFrameLayout(position , (SlideFrameLayout) itemView, mOnClick);
        }

        holder.setUpdateData(this, getDataItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<D> holder) {
        super.onViewRecycled(holder);
        if (mViewHolderOnClick != null) {
            mViewHolderOnClick.onViewRecycled(holder);
        }
        if (holder.itemView instanceof SlideFrameLayout) {
            mSlideControl.unbindSlideFrameLayout((SlideFrameLayout) holder.itemView);
        }


        holder.onRecycled();
    }


}
