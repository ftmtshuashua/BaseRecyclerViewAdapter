package support.lfp.adapter;

/**
 * <pre>
 * Tip:
 *      配置项
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/15 17:07
 * </pre>
 */
public final class BaseRecyclerViewConfig {
    /**
     * ItemView包裹配置，默认开启
     * 关闭此项可以提升一定的性能，但是会使setOnItemClickListener方法失效
     */
    protected static boolean IsEnableItemViewPackage = true;

    /**
     * 配置ItemView包裹
     * 关闭此项可以提升一定的性能，但是会使setOnItemClickListener方法失效
     *
     * @param is true:开启  false:关闭
     */
    public static final void setIsEnableItemViewPackage(boolean is) {
        IsEnableItemViewPackage = is;
    }
}
