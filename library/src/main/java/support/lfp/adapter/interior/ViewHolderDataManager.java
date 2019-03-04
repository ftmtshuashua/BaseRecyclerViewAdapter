package support.lfp.adapter.interior;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import support.lfp.adapter.BaseRecyclerViewAdapter;

/**
 * <pre>
 * Tip:
 *      数据源
 * Function:
 *
 * Created by LiFuPing on 2018/12/10 10:14
 * </pre>
 */
public abstract class ViewHolderDataManager<D> extends RecyclerView.ViewHolder {
    D mSaveData;
    RecyclerView.Adapter mAdapter;

    public ViewHolderDataManager(View itemView) {
        super(itemView);
    }

    /**
     * 获得上下文
     *
     * @return
     */
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 获得当前ViewHolder所绑定的Adapter
     *
     * @param <T> adapter
     * @return
     */
    protected <T extends BaseRecyclerViewAdapter<D>> T getAdapter() {
        if (mAdapter instanceof BaseRecyclerViewAdapter) return (T) mAdapter;
        return null;
    }

    /**
     * 获取当前ViewHolder加载的数据
     *
     * @return object
     */
    public D getSaveData() {
        return mSaveData;
    }

    /**
     * 当ViewHolder被显示的的时候，通过该方法绑定Adapter和数据源
     *
     * @param adapter 加载ViewHolder的Adapter
     * @param data    数据源
     */
    public final void setUpdataData(RecyclerView.Adapter adapter, D data) {
        this.mAdapter = adapter;
        this.mSaveData = data;
        onUpdateUI(data);
    }

    /**
     * 当UI需要更新的时候被调用
     *
     * @param data object
     */
    protected abstract void onUpdateUI(D data);

    /**
     * 发送一个消息给Adapter的创建者，创建者调用Adapter的setViewHolderMessageHandler()方法接收消息
     *
     * @param what 消息类型
     * @param obj  消息中携带数据
     */
    protected final void sendMessage(final int what, final Object obj) {
        getAdapter().sendMessage(what, obj, getAdapterPosition());
    }

    /**
     * 通知数据变化，ViewHolder会通知通过Adapter.setOnAdapterDataChangeListener()方法设置的监听器
     */
    protected final void notifyDataChange() {
        getAdapter().notifyDataChange();
    }

    void tet() {
        getLayoutPosition(); //用户所见到的位置一致
        getAdapterPosition(); //数据再Adapter中的位置, notifyDataSetChanged方法会导致它变为 RecyclerView#NO_POSITION (-1).
    }

    /**
     * 获得当前ViewHolder所加载的数据在数据源集合中的位置
     *
     * @return 数据在数据源中的下标
     */
    public int getDataPosition() {
        return getAdapter().getData().indexOf(getSaveData());
    }

}
