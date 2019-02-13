package support.lfp.adapter.interior;

import java.util.List;

/**
 * <pre>
 * Tip:
 *      表示一个数据源的操作
 *
 * Function:
 *
 * Created by LiFuPing on 2019/2/11 10:01
 * </pre>
 */
public interface IDataOrigin<D> {

    /**
     * 使用一条数据替换数据源,并更新UI
     *
     * @param data 替换数据
     */
    void set(D data);

    /**
     * 使用一组集合数据替换数据源,并更新UI
     *
     * @param data 添加数据
     * @param <T>  数据集合中数据类型是一个<D>类型
     */
    <T extends D> void set(List<T> data);

    /**
     * 在数据源的末尾添加一条数据,并更新UI
     *
     * @param data 添加的数据
     */
    void add(D data);

    /**
     * 在数据源的末尾添加一组数据,并更新UI
     *
     * @param data 添加数据
     * @param <T>  数据集合中数据类型是一个<D>类型
     */
    <T extends D> void add(List<T> data);

    /**
     * 在Index位置插入一条数据,并更新UI
     *
     * @param index 数据插入位置
     * @param data  插入的数据
     */
    void insert(int index, D data);

    /**
     * 在Index位置插入一组数据,并更新UI
     *
     * @param index 数据插入位置
     * @param data  插入的数据集合
     * @param <T>   数据集合中数据类型是一个<D>类型
     */
    <T extends D> void insert(int index, List<T> data);

    /**
     * 移除Index位置的数据,并更新UI
     *
     * @param index 被移除数据的位置
     * @return 被移除的数据
     */
    D remove(int index);

    /**
     * 移除从index开始的count条数据,并更新UI
     *
     * @param index 被移除数据的起点位置
     * @param count 被移除数据条数
     * @return 被移除的数据
     */
    List<D> remove(int index, int count);

    /**
     * 移除所有数据,并更新UI
     */
    void removeAll();

    /**
     * 移动数据位置,并更新UI
     *
     * @param fromIndex 被移动数据的位置
     * @param toIndex   希望移动到哪里
     */
    void move(int fromIndex, int toIndex);

    /**
     * 替换数据,并更新UI
     *
     * @param index 被替换数据位置
     * @param data  替换的数据
     * @return 被替换的数据
     */
    D replace(int index, D data);

}
