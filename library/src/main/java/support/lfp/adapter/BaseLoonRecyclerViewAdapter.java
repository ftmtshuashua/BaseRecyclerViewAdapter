package support.lfp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * <pre>
 * Tip:
 *      RecyclerView万能适配器懒人版,不需要建立ViewHolder
 *
 *
 * Function:
 *
 *
 * Created by LiFuPing on 22018/5/9
 * @param <D> 数据源类型
 * @param <K> ViewHolder类型
 * </pre>
 */
public abstract class BaseLoonRecyclerViewAdapter<D, K extends BaseLoonViewHolder> extends BaseRecyclerViewAdapter<D> implements BaseLoonViewHolder.OnViewHolderUpdata<BaseLoonViewHolder<D>, D> {

    final int mLayoutId; //布局ID

    /**
     * @param layout ViewHolder 加载的布局
     */
    public BaseLoonRecyclerViewAdapter(@LayoutRes int layout) {
        mLayoutId = layout;
    }

    @NonNull
    @Override
    public BaseLoonViewHolder<D> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseLoonViewHolder<D> viewholder = generateBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false));
        viewholder.setOnViewHolderUpdata(this);
        return viewholder;
    }

    /**
     * 通过ViewType类型获得布局文件ID
     *
     * @param viewType
     * @return
     */
    protected int getLayoutId(int viewType) {
        return mLayoutId;
    }


    /*获得泛型对应的ViewHolder*/
    private K generateBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (K) new BaseLoonViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new BaseLoonViewHolder(view);
    }

    /*获得泛型的类型*/
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    /*生成泛型对应的ViewHolder*/
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
