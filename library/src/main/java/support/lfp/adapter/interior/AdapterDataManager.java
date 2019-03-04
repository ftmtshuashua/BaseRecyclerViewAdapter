package support.lfp.adapter.interior;

import androidx.recyclerview.widget.RecyclerView;
import support.lfp.adapter.BaseViewHolder;

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
 * Created by LiFuPing on 2018/12/10 09:58
 * </pre>
 */
public abstract class AdapterDataManager<D> extends RecyclerView.Adapter<BaseViewHolder<D>> implements IDataOrigin<D> {
    /**
     * 禁用所有Item数据动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR = 0x00000001;

    /* 用于列表中显示数据 */
    private final List<D> mDataArray = new ArrayList<>();
    private int mFlag;
    /*
     * 设置notify方法的偏移量，当数据源改变需要通知Adapter刷新UI，为刷新的UI的位置设置一个偏移量     *
     * 允许在某些情况比如顶部增加一个和数据源相对独立的数据，这个时候数据源中下标为0的数据刷新UI的时候需要调用notify(1)
     *
     */
    private int mNotifyItemOffSet = 0;


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
     * 设置刷新偏移量
     *
     * @param offset 偏移量
     */
    public void setNotifyItemOffSet(int offset) {
        this.mNotifyItemOffSet = offset;
    }

    public int getNotifyItemOffSet() {
        return mNotifyItemOffSet;
    }

    /**
     * 获得Adapter行为配置
     */
    public int getFlag() {
        return mFlag;
    }

    /**
     * 设置Item动画是否启用
     *
     * @param enable true:启用  false:禁用
     */
    public void setItemAnimationEnable(boolean enable) {
        if (enable) {
            mFlag &= ~FLAG_DISABLE_ITEM_ANIMATOR;
        } else {
            mFlag |= FLAG_DISABLE_ITEM_ANIMATOR;
        }
    }

    /**
     * 判断是需要启用Item动画
     *
     * @return true:启用  false:禁用
     */
    public boolean isEnableItemAnimation() {
        return (mFlag & FLAG_DISABLE_ITEM_ANIMATOR) == 0;
    }

    /*----------- 数据操作 -----------*/
    //<editor-fold desc="设置数据并执行更新动画: nSet() |  Add()  |  Insert()  || Move() || replace()">

    @Override
    public void set(D data) {
        set(Arrays.asList(data));
    }

    @Override
    public <T extends D> void set(List<T> data) {
        if (getDataCount() > 0) removeAll();
        add(data);
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
        getData().addAll(index, data);
        if (isEnableItemAnimation()) {
            final int start = mNotifyItemOffSet + index;
            final int size = data.size();
            notifyItemRangeInserted(start, size);
        }
        onOperationDataOrigin();
    }

    @Override
    public D remove(int index) {
        return remove(index, 1).get(0);
    }

    @Override
    public List<D> remove(int index, int count) {
        if (isEnableItemAnimation()) {
            final int start = mNotifyItemOffSet + index;
            final int size = count;
            notifyItemRangeRemoved(start, size);
        }
        List<D> removes = new ArrayList<>();
        for (int i = index + count - 1; i >= index; i--) {
            removes.add(getData().remove(i));
        }
        onOperationDataOrigin();
        return removes;
    }

    @Override
    public void removeAll() {
        if (isEnableItemAnimation()) {
            notifyItemRangeRemoved(mNotifyItemOffSet, getDataCount());
        }
        getData().clear();
        onOperationDataOrigin();
    }

    @Override
    public void move(int fromIndex, int toIndex) {
        D form = getData().remove(fromIndex);
        getData().add(toIndex, form);
        if (isEnableItemAnimation()) {
            notifyItemMoved(fromIndex + mNotifyItemOffSet, toIndex + mNotifyItemOffSet);
        }
        onOperationDataOrigin();
    }

    @Override
    public D replace(int index, D data) {
        final D remove = getData().remove(index);
        getData().add(index, data);
        if (isEnableItemAnimation()) {
            notifyItemChanged(index);
        }
        onOperationDataOrigin();
        return remove;
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
        getData().addAll(index, data);
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
            removes.add(getData().remove(i));
        }
        onOperationDataOrigin();
        return removes;
    }

    /**
     * 移除所有数据
     */
    public void removeAllOnly() {
        getData().clear();
        onOperationDataOrigin();
    }

    /**
     * 移动数据位置
     *
     * @param fromIndex 被移动数据的位置
     * @param toIndex   希望移动到哪里
     */
    public void moveOnly(int fromIndex, int toIndex) {
        D form = getData().remove(fromIndex);
        getData().add(toIndex, form);
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
        final D remove = getData().remove(index);
        getData().add(index, data);
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
        return mDataArray;
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
        return mDataArray == null ? 0 : mDataArray.size();
    }


    /*当数据源被操作的时候被调用*/
    protected void onOperationDataOrigin() {
    }
}
