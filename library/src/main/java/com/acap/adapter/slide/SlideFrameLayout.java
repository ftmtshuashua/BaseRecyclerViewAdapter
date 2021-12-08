package com.acap.adapter.slide;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 * Tip:
 *      支持测滑的 ViewGroup
 *
 * @author A·Cap
 * @date 2021/12/6 17:57
 * </pre>
 */
public class SlideFrameLayout extends FrameLayout implements SlideControl.OnScrolling {

    private static final int MINIMUM_VELOCITY = 500; //最小速度
    private int mTouchSlop;     //最小滑动距离
    private VelocityTracker mVelocity;  //速度
    private Scroller mScroller;         //滚动
    private boolean mIsMenuOpened = false;

    private OnMenuStateChangeListener mOnMenuStateChangeListener;

    private int mFirstX;
    private int mFirstY;
    private int mLastX;


    /* 用于滚动显示的View */
    private View mSlideLeftView, mSlideRightView;

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
        if (mSlideLeftView != null) {
            int width = mSlideLeftView.getWidth();
            int v_left = -width;
            int v_right = v_left + width;
            int v_top = mSlideLeftView.getTop();
            int v_bottom = mSlideLeftView.getBottom();
            mSlideLeftView.layout(v_left, v_top, v_right, v_bottom);
        }
        if (mSlideRightView != null) {
            int width = mSlideRightView.getWidth();
            int v_left = getWidth();
            int v_right = v_left + width;
            int v_top = mSlideRightView.getTop();
            int v_bottom = mSlideRightView.getBottom();
            mSlideRightView.layout(v_left, v_top, v_right, v_bottom);
        }
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext());

        mSlideLeftView = new TextView(getContext());
        mSlideLeftView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        ((TextView) mSlideLeftView).setText("左菜单内容");
        mSlideLeftView.setBackgroundColor(Color.RED);
        mSlideRightView = new Button(getContext());
        mSlideRightView.setLayoutParams(new FrameLayout.LayoutParams(600, LayoutParams.MATCH_PARENT));
        mSlideRightView.setBackgroundColor(Color.BLUE);
        mSlideLeftView.setOnClickListener(v -> Log.i("Scroll", "点击了 Left 按钮"));
        mSlideRightView.setOnClickListener(v -> Log.i("Scroll", "点击了 Right 按钮"));
        addView(mSlideLeftView);
        addView(mSlideRightView);
    }

    private void lunx() {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        printDebug("轮询");
                    }
                }
                , 1000, 1000
        );
    }

    private void printDebug(String msg) {
        int childCount = getChildCount();
        if (childCount == 0) {
            Log.i("Scroll-" + msg, "没有 ChildView");
        } else {
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                Log.i("Scroll-" + msg, MessageFormat.format("{0} -> {1,number,0}", childAt.getClass().getSimpleName(), childAt.getWidth()));
            }
        }
    }

    private int getSlideLeftWith() {
        return mSlideLeftView == null ? 0 : mSlideLeftView.getWidth();
    }

    private int getSlideRightWith() {
        return mSlideRightView == null ? 0 : mSlideRightView.getWidth();
    }

    /*是否启用侧滑*/
    private boolean isEnableSlide() {
        return getSlideLeftWith() > 0 || getSlideRightWith() > 0;
    }

    private boolean isIntercept() {
        return isIntercept && isEnableSlide();
    }

    /*拦截状态设置*/
    private void intercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
        getParent().requestDisallowInterceptTouchEvent(isIntercept);
    }

    /* 测滑滚动 */
    private void moveSlide(int dx) {
        dx *= 0.8f;
//        moveTranslationToX(getCurrentMoveSlide() + dx);
        moveScrollToX(getScrollX() - dx);
    }

    private void moveScrollToX(int x) {
        int slw = getSlideLeftWith();
        int srw = getSlideRightWith();
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

    /*判断是否需要横向滑动*/
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


    private void logEvent(String tag, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.i("Scroll-" + tag, "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("Scroll-" + tag, "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i("Scroll-" + tag, "ACTION_CANCEL");
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!isEnableSlide()) return super.onInterceptTouchEvent(e);
        logEvent("onInterceptTouchEvent", e);
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
        logEvent("onTouchEvent", e);

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
                    int with = getSlideRightWith();
                    if ((Math.abs(scrollX) > with / 2 && xVelocity < 0) || xVelocity < -MINIMUM_VELOCITY) { // 操过了菜单的一半 或者动作速度达到
                        animScrollerTo(with);
                    } else { //条件不满足 关闭菜单
                        animScrollerTo(0);
                    }
                } else if (scrollX < 0) { //左菜单
                    int with = getSlideLeftWith();
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

    private void release() {
        releaseVelocity();
        releaseAnimScroll();
        moveScrollToX(0);
    }

    /**
     * 关闭菜单
     */
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

    @Override
    public void onScrolling() {
        if (mIsMenuOpened) {
            animScrollerTo(0);
        }
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener listener) {
        mOnMenuStateChangeListener = listener;
    }

    /**
     * 菜单那状态监听
     */
    public interface OnMenuStateChangeListener {
        void onMenuStateChange(SlideFrameLayout layout, boolean isOpen);
    }

}
