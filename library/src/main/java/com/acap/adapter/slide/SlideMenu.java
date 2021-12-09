package com.acap.adapter.slide;

import android.view.View;

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
    //菜单的布局ID
    private int mLayoutId;

    public SlideMenu(int layoutId) {
        this.mLayoutId = layoutId;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 是否绑定菜单绑定菜单
     *
     * @param position 需要绑定的位置
     * @return true:将菜单绑定到 position 所在位置的ViewHelper上
     */
    public boolean onBindMenu(int position) {
        return true;
    }

    /**
     * 当绑定View时
     */
    public abstract void onViewBind(View menu, int position);

}
