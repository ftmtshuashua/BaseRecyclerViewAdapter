package support.lfp.adapter;

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
public abstract class BaseRecyclerViewAdapter<D > extends AdapterObservable<D> {


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<D> holder, int position) {
        holder.setUpdataData(this, (D) findItemData(position));
    }


}
