package com.acap.adapter.diff;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Tip:
 *      数据变化更新
 *
 * @author A·Cap
 * @date 2021/12/14 10:55
 * </pre>
 */
public class OnDiffUpdateCallback implements ListUpdateCallback {
    private final RecyclerView.Adapter<?> mAdapter;

    public OnDiffUpdateCallback(RecyclerView.Adapter<?> adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        mAdapter.notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        mAdapter.notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, @Nullable Object payload) {
        mAdapter.notifyItemRangeChanged(position, count, payload);
    }
}
