package support.lfp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import java.lang.reflect.Constructor

/**
 * <pre>
 * Tip:
 *      自动创建Adapter，少写几行代码
 *      推荐使用 BaseRecyclerViewAdapter自己实现
 *      因为使用的反射，所以在混淆的时候请注意配置
 * Function:
 *
 * Created by LiFuPing on 2018/5/11.
 * </pre>
 */
class SimpleRecyclerViewAdapter<D>(
    internal var cls_vh: Class<out NotProguardViewHolder<D>>,
    internal var layout_resouce_id: Int
) : BaseRecyclerViewAdapter<D>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): BaseViewHolder<D> {
        try {
            val cls = cls_vh.getDeclaredConstructor(View::class.java)
            if (!cls.isAccessible) cls.isAccessible = true
            return cls.newInstance(LayoutInflater.from(parent.context).inflate(layout_resouce_id, parent, false))
        } catch (ex: NoSuchMethodException) {
            throw RuntimeException("ViewHolder构建错误，不要改变构造方法中的参数数量和参数类型，并且保证它不是一个非静态的内部类!")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    /**
     * 使用反射创建ViewHolder必须保证它不被混淆
     * 推荐使用 BaseRecyclerViewAdapter自己实现
     *
     * @param <T> object
    </T> */
    abstract class NotProguardViewHolder<T>(itemView: View) : BaseViewHolder<T>(itemView)

}
