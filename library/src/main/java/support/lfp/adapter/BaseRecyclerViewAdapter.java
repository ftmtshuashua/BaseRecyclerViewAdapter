package support.lfp.adapter;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


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
public abstract class BaseRecyclerViewAdapter<D> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<D>> {

    /**
     * 禁用添加数据动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR_ADD = 1;

    /**
     * 禁用插入数据动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR_INSERT = 2;

    /**
     * 禁用移除数据动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR_REMOVE = 4;

    /**
     * 禁用设置数据动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR_SET = 8;

    /**
     * 禁用设置数据移动动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR_MOVE = 16;

    /**
     * 禁用所有数据动画
     */
    public static final int FLAG_DISABLE_ITEM_ANIMATOR = 0xFF;

    /* 用于列表中显示数据 */
    private final List<D> mArrayModel = new ArrayList<>();
    /* 在源数据以外提供另一个数据集，该数据集优先于源数据。当它不为空的时候首先展示它,而不展示源数据集中的数据 */
    private final List<D> mArrayModelSearch = new ArrayList<>();


    /* 数据变化监听 */
    private OnAdapterDataChangeListener mOnAdapterDataChangeListener;
    /* ViewHolder消息处理器 */
    private ArrayList<ViewHolderMessageHandler> mViewHolderMessageHandler = new ArrayList<>();
    /* ItemView点击事件监听 */
    private OnItemClickListener mOnItemClickListener;

    private int mFlag;

    /**
     * 判断是否为搜索模式
     */
    public boolean isSearched() {
        return !mArrayModelSearch.isEmpty();
    }

    /**
     * 设置ItemView点击事件监听
     *
     * @param l The AdapterView.OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<? super D> l) {
        mOnItemClickListener = l;
    }

    /**
     * 添加数据变化检查
     *
     * @param l OnAdapterDataChange
     */
    public void setOnAdapterDataChangeListener(OnAdapterDataChangeListener<? super D> l) {
        mOnAdapterDataChangeListener = l;
    }

    /**
     * ViewHolder消息处理器，用于接收和处理来自ViewHolder的消息
     *
     * @param handler ViewHolderMessageHandler
     */
    public void addViewHolderMessageHandler(ViewHolderMessageHandler handler) {
        mViewHolderMessageHandler.add(handler);
    }

    public void removeViewHolderMessageHandler(ViewHolderMessageHandler handler) {
        mViewHolderMessageHandler.remove(handler);
    }

    /**
     * 设置Adapter标志
     *
     * @param flag
     */
    public void setFlag(int flag) {
        mFlag |= flag;
    }

