package support.lfp.adapter;

import android.view.View;
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

    public BaseViewHolder(View itemView) {
        super(itemView);
    }
}