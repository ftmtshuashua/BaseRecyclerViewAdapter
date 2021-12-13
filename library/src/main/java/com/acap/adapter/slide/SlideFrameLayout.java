package com.acap.adapter.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acap.adapter.BaseViewHolder;
import com.acap.adapter.TimeS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 * Tip:
 *      支持测滑的 ViewGroup
 *
 * @author A·Cap
 * @date 2021/12/6 17:57
 * </pre>
 */
public class SlideFrameLayout extends FrameLayout {

    private static final int MINIMUM_VELOCITY = 500; //最小速度
    private int mTouchSlop;     //最小滑动距离
    private VelocityTracker mVelocity;  //速度
    private Scroller mScroller;         //滚动
    private boolean mIsMenuOpened = false;

    private OnMenuStateChangeListener mOnMenuStateChangeListener;

    private int mFirstX;
    private int mFirstY;
    private int mLastX;


    /**
     * 是否正在水平滑动
     */
    private boolean isIntercept;


    public SlideFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SlideFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mMenuViewControl.isEnable()) return;
        int childCount = getChildCount();
        int v_left = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v.getVisibility() == View.VISIBLE) {
                SlideMenu.Place place = mMenuViewControl.getPlace(v);
                if (place != null) {
                    switch (place) {
                        case LEFT:
                            v_left = -v.getWidth();
                            break;
                        case RIGHT:
                            v_left = getWidth();
                            break;
                    }
                    v.layout(v_left, v.getTop(), v_left + v.getWidth(), v.getBottom());
                }
            }
        }
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    private void logE(String tag, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("xxx-" + tag, "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("xxx-" + tag, "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("xxx-" + tag, "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i("xxx-" + tag, "ACTION_CANCEL");
                break;
            default:
                Log.i("xxx-" + tag, "ACTION_" + e.getAction());
                break;
        }
    }

    /*是否启用侧滑*/
    private boolean isEnableSlide() {
        return mMenuViewControl.getSlideWidth(SlideMenu.Place.LEFT) > 0 || mMenuViewControl.getSlideWidth(SlideMenu.Place.RIGHT) > 0;
    }

    private boolean isIntercept() {
        return isIntercept && isEnableSlide();
    }

    //拦截状态设置 ,拦截之后说明正在控制菜单
    private void intercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
        getParent().requestDisallowInterceptTouchEvent(isIntercept);
    }

    //测滑滚动
    private void moveSlide(int dx) {
        dx *= 0.8f;
//        moveTranslationToX(getCurrentMoveSlide() + dx);
        moveScrollToX(getScrollX() - dx);
    }

    private void moveScrollToX(int x) {
        int slw = mMenuViewControl.getSlideWidth(SlideMenu.Place.LEFT);
        int srw = mMenuViewControl.getSlideWidth(SlideMenu.Place.RIGHT);
        if (x > 0) {// 动作：👈
            if (x > srw) x = srw;
        } else if (x < 0) {// 动作：👉
            if (x < -slw) x = -slw;
        }

        setMenuState(x != 0);
//        Log.i("Scroll", MessageFormat.format("ScrollX:{0,number,0} -> L:{1,number,0} |  R:{2,number,0}", x, slw, srw));
//        Log.i("Scroll", MessageFormat.format("ScrollX:{0,number,0} ", x));
        setScrollX(x);
    }

    //判断是否需要进入滑动状态
    private boolean isScrollAtMove(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        mVelocity.computeCurrentVelocity(1000);
        int velocityX = (int) Math.abs(mVelocity.getXVelocity());
        int velocityY = (int) Math.abs(mVelocity.getYVelocity());
        int moveX = Math.abs(x - mFirstX);
        int moveY = Math.abs(y - mFirstY);
        //满足如下条件其一则判定为水平滑动：
        //1、水平速度大于竖直速度,且水平速度大于最小速度
        //2、水平位移大于竖直位移,且大于最小移动距离
        //必需条件：itemView菜单栏宽度大于0，且recyclerView处于静止状态（即并不在竖直滑动和拖拽）
        return (Math.abs(velocityX) >= MINIMUM_VELOCITY && velocityX > velocityY || moveX > moveY && moveX > mTouchSlop);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!isEnableSlide()) return super.onInterceptTouchEvent(e);
