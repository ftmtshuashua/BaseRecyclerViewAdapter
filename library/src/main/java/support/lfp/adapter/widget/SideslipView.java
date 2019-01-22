package support.lfp.adapter.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * <pre>
 * Tip:
 *      侧滑菜单实现
 *
 * Function:
 *
 * Created by LiFuPing on 2019/1/22 10:49
 * </pre>
 */
public class SideslipView extends FrameLayout {
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mOverscrollDistance;
    private int mOverflingDistance;

    private float mHorizontalScrollFactor;


    public SideslipView(Context context) {
        this(context, null);
    }

    public SideslipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideslipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initScrollView();
    }

/*

    private void initScrollView() {
        mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mOverflingDistance = configuration.getScaledOverflingDistance();
//        mHorizontalScrollFactor = configuration.getScaledHorizontalScrollFactor();

        scrollTo(50, 0);
    }


    private OverScroller mScroller;
    float mTouch_X_Old;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("" , "dispatchTouchEvent："+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("" , "onInterceptTouchEvent："+ev.getAction());
        super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("" , "onTouchEvent："+ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: //0
                mTouch_X_Old = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE://2
                float x = ev.getX();
                smoothScrollBy((int) (x - mTouch_X_Old));
                mTouch_X_Old = x;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
//        return super.onTouchEvent(ev);
        return true;
    }


    public final void smoothScrollBy(int dx) {
        Log.e("" , "滚动："+dx);
        mScroller.startScroll(mScroller.getCurrX(), 0, dx, 0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) { // 滚动未完成
            Log.e("滚动：", "CurrX:" + mScroller.getCurrX());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }
*/
}
