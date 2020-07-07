package support.lfp.adapter;

import android.view.View;

import java.util.HashMap;
import java.util.List;

import support.lfp.adapter.interior.AdapterObservable;

/**
 * 复杂布局Adapter
 *
 * @param <T>
 */
public class MultipleRecyclerViewAdapter<T extends MultipleViewModel> extends BaseLoonRecyclerViewAdapter<T, BaseLoonViewHolder<T>> {
    public MultipleRecyclerViewAdapter(int layout) {
        super(layout);

        setOnItemClickListener(new OnItemClickListener<T>() {
            @Override
            public void onItemClick(AdapterObservable<T> adapter, BaseViewHolder<T> viewHolder, View view, int position) {
                viewHolder.getSaveData().onClick(viewHolder.getContext());
            }
        });
    }

    /*储存Layout与ViewType对应关系*/
    private HashMap<Integer, Integer> layout2Type = new HashMap<Integer, Integer>();

    /*储存ViewType与Layout对应关系*/
    private HashMap<Integer, Integer> type2Layout = new HashMap<Integer, Integer>();

    public int findIndexByModel(T model) {
        List<T> data = getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == model) return i;
        }
        return -1;
    }

    public int getItemViewType(int position) {
        int layoutId = getDataItem(position).getLayoutId();
        Integer type = layout2Type.get(layoutId);
        if (type == null) {
            type = layout2Type.size();
            layout2Type.put(layoutId, type);
            type2Layout.put(type, layoutId);
        }
        return type;
    }

    public int getLayoutId(int viewType) {
        return type2Layout.get(viewType);
    }

    @Override
    public void convert(BaseLoonViewHolder<T> holder, T data) {
        data.onAttach(this);
        data.onUpdate(holder);
    }
}