    /**
     * 调用OnItemClickListener监听
     *
     * @param view
     * @return 是否成功调用
     */
    public boolean performItemClick(BaseViewHolder view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(this, view, view.getAdapterPosition(), view.getItemId());
            return true;
        }
        return false;
    }


    /**
     * 通知适配器的数据变化观察者，该适配器有一个数据变化事件
     */
    public final void broadcastDataChange() {
        if (mOnAdapterDataChangeListener != null) mOnAdapterDataChangeListener.onChange(this);
    }

    /**
     * 获得源数据
     *
     * @return List
     */
    public List<D> getData() {
        return mArrayModel;
    }

    /**
     * 获得搜索源数据
     *
     * @return List
     */
    public List<D> getSearchData() {
        return mArrayModelSearch;
    }

    /**
     * 设置数据并且更新
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void setAndUpdata(List<T> data) {
        int old_count = getItemCount();
        setData(data);
        int new_count = getItemCount();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_SET) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, old_count);
            notifyItemRangeChanged(0, new_count);
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void setData(List<T> data) {
        mArrayModel.clear();
        addData(data);
    }


    /**
     * 设置数据并且更新
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void setSearchAndUpdata(List<T> data) {
        int old_count = getItemCount();
        setSearchData(data);
        int new_count = getItemCount();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_SET) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, old_count);
            notifyItemRangeChanged(0, new_count);
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void setSearchData(List<T> data) {
        mArrayModelSearch.clear();
        addSearchData(data);
    }

    /**
     * 设置数据并且更新
     *
     * @param data 数据
     */
    public void setAndUpdata(D data) {
        int old_count = getItemCount();
        setData(data);
        int new_count = getItemCount();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_SET) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, old_count);
            notifyItemRangeChanged(0, new_count);
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setData(D data) {
        mArrayModel.clear();
        addData(data);
    }


    /**
     * 设置数据并且更新
     *
     * @param data 数据
     */
    public void setSearchAndUpdata(D data) {
        int old_count = getItemCount();
        setSearchData(data);
        int new_count = getItemCount();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_SET) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, old_count);
            notifyItemRangeChanged(0, new_count);
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setSearchData(D data) {
        mArrayModelSearch.clear();
        addSearchData(data);
    }

    /**
     * 在末尾添加数据并更新
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void addAndUpdata(List<T> data) {
        int old_count = getItemCount();
        addData(data);
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_ADD) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(old_count, getItemCount());
        }
    }

    /**
     * 在末尾添加数据
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void addData(List<T> data) {
        mArrayModel.addAll(data);
        broadcastDataChange();
    }


    /**
     * 在末尾添加数据并更新
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void addSearchAndUpdata(List<T> data) {
        int old_count = getItemCount();
        addSearchData(data);
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_ADD) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(old_count, getItemCount());
        }
    }

    /**
     * 在末尾添加数据
     *
     * @param data 数据
     * @param <T>  object
     */
    public <T extends D> void addSearchData(List<T> data) {
        mArrayModelSearch.addAll(data);
        broadcastDataChange();
    }

    /**
     * 在末尾添加数据并更新
     *
     * @param data 数据
     */
    public void addAndUpdata(D data) {
        int old_count = getItemCount();
        addData(data);
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_ADD) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(old_count, getItemCount());
        }
    }

    /**
     * 在末尾添加数据
     *
     * @param data 数据
     */
    public void addData(D data) {
        mArrayModel.add(data);
        broadcastDataChange();
    }

    /**
     * 在末尾添加数据并更新
     *
     * @param data 数据
     */
    public void addSearchAndUpdata(D data) {
        int old_count = getItemCount();
        addSearchData(data);
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_ADD) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(old_count, getItemCount());
        }
    }

    /**
     * 在末尾添加数据
     *
     * @param data 数据
     */
    public void addSearchData(D data) {
        mArrayModelSearch.add(data);
        broadcastDataChange();
    }


    /**
     * 在index位置插入数据
     *
     * @param index 插入位置
     * @param data  数据
     * @param <T>   object
     */
    public <T extends D> void insertAndUpdata(int index, List<T> data) {
        mArrayModel.addAll(index, data);
        broadcastDataChange();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_INSERT) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(index, data.size());
        }
    }

    /**
     * 在index位置插入单条数据
     *
     * @param index 插入位置
     * @param data  数据
     */
    public void insertAndUpdata(int index, D data) {
        mArrayModel.add(index, data);
        broadcastDataChange();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_INSERT) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(index);
        }
    }


    /**
     * 在index位置插入数据
     *
     * @param index 插入位置
     * @param data  数据
     * @param <T>   object
     */
    public <T extends D> void insertSearchAndUpdata(int index, List<T> data) {
        mArrayModelSearch.addAll(index, data);
        broadcastDataChange();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_INSERT) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(index, data.size());
        }
    }

    /**
     * 在index位置插入单条数据
     *
     * @param index 插入位置
     * @param data  数据
     */
    public void insertSearchAndUpdata(int index, D data) {
        mArrayModelSearch.add(index, data);
        broadcastDataChange();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_INSERT) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(index);
        }
    }

    /**
     * 移除数据并更新
     *
     * @param postion 移除数据位置
     */
    public void removeAndUpdata(int postion) {
        removeData(postion);
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_REMOVE) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(postion);
        }
    }

    /**
     * 移除数据
     *
     * @param postion 移除数据位置
     */
    public void removeData(int postion) {
        mArrayModel.remove(postion);
        broadcastDataChange();
    }

    /**
     * 移除数据并更新
     *
     * @param postion 移除数据位置
     */
    public void removeSearchAndUpdata(int postion) {
        removeSearchData(postion);
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_REMOVE) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(postion);
        }
    }

    /**
     * 移除数据
     *
     * @param postion 移除数据位置
     */
    public void removeSearchData(int postion) {
        mArrayModelSearch.remove(postion);
        broadcastDataChange();
    }


    /**
     * 移动数据
     *
     * @param fromPosition 被移动数据位置
     * @param toPosition   目标位置
     */
    public void moveAndUpdata(int fromPosition, int toPosition) {
        D form = mArrayModel.remove(fromPosition);
        mArrayModel.add(toPosition, form);
        broadcastDataChange();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_MOVE) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemMoved(fromPosition, toPosition);
        }
    }


    /**
     * 移动数据
     *
     * @param fromPosition 被移动数据位置
     * @param toPosition   目标位置
     */
    public void moveSearchAndUpdata(int fromPosition, int toPosition) {
        D form = mArrayModelSearch.remove(fromPosition);
        mArrayModelSearch.add(toPosition, form);
        broadcastDataChange();
        if ((mFlag & FLAG_DISABLE_ITEM_ANIMATOR_MOVE) != 0) {
            notifyDataSetChanged();
        } else {
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<D> holder, int position) {
        D model = getItemData(position);
        holder.mSaveData = model;
        holder.mAdapter = this;
        holder.onUpdateUI(model);
    }

    /*获得下标对应位置数据*/
    public D getItemData(int postion) {
        if (isSearched()) return mArrayModelSearch.get(postion);
        return mArrayModel.get(postion);
    }

    @Override
    public int getItemCount() {
        if (isSearched()) return mArrayModelSearch.size();
        return mArrayModel.size();
    }

    public static abstract class BaseViewHolder<D> extends RecyclerView.ViewHolder {
        D mSaveData;
        BaseRecyclerViewAdapter<D> mAdapter;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        protected BaseRecyclerViewAdapter<D> getAdapter() {
            return mAdapter;
        }

        /**
         * 获取当前UI对应的数据
         *
         * @return object
         */
        public D getSaveData() {
            return mSaveData;
        }

        /**
         * 更新UI
         *
         * @param data object
         */
        public abstract void onUpdateUI(D data);

        /**
         * 发送一个消息给Adapter的创建者，创建者调用Adapter的setViewHolderMessageHandler()方法接收消息
         *
         * @param what 消息类型
         * @param obj  消息中携带数据
         */
        protected final void sendMessage(final int what, final Object obj) {
            Utils.map(mAdapter.mViewHolderMessageHandler, new Utils.Action1<ViewHolderMessageHandler>() {
                @Override
                public void call(ViewHolderMessageHandler viewHolderMessageHandler) {
                    viewHolderMessageHandler.handlerAdapterMessage(what, obj, getAdapterPosition());
                }
            });
        }
    }

    /**
     * 适配器的数据变化监听器,当Set，Add，Remove方法被调用的时候，该监听器能接收到反馈
     */
    public interface OnAdapterDataChangeListener<D> {
        /**
         * 当Adapter数据变化的时候
         *
         * @param adapter The adapter
         */
        void onChange(BaseRecyclerViewAdapter<D> adapter);
    }

    /**
     * ViewHolder消息观察者，用于接收来自ViewHolder通过sendMessage()方法发出的消息
     */
    public interface ViewHolderMessageHandler<E> {

        /**
         * 接收到一条来自ViewHolder的消息
         *
         * @param what         消息标记
         * @param obj          消息中携带数据
         * @param adapterIndex 该ViewHolder在Adapter中的位置
         */
        void handlerAdapterMessage(int what, E obj, int adapterIndex);

    }

    /**
     * 当点击Item的时候回调
     */
    public interface OnItemClickListener<T> {
        /**
         * 当点击了Item回调
         *
         * @param parent   The adapter
         * @param view     The view of ViewHolder
         * @param position The view item
         * @param id
         */
        void onItemClick(BaseRecyclerViewAdapter<T> parent, BaseViewHolder<T> view, int position, long id);
    }
}
