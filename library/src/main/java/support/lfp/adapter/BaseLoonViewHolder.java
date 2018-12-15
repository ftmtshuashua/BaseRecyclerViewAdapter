package support.lfp.adapter;

import android.view.View;

/**
 * <pre>
 * Tip:
 *      BaseRecyclerViewAdapter的BaseViewHolder,懒人版
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/10 09:36
 * </pre>
 */


public class BaseLoonViewHolder<D> extends BaseViewHolder<D> {

    OnViewHolderUpdata mOnViewHolderUpdata;

    public BaseLoonViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onUpdateUI(D data) {
        if (mOnViewHolderUpdata != null) mOnViewHolderUpdata.convert(this, data);
    }

    public void setOnViewHolderUpdata(OnViewHolderUpdata l) {
        mOnViewHolderUpdata = l;
    }


    public interface OnViewHolderUpdata<K extends BaseLoonViewHolder, T> {
        void convert(K holper, T data);
    }

}