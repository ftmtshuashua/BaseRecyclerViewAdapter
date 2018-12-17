package support.lfp.adapter.interior;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/4 16:06
 * </pre>
 */
  class Utils {
    /**
     * 如果这个对象为空这抛出 {@code NullPointerException},否则返回这个对象
     *
     * @param obj 被检测的对象
     * @param <T> object
     * @return 如果对象不为空则返回这个对象
     */
    public static <T> T requireNonNull(@Nullable final T obj) {
        if (obj == null) throw new NullPointerException();
        return obj;
    }


    /**
     * 遍历集合
     *
     * @param collection 被遍历的集合
     * @param action     回调函数
     * @param <T>        集合数据类型
     */
    public static <T> void map(Collection<T> collection, Action1<? super T> action) {
        requireNonNull(collection);
        requireNonNull(action);
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T module = iterator.next();
            action.call(module);
        }
    }

    @FunctionalInterface
    public interface Action1<A> {
        void call(A a);
    }
}
