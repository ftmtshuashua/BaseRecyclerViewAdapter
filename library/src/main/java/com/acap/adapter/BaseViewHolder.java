package com.acap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acap.adapter.interior.ViewHolderCacheManager;
import com.cap.adapter.R;

/**
 * <pre>
 * Tip:
 *      BaseRecyclerViewAdapter的BaseViewHolder
 *
 * Function:
 *
 * Created by A·Cap on 2018/12/10 09:36
 * </pre>
 */
public abstract class BaseViewHolder<D> extends ViewHolderCacheManager<D> {

    private View mV_ContentView;

    public BaseViewHolder(View itemView) {
        super(wrapItemView(itemView));
        View viewById = itemView.findViewById(R.id.view_SlideFrameLayout);
        if (viewById == null) {
            mV_ContentView = itemView;
        } else {
            mV_ContentView = viewById;
        }
    }

    /*在ItemView的外面包裹一层，便于点击事件设置监听*/
    private static final View wrapItemView(View itemView) {
        if (itemView != null) {
            ViewGroup inflate = (ViewGroup) LayoutInflater.from(itemView.getContext()).inflate(R.layout.wrap_item_view, null, false);
            final ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            inflate.setLayoutParams(new ViewGroup.LayoutParams(layoutParams.width, layoutParams.height));
            inflate.addView(itemView);
            return inflate;
        }
        return null;
    }

    /**
     * 当ViewHolder被回收的时候调用
     */
    protected void onRecycled() {
    }

    /**
     * 获得ItemView
     */
    public View getItemView() {
        return itemView;
    }

    /**
     * 获得用户设置的ContentView,如果启用了点击模式ViewHolder会在itemView外面包裹一层，用来实现ItemOnClick事件
     */
    public View getContentView() {
        return mV_ContentView;
    }

}