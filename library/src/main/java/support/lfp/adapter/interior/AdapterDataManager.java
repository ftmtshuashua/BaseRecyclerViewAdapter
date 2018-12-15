package support.lfp.adapter.interior;

import androidx.recyclerview.widget.RecyclerView;
import support.lfp.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
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
public abstract class AdapterDataManager<D> extends RecyclerView.Adapter<BaseViewHolder<D>> {
    /**
     * 禁用所有Item数据动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR = 0x00000001;

    /*头部数据源*/
    private List mHeadArray;
    /*脚步数据源*/
    private List mFootArray;


//    private Map<Integer , >

    /* 用于列表中显示数据 */
    private final List<D> mDataArray = new ArrayList<>();

    private int mFlag;

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

    /**
     * 获得源数据
     *
     * @return List
     */
    public List<D> getData() {
        return mDataArray;
    }

    /*----------- 数据操作 -----------*/
    //<editor-fold desc="Set() |  Add()  |  Insert()  || Move()">
    public void set(D data) {
        set(Arrays.asList(data));
    }

    public <T extends D> void set(List<T> data) {
        if (getDataCount() > 0) removeAll();
        add(data);
    }

    public void add(D data) {
        add(Arrays.asList(data));
    }

    public <T extends D> void add(List<T> data) {
        insert(getDataCount(), data);
    }

    public void insert(int index, D data) {
        insert(index, Arrays.asList(data));
    }

    public <T extends D> void insert(int index, List<T> data) {
        getData().addAll(index, data);
        if (isEnableItemAnimation()) notifyItemRangeInserted(getItemStartPosition() + index, data.size());
    }

    public void remove(int index) {
        if (isEnableItemAnimation()) notifyItemRangeRemoved(getItemStartPosition() + index, 1);
        getData().remove(index);
    }

    public void removeAll() {
        if (isEnableItemAnimation()) notifyItemRangeRemoved(getItemStartPosition(), getDataCount());
        getData().clear();
    }

    public void move(int fromPosition, int toPosition) {
        D form = getData().remove(fromPosition);
        getData().add(toPosition, form);
        if (isEnableItemAnimation())
            notifyItemMoved(fromPosition + getItemStartPosition(), toPosition + getItemStartPosition());
    }
    //</editor-fold>

    @Override
    public int getItemCount() {
        return getHeadCount() + getDataCount() + getFootCount();
    }

    /**
     * 获得下标对应位置数据
     */
    protected Object findItemData(int position) {
        if (isHeadPosition(position)) {
            return getHeadItem(position);
        } else if (isItemPoistion(position)) {
            return getDataItem(getItemPosition(position));
        } else if (isFootPoistion(position)) {
            return getFootItem(position - getItemEndPosition());
        }
        return null;
    }

    /**
     * 根据Data数据源下标获得数据对象
     *
     * @param index 数据再Data中的下标
     * @return
     */
    public D getDataItem(int index) {
        return getData().get(index);
    }

    public Object getHeadItem(int index) {
        return mHeadArray.get(index);
    }

    public Object getFootItem(int index) {
        return mFootArray.get(index);
    }

    public int getItemPosition(int position) {
        return position - getHeadCount();
    }

    /**
     * 判断Position是否属于HeadView
     *
     * @param position
     * @return
     */
    protected boolean isHeadPosition(int position) {
        return position < getHeadCount();
    }

    /**
     * 判断Position是否属于HeadView
     *
     * @param position
     * @return
     */
    protected boolean isItemPoistion(int position) {
        final int count_head = getHeadCount();
        final int count_item = getDataCount();
        return count_head <= position && position < (count_head + count_item);
    }

    /**
     * 判断Position是否属于HeadView
     *
     * @param position
     * @return
     */
    protected boolean isFootPoistion(int position) {
        final int count_head = getHeadCount();
        final int count_item = getDataCount();
        final int count_foot = getFootCount();
        return (count_head + count_item) <= position && position < (count_head + count_item + count_foot);
    }

    /**
     * 获得数据源的数据量
     */
    public int getDataCount() {
        return mDataArray == null ? 0 : mDataArray.size();
    }

    /**
     * 获得头部数据数量
     */
    public int getHeadCount() {
        return mHeadArray == null ? 0 : mHeadArray.size();
    }

    /**
     * 获得底部数据数量
     */
    public int getFootCount() {
        return mFootArray == null ? 0 : mFootArray.size();
    }

    /**
     * 获得Item数据的起始坐标点
     */
    protected int getItemStartPosition() {
        return getHeadCount();
    }

    /**
     * 获得Item数据的结束坐标点
     */
    protected int getItemEndPosition() {
        return getItemStartPosition() + getDataCount();
    }

}
