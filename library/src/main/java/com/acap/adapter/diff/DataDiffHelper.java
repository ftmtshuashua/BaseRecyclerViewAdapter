package com.acap.adapter.diff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * <pre>
 * Tip:
 *      数据差分器 , 使数据源经过一系列变化得与新数据源相同的结构与顺序
 *
 * @author A·Cap
 * @date 2021/12/13 17:43
 * </pre>
 */
public class DataDiffHelper<T> {
    @NonNull
    private final OnDiffUpdateCallback mUpdate;
    private boolean mIsDiffEnable = false;
    private boolean mIsDiffAsync = false;

    //非Diff模式使用的数据源
    @NonNull
    private List<T> mList = new ArrayList<>();
    @NonNull
    private List<T> mReadOnlyList = Collections.unmodifiableList(mList);

    public DataDiffHelper(@NonNull OnDiffUpdateCallback update) {
        this.mUpdate = update;
    }

    public void setEnable(boolean enable) {
        mIsDiffEnable = enable;
    }

    public void setAsync(boolean async) {
        mIsDiffAsync = async;
    }

    //检测Diff状态
    private void diffCheck() {
        if (mDiffRunning) throw new IllegalStateException("diff 运行中,请等待运行完成");
    }

    public int size() {
        return mReadOnlyList.size();
    }

    public List<T> getList() {
        return mReadOnlyList;
    }

    public T remove(int index) {
        diffCheck();
        return mList.remove(index);
    }

    public List<T> remove(int index, int count) {
        diffCheck();
        List<T> removes = new ArrayList<>();
        for (int i = index + count - 1; i >= index; i--) {
            removes.add(mList.remove(i));
        }
        return removes;
    }

    public void add(int index, T data) {
        diffCheck();
        mList.add(index, data);
    }

    public void move(int fromIndex, int toIndex) {
        diffCheck();
        mList.add(toIndex, mList.remove(fromIndex));
    }

    public void clear() {
        diffCheck();
        mList = new ArrayList<>();
        mReadOnlyList = Collections.unmodifiableList(mList);
    }

    public <D extends T> boolean addAll(int index, List<D> data) {
        if (data == null) return false;
        diffCheck();
        return mList.addAll(index, data);

    }

    public T replace(int index, T data) {
        diffCheck();
        final T remove = mList.remove(index);
        mList.add(index, data);
        return remove;
    }

    public <D extends T> void set(List<D> data) {
        if (mIsDiffEnable) {
            differ(data);
        } else {
            int old_size = mList.size();       // 10
            int new_size = data.size();             // 20
            int remove_size = old_size - new_size;  // -10
            int add_size = new_size - old_size;     // 10
            int change_size = remove_size > 0 ? old_size : new_size; //10

            clear();
            addAll(0, data);

            if (change_size > 0) {
                mUpdate.onChanged(0, change_size, null);
            }
            if (remove_size > 0) {
                mUpdate.onRemoved(change_size, remove_size);
            }
            if (add_size > 0) {
                mUpdate.onInserted(change_size, add_size);
            }
        }
    }

    DiffUtil.ItemCallback<T> mDiffCallback;
    int mMaxScheduledGeneration;
    boolean mDiffRunning = false;  // Diff 运行中

    Executor mBackExecutor, mMainExecutor;

    //差分
    private <D extends T> void differ(List<D> data) {
        final int runGeneration = ++mMaxScheduledGeneration;
        if (mList == data) {
            return;
        }
        if (data == null) {
            int size = mList.size();
            clear();
            mUpdate.onRemoved(0, size);
            return;
        }
        if (mList.isEmpty()) {
            mList = new ArrayList<>(data);
            mReadOnlyList = Collections.unmodifiableList(mList);
            mUpdate.onInserted(0, data.size());
            return;
        }

        if (mDiffCallback == null) {
            mDiffCallback = new DefaultDiffCallback<>();
        }

        final List<T> oldList = mList;
        final List<T> newList = new ArrayList<>(data);
        mDiffRunning = true;
        if (mIsDiffAsync) {
            if (mBackExecutor == null) {
                mBackExecutor = DiffThreadExecutor.getDiffThreadExecutor();
            }
            if (mMainExecutor == null) {
                mMainExecutor = DiffThreadExecutor.getMainThreadExecutor();
            }

            mBackExecutor.execute(() -> {
                final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(oldList, newList, mDiffCallback));
                mMainExecutor.execute(() -> {
                    if (mMaxScheduledGeneration == runGeneration) {
                        latchList(newList, result);
                    }
                });
            });

        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(oldList, newList, mDiffCallback));
            if (mMaxScheduledGeneration == runGeneration) {
                latchList(newList, result);
            }
        }

    }

    //差分完成
    private void latchList(@NonNull List<T> newList, @NonNull DiffUtil.DiffResult diffResult) {
        mList = newList;
        mReadOnlyList = Collections.unmodifiableList(newList);
        mDiffRunning = false;
        diffResult.dispatchUpdatesTo(mUpdate);
    }

    private static final class DiffCallback<T> extends DiffUtil.Callback {
        List<T> oldList;
        List<T> newList;
        DiffUtil.ItemCallback<T> mDiffCallback;

        public DiffCallback(List<T> oldList, List<T> newList, DiffUtil.ItemCallback<T> mDiffCallback) {
            this.oldList = oldList;
            this.newList = newList;
            this.mDiffCallback = mDiffCallback;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldList.get(oldItemPosition);
            T newItem = newList.get(newItemPosition);
            if (oldItem != null && newItem != null) {
                return mDiffCallback.areItemsTheSame(oldItem, newItem);
            }
            // If both items are null we consider them the same.
            return oldItem == null && newItem == null;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldList.get(oldItemPosition);
            T newItem = newList.get(newItemPosition);
            if (oldItem != null && newItem != null) {
                return mDiffCallback.areContentsTheSame(oldItem, newItem);
            }
            if (oldItem == null && newItem == null) {
                return true;
            }
            // There is an implementation bug if we reach this point. Per the docs, this
            // method should only be invoked when areItemsTheSame returns true. That
            // only occurs when both items are non-null or both are null and both of
            // those cases are handled above.
            throw new AssertionError();
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            T oldItem = oldList.get(oldItemPosition);
            T newItem = newList.get(newItemPosition);
            if (oldItem != null && newItem != null) {
                return mDiffCallback.getChangePayload(oldItem, newItem);
            }
            // There is an implementation bug if we reach this point. Per the docs, this
            // method should only be invoked when areItemsTheSame returns true AND
            // areContentsTheSame returns false. That only occurs when both items are
            // non-null which is the only case handled above.
            throw new AssertionError();
        }
    }
}
