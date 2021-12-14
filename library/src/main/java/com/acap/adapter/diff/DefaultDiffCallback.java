package com.acap.adapter.diff;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/14 10:57
 * </pre>
 */
public class DefaultDiffCallback<T> extends DiffUtil.ItemCallback<T> {


    @Override
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return areContentsTheSame(oldItem, newItem);
    }

    @SuppressLint("DiffUtilEquals")
    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.equals(newItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {
        return super.getChangePayload(oldItem, newItem);
    }
}
