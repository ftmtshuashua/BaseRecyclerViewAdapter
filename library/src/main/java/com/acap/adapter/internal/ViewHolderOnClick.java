package com.acap.adapter.internal;

import androidx.annotation.NonNull;

import com.acap.adapter.BaseRecyclerViewAdapter;
import com.acap.adapter.BaseViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Tip:
 *      ViewHolder的点击适配
 *
 * @author A·Cap
 * @date 2021/12/8 15:53
 * </pre>
 */
public class ViewHolderOnClick {
    private final Map<BaseViewHolder<?>, ViewHolderOnClickTransfer> mCacheTransfer = new HashMap<>();
    private final ObjectCacheUtils<Object, ViewHolderOnClickTransfer> mObjectCacheUtils = new ObjectCacheUtils<Object, ViewHolderOnClickTransfer>() {
        @Override
        public ViewHolderOnClickTransfer create(Object[] r) {
            return new ViewHolderOnClickTransfer();
        }
    };

    /**
     * 生成监听器
     */
    public ViewHolderOnClickTransfer generate(BaseRecyclerViewAdapter<?> adapter, BaseViewHolder<?> holder) {
        ViewHolderOnClickTransfer transfer = getObjectCache().obtain();
        transfer.mAdapter = adapter;
        transfer.mHolder = holder;

        mCacheTransfer.put(holder, transfer);
        return transfer;
    }

    private ObjectCacheUtils<Object, ViewHolderOnClickTransfer> getObjectCache() {
        return mObjectCacheUtils;
    }


    public void onViewRecycled(@NonNull BaseViewHolder<?> holder) {
        if (mObjectCacheUtils != null) {
            mObjectCacheUtils.recycle(mCacheTransfer.remove(holder));
        }
    }
}
