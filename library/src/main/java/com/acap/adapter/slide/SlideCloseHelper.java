package com.acap.adapter.slide;

/**
 * <pre>
 * Tip:
 *      用于主动关闭菜单
 *
 * @author A·Cap
 * @date 2021/12/13 11:21
 * </pre>
 */
public interface SlideCloseHelper {

    /**
     * 当需要关闭其他菜单时
     *
     * @param exclude 不用关闭的的菜单,如果它==null 表示关闭所有
     */
    void onCloseOther(SlideFrameLayout exclude);
}
