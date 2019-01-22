package support.lfp.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import support.lfp.adapter.interior.AdapterObservable;

import java.util.HashMap;
import java.util.Map;


/**
 * <pre>
 * Tip:
 *      RecyclerView万能适配器
 *
 *
 * Function:
 *      isSearched()            :判断是否为搜索模式
 *
 * Created by LiFuPing on 22018/5/9
 * @param <D> 数据源类型
 * </pre>
 */
public abstract class BaseRecyclerViewAdapter<D> extends AdapterObservable<D> {

    private ObjectCacheUtils<Object, ViewHolderOnClickTransfer> mObjectCacheUtils;
    private Map<BaseViewHolder, ViewHolderOnClickTransfer> mCacheTransfer;

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<D> holder, int position) {
        if (getOnItemClickListener() != null || getOnItemLongClickListener() != null) {
            if (BaseRecyclerViewConfig.IsEnableItemViewPackage && holder != null && holder.itemView != null && holder.itemView instanceof ViewGroup) {
                View contentView = holder.getContentView();
                ViewHolderOnClickTransfer mOnClickTransfer = generateViewHolderOnClickTransfer(this, holder);
                if (getOnItemClickListener() != null) contentView.setOnClickListener(mOnClickTransfer);
                if (getOnItemLongClickListener() != null) contentView.setOnLongClickListener(mOnClickTransfer);

                mCacheTransfer.put(holder, mOnClickTransfer);
            }
        }
        holder.setUpdataData(this, getDataItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<D> holder) {
        super.onViewRecycled(holder);
        if (mObjectCacheUtils != null) {
            mObjectCacheUtils.recycle(mCacheTransfer.remove(holder));
        }
        holder.onRecycled();
    }


    final ViewHolderOnClickTransfer generateViewHolderOnClickTransfer(BaseRecyclerViewAdapter adapter, BaseViewHolder holder) {
        //<editor-fold desc="对象生成">
        if (mObjectCacheUtils == null) {
            mCacheTransfer = new HashMap<>();
            mObjectCacheUtils = new ObjectCacheUtils<Object, ViewHolderOnClickTransfer>() {
                @Override
                public ViewHolderOnClickTransfer create(Object[] r) {
                    return new ViewHolderOnClickTransfer();
                }
            };
        }
        //</editor-fold>
        ViewHolderOnClickTransfer mViewHolderOnClickTransfer = mObjectCacheUtils.obtain();
        mViewHolderOnClickTransfer.mAdapter = adapter;
        mViewHolderOnClickTransfer.mHolder = holder;
        return mViewHolderOnClickTransfer;
    }

}
