package com.acap.adapter.interior;

import androidx.recyclerview.widget.RecyclerView;

import com.acap.adapter.BaseViewHolder;
import com.acap.adapter.diff.DataDiffHelper;
import com.acap.adapter.diff.OnDiffUpdateCallback;

import java.util.ArrayList;
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
public abstract class AdapterDataManager<D> extends RecyclerView.Adapter<BaseViewHolder<D>> implements IDataOrigin<D> {
    //Adapter更新
    private final OnDiffUpdateCallback mUpdate = new OnDiffUpdateCallback(this);

    /* 用于列表中显示数据 */
    private final DataDiffHelper<D> mDataDiff = new DataDiffHelper(mUpdate);


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
        mUpdate.onInserted(index, data.size());
        onOperationDataOrigin();
    }

    @Override
    public D remove(int index) {
        return remove(index, 1).get(0);
    }

    @Override
    public List<D> remove(int index, int count) {
        List<D> remove = mDataDiff.remove(index, count);
        mUpdate.onRemoved(index, count);
        onOperationDataOrigin();
        return remove;
    }

    @Override
    public void removeAll() {
        mUpdate.onRemoved(0, mDataDiff.size());
        mDataDiff.clear();
        onOperationDataOrigin();
    }

    @Override
    public void move(int fromIndex, int toIndex) {
        mDataDiff.move(fromIndex, toIndex);
        mUpdate.onMoved(fromIndex, toIndex);
        onOperationDataOrigin();
    }

    @Override
    public D replace(int index, D data) {
        D replace = mDataDiff.replace(index, data);
        mUpdate.onChanged(index, 1, null);
        onOperationDataOrigin();
        return replace;
    }
    //</editor-fold>

    //<editor-fold desc="只设置数据,需要手动的刷新: SetOnly() |  AddOnly()  |  InsertOnly()  || MoveOnly() || replaceOnly()">

    /**
     * 使用一条数据替换数据源
     *
     * @param data 替换数据
     */
    public void setOnly(D data) {
        setOnly(Arrays.asList(data));
    }

    /**
     * 使用一组集合数据替换数据源
     *
     * @param data 添加数据
     * @param <T>  数据集合中数据类型是一个<D>类型
     */
    public <T extends D> void setOnly(List<T> data) {
        if (getDataCount() > 0) removeAllOnly();
        addOnly(data);
    }

    /**
     * 在数据源的末尾添加一条数据
     *
     * @param data 添加的数据
     */
    public void addOnly(D data) {
        addOnly(Arrays.asList(data));
    }

    /**
     * 在数据源的末尾添加一组数据
     *
     * @param data 添加数据
     * @param <T>  数据集合中数据类型是一个<D>类型
     */
    public <T extends D> void addOnly(List<T> data) {
        insertOnly(getDataCount(), data);
    }

    /**
     * 在Index位置插入一条数据
     *
     * @param index 数据插入位置
     * @param data  插入的数据
     */
    public void insertOnly(int index, D data) {
        insertOnly(index, Arrays.asList(data));
    }

    /**
     * 在Index位置插入一组数据
     *
     * @param index 数据插入位置
     * @param data  插入的数据集合
     * @param <T>   数据集合中数据类型是一个<D>类型
     */
    public <T extends D> void insertOnly(int index, List<T> data) {
        if (data == null) return;
        mDataDiff.addAll(index, data);
        onOperationDataOrigin();
    }

    /**
     * 移除Index位置的数据
     *
     * @param index 被移除数据的位置
     * @return 被移除的数据
     */
    public D removeOnly(int index) {
        return removeOnly(index, 1).get(0);
    }

    /**
     * 移除从index开始的count条数据
     *
     * @param index 被移除数据的起点位置
     * @param count 被移除数据条数
     * @return 被移除的数据
     */
    public List<D> removeOnly(int index, int count) {
        List<D> removes = new ArrayList<>();
        for (int i = index + count - 1; i >= index; i--) {
            removes.add(mDataDiff.remove(i));
        }
        onOperationDataOrigin();
        return removes;
    }

    /**
     * 移除所有数据
     */
    public void removeAllOnly() {
        mDataDiff.clear();
        onOperationDataOrigin();
    }

    /**
     * 移动数据位置
     *
     * @param fromIndex 被移动数据的位置
     * @param toIndex   希望移动到哪里
     */
    public void moveOnly(int fromIndex, int toIndex) {
        D form = mDataDiff.remove(fromIndex);
        mDataDiff.add(toIndex, form);
        onOperationDataOrigin();
    }

    /**
     * 替换数据
     *
     * @param index 被替换数据位置
     * @param data  替换的数据
     * @return 被替换的数据
     */
    public D replaceOnly(int index, D data) {
        final D remove = mDataDiff.remove(index);
        mDataDiff.add(index, data);
        onOperationDataOrigin();
        return remove;
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
    protected void onOperationDataOrigin() {
    }


}
