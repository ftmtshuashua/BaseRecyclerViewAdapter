package com.acap.adapter.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Tip:
 *      支持侧滑的 RecyclerView
 *
 * @author A·Cap
 * @date 2021/12/6 17:44
 * </pre>
 */
public class SlideRecyclerView extends RecyclerView {
    public SlideRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SlideRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
