package support.lfp.adapter;

import android.view.View;
import android.view.ViewGroup;
import support.lfp.adapter.interior.ViewHolderCacheManager;
import support.lfp.adapter.widget.SideslipView;

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
    private View mV_ContentView;

    public BaseViewHolder(View itemView) {
        super(packageItemView(itemView));
        mV_ContentView = itemView.findViewById(ITEMVIEW_PACKAGE_VIEW_ID);
        if (mV_ContentView == null) mV_ContentView = itemView;

    }

    /*在ItemView的外面包裹一层，便于点击事件设置监听*/
    private static final View packageItemView(View itemView) {
        if (itemView != null && BaseRecyclerViewConfig.IsEnableItemViewPackage) {
            ViewGroup rootview = new SideslipView(itemView.getContext());
            final ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            rootview.setLayoutParams(new ViewGroup.LayoutParams(layoutParams.width, layoutParams.height));
            rootview.setId(ITEMVIEW_PACKAGE_VIEW_ID);
            rootview.addView(itemView);
            return rootview;
        }
        return itemView;
    }

    /**
     * 当ViewHolder被回收的时候调用
     */
    protected void onRecycled() {
    }

    /**
     * 获得ItemView
     */
    public View getItemView() {
        return itemView;
    }

    /**
     * 获得用户设置的ContentView,如果启用了点击模式ViewHolder会在itemView外面包裹一层，用来实现ItemOnClick事件
     */
    public View getContentView() {
        return mV_ContentView;
    }

}