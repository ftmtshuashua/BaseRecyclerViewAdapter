package support.lfp.adapter.interior;

import android.view.View;
import support.lfp.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * <pre>
 * Tip:
 *      可观察的RecyclerViewAdapter
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/10 09:25
 * </pre>
 */
public abstract class AdapterObservable<D> extends AdapterDataManager<D> {

    /* 数据变化监听 */
    private OnAdapterDataChangeListener mOnAdapterDataChangeListener;
    /* ViewHolder消息处理器 */
    private ArrayList<ViewHolderMessageHandler> mViewHolderMessageHandler = new ArrayList<>();
    /* ItemView点击事件监听 */
    private OnItemClickListener mOnItemClickListener;
    /*ItemView长按事件监听*/
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 设置ItemView点击事件监听
     *
     * @param l The AdapterView.OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<? super D> l) {
        mOnItemClickListener = l;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener<? super D> l) {
        mOnItemLongClickListener = l;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    /**
     * 添加数据变化检查
     *
     * @param l OnAdapterDataChange
     */
    public void setOnAdapterDataChangeListener(OnAdapterDataChangeListener<? super D> l) {
        mOnAdapterDataChangeListener = l;
    }

    public OnAdapterDataChangeListener getOnAdapterDataChangeListener() {
        return mOnAdapterDataChangeListener;
    }

    /**
     * ViewHolder消息处理器，用于接收和处理来自ViewHolder的消息
     *
     * @param handler ViewHolderMessageHandler
     */
    public void addViewHolderMessageHandler(ViewHolderMessageHandler  handler) {
        mViewHolderMessageHandler.add(handler);
    }

    public void removeViewHolderMessageHandler(ViewHolderMessageHandler handler) {
        mViewHolderMessageHandler.remove(handler);
    }


    /*Item点击监听*/
    public interface OnItemClickListener<D> {
        void onItemClick(AdapterObservable<D> adapter, BaseViewHolder<D> viewHolder, View view, int position);
    }

    /*Item长按事件监听*/
    public interface OnItemLongClickListener<D> {
        boolean onItemLongClick(AdapterObservable<D> adapter, BaseViewHolder<D> viewHolder, View view, int position);
    }

    /**
     * 通知数据变化,通过setOnAdapterDataChangeListener方法接收这个通知
     */
    public void notifyDataChange() {
        if (mOnAdapterDataChangeListener != null) mOnAdapterDataChangeListener.onChange(this);
    }

    /**
     * 发送一个消息给Adapter的创建者，创建者调用Adapter的setViewHolderMessageHandler()方法接收消息
     *
     * @param what 消息类型
     * @param obj  消息中携带数据
     */
    protected void sendMessage(final int what, final Object obj, final int position) {
        Utils.map(mViewHolderMessageHandler, new Utils.Action1<AdapterObservable.ViewHolderMessageHandler>() {
            @Override
            public void call(AdapterObservable.ViewHolderMessageHandler viewHolderMessageHandler) {
                viewHolderMessageHandler.handlerAdapterMessage(what, obj, position);
            }
        });
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
        void onChange(AdapterObservable<D> adapter);
    }

    /**
     * ViewHolder消息观察者，用于接收来自ViewHolder通过sendMessage()方法发出的消息
     */
    public interface ViewHolderMessageHandler  {

        /**
         * 接收到一条来自ViewHolder的消息
         *
         * @param what        消息标记
         * @param obj         消息中携带数据
         * @param layoutIndex 该ViewHolder在Adapter中的位置
         */
        void handlerAdapterMessage(int what, Object obj, int layoutIndex);

    }

    @Override
    protected void onOperationDataOrigin() {
        super.onOperationDataOrigin();
        notifyDataChange();
    }

}
