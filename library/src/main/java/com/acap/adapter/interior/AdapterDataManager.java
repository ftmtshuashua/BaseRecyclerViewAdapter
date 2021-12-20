package com.acap.adapter.interior;

import androidx.recyclerview.widget.RecyclerView;

import com.acap.adapter.BaseViewHolder;
import com.acap.adapter.diff.DataDiffHelper;
import com.acap.adapter.diff.OnDiffUpdateCallback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * Tip:
 *      数据支持
 *
 *      自动维护Adapter中的数据源，并提供另外一个独立的Search数据源
 *      Adapter会首先加载Search中的数据
 *
 *
 * Function:
 *
 * Created by A·Cap on 2018/12/10 09:58
 * </pre>
 */
public abstract class AdapterDataManager<D> extends RecyclerView.Adapter<BaseViewHolder<D>> implements IDataOrigin<D>, OnDataChangeListener {
    //Adapter更新
    private final OnDiffUpdateCallback mUpdate = new OnDiffUpdateCallback(this);

    /* 用于列表中显示数据 */
    private final DataDiffHelper<D> mDataDiff = new DataDiffHelper(mUpdate, this);


    /*数据储存，方便在ViewHolder中获取*/
    private HashMap<String, Object> mTags;

    /*数据储存，方便在ViewHolder中获取*/
    private Object mTag;

    public void setTag(Object o) {
        mTag = o;
    }

    public Object getTag() {
        return mTag;
    }

    public void putTag(String key, Object o) {
        if (mTags == null) mTags = new HashMap<>(1);
        mTags.put(key, o);
    }

    public Object getTag(String key) {
        return mTags == null ? null : mTags.get(key);
    }

    /**
     * 启用差分算法进行列表更新,相比于刷新整个列表具有更高的性能
     *
     * @param enable true:启用
     */
    public void setDiffEnable(boolean enable) {
        mDataDiff.setEnable(enable);
    }

    /**
     * 是否让差分算法异步运行,有助于提升性能
     *
     * @param async true:异步执行
     */
    public void setDiffAsync(boolean async) {
        mDataDiff.setAsync(async);
    }

    /*----------- 数据操作 -----------*/

    //<editor-fold desc="设置数据并执行更新动画: nSet() |  Add()  |  Insert()  || Move() || replace()">

    @Override
    public void set(D data) {
        set(Arrays.asList(data));
    }

    @Override
    public <T extends D> void set(List<T> data) {
        mDataDiff.set(data);
    }

    @Override
    public void add(D data) {
        add(Arrays.asList(data));
    }

    @Override
    public <T extends D> void add(List<T> data) {
        insert(getDataCount(), data);
    }

    @Override
    public void insert(int index, D data) {
        insert(index, Arrays.asList(data));
    }

    @Override
    public <T extends D> void insert(int index, List<T> data) {
        if (data == null) return;
        mDataDiff.addAll(index, data);
    }

    @Override
    public D remove(int index) {
        return remove(index, 1).get(0);
    }

    @Override
    public List<D> remove(int index, int count) {
        return mDataDiff.remove(index, count);
    }

    @Override
    public void removeAll() {
        mDataDiff.clear();
    }

    @Override
    public void move(int fromIndex, int toIndex) {
        mDataDiff.move(fromIndex, toIndex);
    }

    @Override
    public D replace(int index, D data) {
        return mDataDiff.replace(index, data);
    }
    //</editor-fold>

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return getDataCount();
    }

    /**
     * 获得数据源集合对象
     */
    public List<D> getData() {
        return mDataDiff.getList();
    }

    /**
     * 获得数据源中下标对应位置的数据
     *
     * @param index 下标
     */
    public D getDataItem(int index) {
        return getData().get(index);
    }

    /**
     * 获得数据源中数据的数量
     */
    public int getDataCount() {
        return mDataDiff.size();
    }

    /*当数据源被操作的时候被调用*/
    @Override
    public void onDataChange() {

    }
}
