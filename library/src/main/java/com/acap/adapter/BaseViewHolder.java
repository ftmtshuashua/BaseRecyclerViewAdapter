package com.acap.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

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

    @NonNull
    private final View contentView;

    //匹配侧滑菜单
    private int[] slideMenuIds;


    public BaseViewHolder(@NonNull View itemView) {
        super(wrapItemView(itemView));
        contentView = itemView;
    }

    /*在ItemView的外面包裹一层，便于点击事件设置监听*/
    @SuppressLint("InflateParams")
    private static final View wrapItemView(@NonNull View itemView) {
        ViewGroup inflate = (ViewGroup) LayoutInflater.from(itemView.getContext()).inflate(R.layout.wrap_item_view, null, false);
        final ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        inflate.setLayoutParams(new ViewGroup.LayoutParams(layoutParams.width, layoutParams.height));
        inflate.addView(itemView);
        return inflate;
    }

    /**
     * 当ViewHolder被回收的时候调用
     */
    protected void onRecycled() {

    }

    /**
     * 获得用户设置的ContentView,如果启用了点击模式ViewHolder会在itemView外面包裹一层，用来实现ItemOnClick事件
     */
    @NonNull
    public View getContentView() {
        return contentView;
    }


    /**
     * 设置 ViewHolder 绑定的菜单列表
     *
     * @param ids 菜单id列表
     */
    public void setSlideMenuIds(int... ids) {
        this.slideMenuIds = ids;
    }

    /**
     * 获得当前 ViewHolder 绑定的菜单列表,默认返回空,如果
     *
     * @return
     */
    public int[] getSlideMenuIds() {
        return slideMenuIds;
    }

}