//        logE("onInterceptTouchEvent", e);
        addVelocityEvent(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) mScroller.abortAnimation();      //若Scroller处于动画中，则终止动画
                mFirstX = (int) e.getX();
                mFirstY = (int) e.getY();
                mLastX = (int) e.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrollAtMove(e)) {
                    intercept(true);
                    return true; //设置其已处于水平滑动状态，并拦截事件
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //点击或者其他操作，
//                release();
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!isIntercept()) return super.onTouchEvent(e);
        int x = (int) e.getX();
        addVelocityEvent(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                moveSlide(x - mLastX);
                mLastX = x;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocity.computeCurrentVelocity(1000);
                float xVelocity = mVelocity.getXVelocity();  //  x<0:左滑动 x>0:右滑动

                //先前没结束的动画终止，并直接到终点
                releaseAnimScroll();

                //动作
                int scrollX = getScrollX();
                if (scrollX > 0) { //右菜单
                    int with = mMenuViewControl.getSlideWidth(SlideMenu.Place.RIGHT);
                    if ((Math.abs(scrollX) > with / 2 && xVelocity < 0) || xVelocity < -MINIMUM_VELOCITY) { // 操过了菜单的一半 或者动作速度达到
                        animScrollerTo(with);
                    } else { //条件不满足 关闭菜单
                        animScrollerTo(0);
                    }
                } else if (scrollX < 0) { //左菜单
                    int with = mMenuViewControl.getSlideWidth(SlideMenu.Place.LEFT);
                    if ((Math.abs(scrollX) > with / 2 && xVelocity > 0) || xVelocity > MINIMUM_VELOCITY) { // 操过了菜单的一半 或者动作速度达到
                        animScrollerTo(-with);
                    } else { //条件不满足 关闭菜单
                        animScrollerTo(0);
                    }
                }

                releaseVelocity();
                intercept(false);
                break;
        }
        return super.onTouchEvent(e);
    }


    //菜单状态改变时
    private void setMenuState(boolean open) {
        if (mIsMenuOpened != open) {
            mIsMenuOpened = open;
            mOnMenuStateChangeListener.onMenuStateChange(this, open);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    //立即回到初始状态
    private void release() {
        releaseVelocity();
        releaseAnimScroll();
        moveScrollToX(0);
    }

    //通过动画回到初始状态
    public void closeMenu() {
        if (mIsMenuOpened) {
            releaseAnimScroll();
            animScrollerTo(0);
        }
    }

    //滚动到某个位置
    private void animScrollerTo(int x) {
        int scrollX = getScrollX();
        int dx = x - scrollX;
        mScroller.startScroll(scrollX, 0, dx, 0, Math.abs(dx));
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            moveScrollToX(mScroller.getCurrX());
            postInvalidate();
        }
    }

    /**
     * 获取VelocityTracker实例，并为其添加事件
     *
     * @param e 触碰事件
     */
    private void addVelocityEvent(MotionEvent e) {
        if (mVelocity == null) {
            mVelocity = VelocityTracker.obtain();
        }
        mVelocity.addMovement(e);
    }

    //释放VelocityTracker
    private void releaseVelocity() {
        if (mVelocity != null) {
            mVelocity.clear();
            mVelocity.recycle();
            mVelocity = null;
        }
    }

    //释放动画,注意释放之后Scroll会停留在当前位置
    private void releaseAnimScroll() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }


    //监听菜单的打开状态
    public void setOnMenuStateChangeListener(OnMenuStateChangeListener listener) {
        mOnMenuStateChangeListener = listener;
    }

    /**
     * 菜单那状态监听
     */
    public interface OnMenuStateChangeListener {
        void onMenuStateChange(SlideFrameLayout layout, boolean isOpen);
    }


    //------------------------------------- 菜单项 --------------------------------------

    private MenuViewControl mMenuViewControl = new MenuViewControl(this);

    /**
     * 设置侧滑菜单
     *
     * @param vh
     * @param slideMenuIds
     */
    public void setSlideMenu(SlideMenuProvider menus, BaseViewHolder<?> vh, int[] slideMenuIds) {
        TimeS timeS = new TimeS();
        mMenuViewControl.setSlideMenu(menus, slideMenuIds);
        timeS.look();
        for (SlideMenu menu : menus.getMenus()) {
            View view = mMenuViewControl.getMenuView(menu);
            if (view != null) {
                menu.onViewBind(view, vh);
            }
        }
        timeS.look();
    }

    // MenuView 管理
    private static final class MenuViewControl {
        private final SlideFrameLayout mRoot;
        private SlideMenuProvider mMenus;
        //缓存 layout_id -> View
//        private final HashMap<Integer, View> mLayoutToViews = new HashMap<>();
        //绑定 View 和 Menu
        private final HashMap<SlideMenu, View> mMenuToView = new HashMap<>();
        private final HashMap<View, SlideMenu> mViewToMenu = new HashMap<>();

//        private final HashMap<Integer, View> mViewToPlace = new HashMap<>();
//        private final HashMap<Integer, SlideMenu> mCacheMenus = new HashMap<>();

        private final boolean[] mMenuChange = new boolean[]{true, true};
        private final int[] mMenuWidth = new int[]{0, 0};  //数据变化时

        public MenuViewControl(SlideFrameLayout layout) {
            mRoot = layout;
        }

        public boolean isEnable() {
            return mMenus != null && !mMenus.isEmpty();
        }

        //添加菜单
        public void setSlideMenu(SlideMenuProvider menus, int[] slideMenuIds) {
            this.mMenus = menus;

            List<SlideMenu> show_menus = new ArrayList<>();
            //初始化与加载
            if (slideMenuIds == null) {
                for (SlideMenu slideMenu : menus.getMenus()) {
                    insert(slideMenu);
                    show_menus.add(slideMenu);
                }
            } else {
                for (int slideMenuId : slideMenuIds) {
                    SlideMenu slideMenu = menus.getMenuById(slideMenuId);
                    insert(slideMenu);
                    show_menus.add(slideMenu);
                }
            }

            //隐藏非当前菜单
            for (SlideMenu slideMenu : mMenuToView.keySet()) {
                View view = mMenuToView.get(slideMenu);
                if (show_menus.contains(slideMenu)) {
                    if (view.getVisibility() != View.VISIBLE) {
                        view.setVisibility(View.VISIBLE);
                        setMenuChange(null);
                    }
                } else {
                    if (view.getVisibility() != View.GONE) {
                        view.setVisibility(View.GONE);
                        setMenuChange(null);
                    }
                }
            }
        }

        //添加View到布局
        private void addView(SlideMenu slideMenu, View view) {
            mMenuToView.put(slideMenu, view);
            mViewToMenu.put(view, slideMenu);
            mRoot.addView(view);
        }

        //插入菜单
        private void insert(SlideMenu slideMenu) {
            View view = mMenuToView.get(slideMenu);
            if (view == null) {
                view = LayoutInflater.from(mRoot.getContext()).inflate(slideMenu.getLayoutId(), mRoot, false);
                addView(slideMenu, view);
                setMenuChange(slideMenu.getPlace());
            }
        }

        //获得某个方向种菜单的宽度
        public int getSlideWidth(SlideMenu.Place place) {
            int ordinal = place.ordinal();
            if (mMenuChange[ordinal]) {
                mMenuChange[ordinal] = false;

                List<View> arrays = new ArrayList<>();
                for (SlideMenu menu : mMenuToView.keySet()) {
                    if (menu.getPlace() == place) {
                        arrays.add(mMenuToView.get(menu));
                    }
                }

                mMenuWidth[ordinal] = getMaxWidth(arrays);
            }
            return mMenuWidth[ordinal];
        }

        //获得布局所在位置
        private SlideMenu.Place getPlace(View view) {
            SlideMenu slideMenu = mViewToMenu.get(view);
            if (slideMenu != null) return slideMenu.getPlace();
            return null;
        }

        //设置菜单变化
        private void setMenuChange(SlideMenu.Place place) {
            if (place == null) {
                Arrays.fill(mMenuChange, true);
            } else {
                mMenuChange[place.ordinal()] = true;
            }
        }


        //获得View列表种View的最大宽度
        private int getMaxWidth(Collection<View> collection) {
            int width = 0;
            Iterator<View> iterator = collection.iterator();
            while (iterator.hasNext()) {
                View next = iterator.next();
                if (next.getVisibility() == View.VISIBLE) {
                    width = Math.max(width, next.getWidth());
                }
            }
            return width;
        }


        public View getMenuView(SlideMenu menu) {
            return mMenuToView.get(menu);
        }
    }

}
