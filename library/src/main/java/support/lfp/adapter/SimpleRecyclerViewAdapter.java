package support.lfp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;

/**
 * <pre>
 * Tip:
 *      简单RecyclerView万能适配器，
 *      自动创建Adapter，少写几行代码
 *      推荐使用 BaseRecyclerViewAdapter自己实现
 *
 * Function:
 *
 *
 *
 *
 * Created by LiFuPing on 2018/5/11
 * @param <D> 数据源类型<br/>
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

}
