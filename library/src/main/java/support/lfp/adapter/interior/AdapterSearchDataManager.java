package support.lfp.adapter.interior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * Tip:
 *      提供一个搜索数据源
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/14 17:33
 * </pre>
 */
public abstract class AdapterSearchDataManager<D> extends AdapterDataManager<D> {
    /**
     * 搜索模式下启用头部数据
     */
    public static final int FLAG_ENABLE_SEARCHE_HEAD = 0x0000000100;
    /**
     * 搜索模式下启用脚部数据
     */
    public static final int FLAG_ENABLE_SEARCHE_FOOT = 0x0000000200;
    /**
     * 搜索模式下启用头部数据和脚部数据
     */
    public static final int MASK_ENABLE_SEARCHE_FOOT_AND_HEAD = 0xF00;

    /* 在源数据以外提供另一个数据集，该数据集优先于源数据。当它不为空的时候首先展示它,而不展示源数据集中的数据 */
    private List<D> mSearchArray;

    /**
     * 判断是否为Search模式
     */
    public boolean isSearched() {
        return mSearchArray != null && !mSearchArray.isEmpty();
    }

    /**
     * 获得Search源数据
     *
     * @return List
     */
    public List<D> getSearch() {
        if (mSearchArray == null) mSearchArray = new ArrayList<>();
        return mSearchArray;
    }

    /*----------- 数据操作 -----------*/
    //<editor-fold desc="Set() |  Add()  |  Insert()  || Move()">
    public void setSearch(D data) {
        setSearch(Arrays.asList(data));
    }

    public <T extends D> void setSearch(List<T> data) {
        if (getSearchCount() > 0) removeSearchAll();
        addSearch(data);
    }

    public void addSearch(D data) {
        addSearch(Arrays.asList(data));
    }

    public <T extends D> void addSearch(List<T> data) {
        insertSearch(getSearchCount(), data);
    }

    public void insertSearch(int index, D data) {
        insertSearch(index, Arrays.asList(data));
    }

    public <T extends D> void insertSearch(int index, List<T> data) {
        getSearch().addAll(index, data);
        if (isEnableItemAnimation()) notifyItemRangeInserted(getNotifyItemOffSet() + index, data.size());
    }

    public void removeSearch(int index) {
        if (isEnableItemAnimation()) notifyItemRangeRemoved(getNotifyItemOffSet() + index, 1);
        getSearch().remove(index);
    }

    public void removeSearchAll() {
        if (isEnableItemAnimation()) notifyItemRangeRemoved(getNotifyItemOffSet(), getSearchCount());
        getSearch().clear();
    }

    public void moveSearch(int fromPosition, int toPosition) {
        D form = getSearch().remove(fromPosition);
        getSearch().add(toPosition, form);
        if (isEnableItemAnimation())
            notifyItemMoved(fromPosition + getNotifyItemOffSet(), toPosition + getNotifyItemOffSet());
    }
    //</editor-fold>

    @Override
    public int getItemCount() { //该方法暂未实现
        return super.getItemCount();
//        return getHeadCount() + getSearchCount() + getFootCount();
    }

    @Override
    public D getDataItem(int position) { //该方法暂未实现
//        if (isSearched()) {
//            if (isHeadPosition(position)) {
//                return mHeadArray.get(position);
//            } else if (isItemPoistion(position)) {
//                return getSearchItem(getItemPosition(position));
//            } else if (isFootPoistion(position)) {
//                return mFootArray.get(position);
//            }
//            return null;
//        }
        return super.getDataItem(position);
    }


    /**
     * 获得Searched数据源的数据量
     */
    public int getSearchCount() {
        return mSearchArray == null ? 0 : mSearchArray.size();
    }

}
