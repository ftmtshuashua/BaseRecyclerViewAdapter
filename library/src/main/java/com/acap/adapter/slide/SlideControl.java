package com.acap.adapter.slide;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acap.adapter.internal.ViewHolderOnClickTransfer;

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
public class SlideControl extends RecyclerView.OnScrollListener implements ViewHolderOnClickTransfer.OnClickTriggerListener {
    private List<OnScrolling> mOnScrollingListener = new ArrayList<>();
    private List<SlideFrameLayout> mSlideFrameLayout = new ArrayList<>();

    private SlideFrameLayout.OnMenuStateChangeListener mOnMenuState = (layout, isOpen) -> {
//        Log.i("Scroll", MessageFormat.format("{0} -> {1}", layout, isOpen));
        if (isOpen) {
            for (int i = 0; i < mSlideFrameLayout.size(); i++) {
                SlideFrameLayout slideFrameLayout = mSlideFrameLayout.get(i);
                if (slideFrameLayout != layout) {
                    slideFrameLayout.closeMenu();
                }
            }
        }
    };

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == 1) { //滚动中  0:停止  1:滚动 2:甩动
            for (int i = 0; i < mOnScrollingListener.size(); i++) {
                mOnScrollingListener.get(i).onScrolling();
            }
        }
    }

    public void bindSlideFrameLayout(SlideFrameLayout itemView, ViewHolderOnClickTransfer mOnClick) {
        mOnScrollingListener.add(itemView);
        mSlideFrameLayout.add(itemView);
        itemView.setOnMenuStateChangeListener(mOnMenuState);

        mOnClick.setOnClickTriggerListener(this);
    }

    public void unbindSlideFrameLayout(SlideFrameLayout itemView) {
        mOnScrollingListener.remove(itemView);
        mSlideFrameLayout.remove(itemView);
        itemView.setOnMenuStateChangeListener(null);
    }

    @Override
    public void onTrigger(int type, View view) {
        mOnMenuState.onMenuStateChange(null, true);
    }


    /**
     * 当开始滚动时候
     */
    public interface OnScrolling {
        void onScrolling();
    }


}
