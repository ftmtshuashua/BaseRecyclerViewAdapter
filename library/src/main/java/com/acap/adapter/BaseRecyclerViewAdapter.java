package com.acap.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acap.adapter.interior.AdapterObservable;
import com.acap.adapter.internal.ViewHolderOnClick;
import com.acap.adapter.internal.ViewHolderOnClickTransfer;
import com.acap.adapter.slide.SlideControl;
import com.acap.adapter.slide.SlideFrameLayout;
import com.acap.adapter.slide.SlideMenu;


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

        // 点击和长按监听器
        OnItemClickListener<?> onClick = getOnItemClickListener();
        OnItemLongClickListener<?> onLongClick = getOnItemLongClickListener();
        if (onClick != null || onLongClick != null) {
            ViewHolderOnClickTransfer mOnClick = getViewHolderOnClick().generate(this, holder);
            if (onClick != null) mOnClick.bindOnClickListener(holder.getContentView());
            if (onLongClick != null) mOnClick.bindOnLongClickListener(holder.getContentView());
        }

        holder.setUpdateData(this, getDataItem(position));

        // 侧滑菜单
        if (mSlideControl.isEnable()) {
            mSlideControl.bindSlideFrameLayout(holder);
        }
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<D> holder) {
        super.onViewRecycled(holder);
        if (mViewHolderOnClick != null) {
            mViewHolderOnClick.onViewRecycled(holder);
        }
        if (holder.itemView instanceof SlideFrameLayout) {
            mSlideControl.unbindSlideFrameLayout(holder);
        }


        holder.onRecycled();
    }


    /**
     * 添加侧滑菜单
     *
     * @param menu
     */
    public void addSlideMenu(SlideMenu menu) {
        mSlideControl.addSlideMenu(menu);
    }


}
