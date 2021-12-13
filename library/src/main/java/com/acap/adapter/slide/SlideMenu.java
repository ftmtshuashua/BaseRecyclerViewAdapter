package com.acap.adapter.slide;

import android.view.View;

import androidx.annotation.NonNull;

import com.acap.adapter.BaseViewHolder;

/**
 * <pre>
 * Tip:
 *      侧滑菜单适配器
 *
 * @author A·Cap
 * @date 2021/12/9 13:14
 * </pre>
 */
public abstract class SlideMenu {
    private final int mId;
    private final int mLayoutId;
    private final Place mPlace;

    /**
     * 侧滑菜单配置
     *
     * @param menuId   菜单ID,ViewHolder通过菜单ID来确定使用哪一个菜单
     * @param place    菜单的位置 {@link Place#LEFT} or {@link Place#RIGHT}
     * @param layoutId 菜单的布局
     */
    public SlideMenu(int menuId, @NonNull Place place, int layoutId) {
        this.mId = menuId;
        this.mLayoutId = layoutId;
        this.mPlace = place;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    @NonNull
    public Place getPlace() {
        return mPlace;
    }

    public int getId() {
        return mId;
    }

    /**
     * 当绑定View时
     */
    public abstract void onViewBind(@NonNull View menu, @NonNull BaseViewHolder<?> vh);


    /**
     * 菜单所在位置
     */
    public enum Place {
        /**
         * 位于 Item 的左边
         */
        LEFT,
        /**
         * 位于 Item 的右边
         */
        RIGHT
    }

}
