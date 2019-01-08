package support.lfp.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import support.lfp.adapter.interior.AdapterObservable;


/**
 * <pre>
 * Tip:
 *      RecyclerView万能适配器
 *
 * Function:
 *      isSearched()            :判断是否为搜索模式
 *
 * Created by LiFuPing on 22018/5/9
 * </pre>
 */
public abstract class BaseRecyclerViewAdapter<D> extends AdapterObservable<D> {

    private ObjectCacheUtils<Object, ViewHolderOnClickTransfer> mObjectCacheUtils;


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<D> holder, int position) {
        if (getOnItemClickListener() != null || getOnItemLongClickListener() != null) {
            if (BaseRecyclerViewConfig.IsEnableItemViewPackage && holder != null && holder.itemView != null && holder.itemView instanceof ViewGroup) {
                View contentView = ((ViewGroup) holder.itemView).getChildAt(0);
                ViewHolderOnClickTransfer mViewHolderOnClickTransfer = generateViewHolderOnClickTransfer(this, holder);
                if (getOnItemClickListener() != null) {
                    contentView.setOnClickListener(mViewHolderOnClickTransfer);
                }
                if (getOnItemLongClickListener() != null) {
                    contentView.setOnLongClickListener(mViewHolderOnClickTransfer);
                }
                holder.setViewHolderOnClickTransfer(mViewHolderOnClickTransfer);
            }
        }
        holder.setUpdataData(this, (D) getDataItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<D> holder) {
        super.onViewRecycled(holder);
        if (mObjectCacheUtils != null) {
            mObjectCacheUtils.recycle(holder.getViewHolderOnClickTransfer());
        }
        holder.onRecycled();
    }


    final ViewHolderOnClickTransfer generateViewHolderOnClickTransfer(BaseRecyclerViewAdapter adapter, BaseViewHolder holder) {
        //<editor-fold desc="对象生成">
        if (mObjectCacheUtils == null) {
            mObjectCacheUtils = new ObjectCacheUtils<Object, ViewHolderOnClickTransfer>() {
                @Override
                public ViewHolderOnClickTransfer create(Object[] r) {
                    return new ViewHolderOnClickTransfer();
                }
            };
        }
        //</editor-fold>
        ViewHolderOnClickTransfer mViewHolderOnClickTransfer = mObjectCacheUtils.obtain();
//        if (mViewHolderOnClickTransfer == null) Log.e("BaseRecyclerViewAdapter", "生成对象为空");
        mViewHolderOnClickTransfer.mAdapter = adapter;
        mViewHolderOnClickTransfer.mHolder = holder;
        return mViewHolderOnClickTransfer;
    }

}
