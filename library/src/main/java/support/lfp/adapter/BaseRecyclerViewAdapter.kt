package support.lfp.adapter

import android.content.Context
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * <pre>
 * Tip:
 *      RecyclerView简化适配器
 * Function:
 *
 * Created by LiFuPing on 2018/5/9
 * </pre>
 */
abstract class BaseRecyclerViewAdapter<D> : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<D>>() {

    /**
     * 用于列表中显示数据
     */
    internal val mArrayModel: MutableList<D> = ArrayList()
    /**
     * 观察Adapter数据变化
     */
    internal var mOnAdapterDataChange: OnAdapterDataChange<D>? = null
    internal var mViewHolderMessageHandler: ViewHolderMessageHandler<D>? = null

    internal var mFlag: Int = 0

    /**
     * 获得列表数据
     * @return List
     */
    val data: List<D>
        get() = mArrayModel


    override fun getItemCount(): Int {
        return mArrayModel.size
    }

    /**
     * 添加数据变化检查
     * @param l OnAdapterDataChange
     */
    fun setOnAdapterDataChange(l: OnAdapterDataChange<D>) {
        mOnAdapterDataChange = l
    }

    /**
     * ViewHolder变化监听
     * @param l ViewHolderMessageHandler
     */
    fun setViewHolderMessageHandler(l: ViewHolderMessageHandler<D>) {
        mViewHolderMessageHandler = l
    }

    fun setFlag(flag: Int) {
        mFlag = mFlag or flag
    }

    /**
     * 播报数据更新
     */
    fun broadcastDataChange() {
        if (mOnAdapterDataChange != null) mOnAdapterDataChange!!.onChange(this)
    }

    /**
     * 设置数据并且更新
     *
     * @param data 数据
     * @param <T> object
    </T> */
    fun <T : D> setAndUpdata(data: List<T>) {
        val old_count = itemCount
        setData(data)
        val new_count = itemCount
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_SET != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeRemoved(0, old_count)
            notifyItemRangeChanged(0, new_count)
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     * @param <T> object
    </T> */
    fun <T : D> setData(data: List<T>) {
        mArrayModel.clear()
        addData(data)
    }

    /**
     * 设置数据并且更新
     *
     * @param data 数据
     */
    fun setAndUpdata(data: D) {
        val old_count = itemCount
        setData(data)
        val new_count = itemCount
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_SET != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeRemoved(0, old_count)
            notifyItemRangeChanged(0, new_count)
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    fun setData(data: D) {
        mArrayModel.clear()
        addData(data)
    }

    /**
     * 在末尾添加数据并更新
     *
     * @param data 数据
     * @param <T> object
    </T> */
    fun <T : D> addAndUpdata(data: List<T>) {
        val old_count = itemCount
        addData(data)
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_ADD != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(old_count, itemCount)
        }
    }

    /**
     * 在末尾添加数据
     *
     * @param data 数据
     * @param <T> object
    </T> */
    fun <T : D> addData(data: List<T>) {
        mArrayModel.addAll(data)
        broadcastDataChange()
    }

    /**
     * 在末尾添加数据并更新
     *
     * @param data 数据
     */
    fun addAndUpdata(data: D) {
        val old_count = itemCount
        addData(data)
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_ADD != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(old_count, itemCount)
        }
    }

    /**
     * 在末尾添加数据
     *
     * @param data 数据
     */
    fun addData(data: D) {
        mArrayModel.add(data)
        broadcastDataChange()
    }

    /**
     * 在index位置插入数据
     *
     * @param index 插入位置
     * @param data  数据
     * @param <T> object
    </T> */
    fun <T : D> insertAndUpdata(index: Int, data: List<T>) {
        mArrayModel.addAll(index, data)
        broadcastDataChange()
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_INSERT != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(index, data.size)
        }
    }

    /**
     * 在index位置插入单条数据
     *
     * @param index 插入位置
     * @param data  数据
     */
    fun insertAndUpdata(index: Int, data: D) {
        mArrayModel.add(index, data)
        broadcastDataChange()
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_INSERT != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemInserted(index)
        }
    }

    /**
     * 移除数据并更新
     *
     * @param postion 移除数据位置
     */
    fun removeAndUpdata(postion: Int) {
        removeData(postion)
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_REMOVE != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRemoved(postion)
        }
    }

    /**
     * 移除数据
     *
     * @param postion 移除数据位置
     */
    fun removeData(postion: Int) {
        mArrayModel.removeAt(postion)
        broadcastDataChange()
    }

    /**
     * 移动数据
     *
     * @param fromPosition 被移动数据位置
     * @param toPosition   目标位置
     */
    fun moveAndUpdata(fromPosition: Int, toPosition: Int) {
        val form = mArrayModel.removeAt(fromPosition)
        mArrayModel.add(toPosition, form)
        broadcastDataChange()
        if (mFlag and FLAG_DISABLE_ITEM_ANIMATOR_MOVE != 0) {
            notifyDataSetChanged()
        } else {
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onBindViewHolder(@NonNull holder: BaseViewHolder<D>, position: Int) {
        val model = getItemData(position)
        holder.saveData = model
        holder.adapter = this
        holder.onUpdateUI(model)
    }

    /*获得下标对应位置数据*/
    fun getItemData(postion: Int): D {
        return mArrayModel[postion]
    }

    abstract class BaseViewHolder<D>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * 获取当前UI对应的数据
         * @return  object
         */
        var saveData: D? = null
            internal set
        internal var adapter: BaseRecyclerViewAdapter<D>? = null
            internal set

        val context: Context
            get() = itemView.getContext()

        /**
         * 更新UI
         *
         * @param data object
         */
        abstract fun onUpdateUI(data: D)

        /**
         * 发送一个消息给Adapter的创建者，创建者调用Adapter的setViewHolderMessageHandler()方法接收消息
         *
         * @param what 消息类型
         * @param obj  消息中携带数据
         */
        protected fun sendMessage(what: Int, obj: D) {
            val l = adapter!!.mViewHolderMessageHandler
            l?.handlerAdapterMessage(what, obj) ?: NullPointerException("未设置ViewHolderMessageHandler!")
        }
    }

    /**
     * 监听适配器的数据变化
     */
    interface OnAdapterDataChange<D> {
        fun onChange(adapter: BaseRecyclerViewAdapter<D>)
    }

    /**
     * ViewHolder变化监听
     */
    interface ViewHolderMessageHandler<E> {

        /**
         * @param what 消息类型
         * @param obj  消息中携带数据
         */
        fun handlerAdapterMessage(what: Int, obj: E)

    }

    companion object {

        /**
         * 禁用添加数据动画
         */
        val FLAG_DISABLE_ITEM_ANIMATOR_ADD = 1

        /**
         * 禁用插入数据动画
         */
        val FLAG_DISABLE_ITEM_ANIMATOR_INSERT = 2

        /**
         * 禁用移除数据动画
         */
        val FLAG_DISABLE_ITEM_ANIMATOR_REMOVE = 4

        /**
         * 禁用设置数据动画
         */
        val FLAG_DISABLE_ITEM_ANIMATOR_SET = 8

        /**
         * 禁用设置数据移动动画
         */
        val FLAG_DISABLE_ITEM_ANIMATOR_MOVE = 16

        /**
         * 禁用所有数据动画
         */
        val FLAG_DISABLE_ITEM_ANIMATOR = 0xFF
    }

}
