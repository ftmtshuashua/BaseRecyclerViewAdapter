package com.acap.adapter.slide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acap.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Tip:
 *      当 RecyclerView 滚动时,关闭 SlideFrame 中的菜单
 *
 * @author A·Cap
 * @date 2021/12/8 15:32
 * </pre>
 */
public class SlideControl extends RecyclerView.OnScrollListener implements SlideCloseHelper {
    //联动的侧滑布局
    private final List<SlideFrameLayout> mSlideFrameLayout = new ArrayList<>();
    //侧滑菜单列表
    private final SlideMenuProvider mMenus = new SlideMenuProvider();

    /**
     * 是否启用侧滑菜单
     *
     * @return
     */
    public boolean isEnable() {
        return !mMenus.isEmpty();
    }

    /**
     * 绑定侧滑布局控制器
     *
     * @param vh
     */
    public void bindSlideFrameLayout(@NonNull BaseViewHolder<?> vh) {
        SlideFrameLayout itemView = (SlideFrameLayout) vh.itemView;
        mSlideFrameLayout.add(itemView);
        itemView.setSlideCloseHelper(this);

        itemView.setSlideMenu(mMenus, vh, vh.getSlideMenuIds());
    }

    /**
     * 解除控制器绑定
     *
     * @param vh
     */
    public void unbindSlideFrameLayout(@NonNull BaseViewHolder<?> vh) {
        SlideFrameLayout itemView = (SlideFrameLayout) vh.itemView;
        mSlideFrameLayout.remove(itemView);
        itemView.setSlideCloseHelper(null);
    }

    //当RecyclerView滚动时关闭所有菜单
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (newState == 1) { //滚动中  0:停止  1:滚动 2:甩动
            onCloseOther(null);
        }
    }

    //---------------------------------------- 菜单适配器 ----------------------------------------

    public void addSlideMenu(SlideMenu menu) {
        mMenus.add(menu);
    }

    @Override
    public void onCloseOther(SlideFrameLayout exclude) {
        for (int i = 0; i < mSlideFrameLayout.size(); i++) {
            SlideFrameLayout slideFrameLayout = mSlideFrameLayout.get(i);
            if (slideFrameLayout != exclude) {
                slideFrameLayout.closeMenu();
            }
        }
    }
}
