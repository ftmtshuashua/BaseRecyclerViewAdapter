package support.lfp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

/**
 * <pre>
 * Tip:
 *      RecyclerView万能适配器
 *      自动创建Adapter，少写几行代码
 *      推荐使用 BaseRecyclerViewAdapter自己实现
 *
 * Function:
 *
 *
 * Created by LiFuPing on 2018/5/11
 * </pre>
 */
public class SimpleRecyclerViewAdapter<D> extends BaseRecyclerViewAdapter<D> {

    Class<? extends BaseViewHolder<D>> cls_vh;
    int layout_resouce_id;

    /**
     * 创建适配器
     *
     * @param vh                为适配器提供一个自定义的ViewHolder类型
     * @param layout_resouce_id 该ViewHolder使用的资源文件ID
     */
    public SimpleRecyclerViewAdapter(Class<? extends BaseViewHolder<D>> vh, int layout_resouce_id) {
        this.cls_vh = vh;
        this.layout_resouce_id = layout_resouce_id;
    }

    @NonNull
    @Override
    public BaseViewHolder<D> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            Constructor<BaseViewHolder<D>> cls = (Constructor<BaseViewHolder<D>>) cls_vh.getDeclaredConstructor(View.class);
            if (!cls.isAccessible()) cls.setAccessible(true);
            return cls.newInstance(LayoutInflater.from(parent.getContext()).inflate(layout_resouce_id, parent, false));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("ViewHolder构建错误，不要改变构造方法中的参数数量和参数类型，并且保证它不是一个非静态的内部类!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * <pre>
     * Tip:
     *      线程安全的对象复用工具
     *      Come form https://github.com/ftmtshuashua/ToolKit
     *
     * Function:
     *      obtain()    :获得一个实例
     *      recycle()   :回收实例
     *      create()    :创建实例
     *
     * Created by LiFuPing on 2018/6/26.
     *
     * @param <R> 关联对象，某些时候我们创建一个缓存对象需要通过其他对象的参数来创建的时候就可以利用该参数
     * @param <W> 输出对象，也就是被缓存的对象
     *
     *
     * </pre>
     */
    abstract static class ObjectCacheUtils<R, W> {
        final Object sPoolSync = new Object();
        final LinkedList<W> mScrapHeap = new LinkedList<>();

        public ObjectCacheUtils() {

        }

        /**
         * 如果缓存中存在对象,则从缓存获取.否则创建一个新对象
         *
         * @param r 关联对象
         * @return 对象实例
         */
        public W obtain(R... r) {
            W obj;
            synchronized (sPoolSync) {
                if (!mScrapHeap.isEmpty()) {
                    obj = mScrapHeap.removeFirst();
                } else {
                    obj = create(r);
                }
            }
            return obj;
        }

        /**
         * 回收实例,请确保被回收对象不在其他地方引用
         *
         * @param obj 被回收的对象
         */
        public void recycle(W obj) {
            synchronized (sPoolSync) {
                mScrapHeap.add(obj);
            }
        }

        /**
         * 当未缓存任何对象的时候,会回调此方法来创建一个新的实例
         *
         * @param r 关联对象
         * @return 对象实例
         */
        protected abstract W create(R... r);


    }
}
