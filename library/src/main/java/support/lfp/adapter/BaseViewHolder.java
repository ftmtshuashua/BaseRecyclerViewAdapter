package support.lfp.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import support.lfp.adapter.interior.ViewHolderCacheManager;

/**
 * <pre>
 * Tip:
 *      BaseRecyclerViewAdapter的BaseViewHolder
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/10 09:36
 * </pre>
 */


public abstract class BaseViewHolder<D> extends ViewHolderCacheManager<D> {

    public static final int ITEMVIEW_PACKAGE_VIEW_ID = 0xA78B0E23;
    private ViewHolderOnClickTransfer mViewHolderOnClickTransfer;

    public BaseViewHolder(View itemView) {
        super(packageItemView(itemView));
    }

    /*在ItemView的外面包裹一层，便于点击事件设置监听*/
    private static View packageItemView(View itemView) {
        if (itemView == null) return itemView;
        if (BaseRecyclerViewConfig.IsEnableItemViewPackage) {
            FrameLayout frameLayout = new FrameLayout(itemView.getContext());
            frameLayout.setBackgroundColor(Color.RED);
            frameLayout.setId(ITEMVIEW_PACKAGE_VIEW_ID);
            frameLayout.addView(itemView
                    ,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT )
            );
            return frameLayout;
        } else {
            return itemView;
        }
    }

    /**
     * 当ViewHolder被回收的时候调用
     */
    protected void onRecycled() {
        mViewHolderOnClickTransfer = null;
    }

    public void setViewHolderOnClickTransfer(ViewHolderOnClickTransfer l) {
        mViewHolderOnClickTransfer = l;
    }

    public ViewHolderOnClickTransfer getViewHolderOnClickTransfer() {
        return mViewHolderOnClickTransfer;
    }

    public View getItemView() {
        return itemView;
    }

    public View getContentView() {
        if (BaseRecyclerViewConfig.IsEnableItemViewPackage) {
            return itemView.findViewById(ITEMVIEW_PACKAGE_VIEW_ID);
        }
        return itemView;
    }

}