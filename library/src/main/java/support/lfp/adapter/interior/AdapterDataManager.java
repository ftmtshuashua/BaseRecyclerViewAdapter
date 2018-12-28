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

    /* 用于列表中显示数据 */
    private final List<D> mDataArray = new ArrayList<>();
    private int mFlag;
    /*
     * 设置notify方法的偏移量，当数据源改变需要通知Adapter刷新UI，为刷新的UI的位置设置一个偏移量     *
     * 允许在某些情况比如顶部增加一个和数据源相对独立的数据，这个时候数据源中下标为0的数据刷新UI的时候需要调用notify(1)
     *
     */
    private int mNotifyItemOffSet = 0;


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
        if (isEnableItemAnimation()) {
            final int start = mNotifyItemOffSet + index;
            final int size = data.size();
            notifyItemRangeInserted(start, size);
            final int start_last = start + size;
            final int size_last = getItemCount() - start_last;
            notifyItemRangeChanged(start_last, size_last);
        }
    }

    public void remove(int index) {
        if (isEnableItemAnimation()) {
            final int start = mNotifyItemOffSet + index;
            final int size = 1;
            notifyItemRangeRemoved(start, size);
            final int start_last = start;
            final int size_last = getItemCount() - start_last;
            notifyItemRangeChanged(start_last, size_last);
        }
        getData().remove(index);
    }

    public void removeAll() {
        if (isEnableItemAnimation()) {
            notifyItemRangeRemoved(mNotifyItemOffSet, getDataCount());
        }
        getData().clear();
    }

    public void move(int fromPosition, int toPosition) {
        D form = getData().remove(fromPosition);
        getData().add(toPosition, form);
        if (isEnableItemAnimation())
            notifyItemMoved(fromPosition + mNotifyItemOffSet, toPosition + mNotifyItemOffSet);
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


}
