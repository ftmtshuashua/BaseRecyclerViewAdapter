package support.lfp.adapter;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import support.lfp.adapter.utils.Utils;

/**
 * <pre>
 * Tip:
 *      RecyclerView item 触摸事件分发
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/10 10:57
 * </pre>
 */
public class RecyclerViewItemTouchHelper extends RecyclerView.SimpleOnItemTouchListener {

    RecyclerView mRecyclerView;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;

    public RecyclerViewItemTouchHelper(RecyclerView recyclerView) {
        Utils.requireNonNull(recyclerView);
        this.mRecyclerView = recyclerView;
        this.mRecyclerView.addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(mRecyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            //长按事件
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                if (mOnItemLongClickListener != null) {
                    View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = mRecyclerView.getChildLayoutPosition(childView);
                        RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(childView);
                        mOnItemLongClickListener.onItemLongClick(viewHolder, position);
                    }
                }
            }

            //单击事件
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemClickListener != null) {
                    View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = mRecyclerView.getChildLayoutPosition(childView);
                        RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(childView);
                        mOnItemClickListener.onItemClick(viewHolder, position);
                        return true;
                    }
                }
                return super.onSingleTapUp(e);
            }
        });

    }

    public void clear() {
        this.mRecyclerView.removeOnItemTouchListener(this);
    }

    boolean disallowIntercept;

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (disallowIntercept) {
            Log.e("不允许拦截事件", "return false");
            return false;
        }
        boolean isGestureDetector = mGestureDetector.onTouchEvent(e);
        Log.e("判断是否拦截事件", "return  " + isGestureDetector);
        return isGestureDetector;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        super.onTouchEvent(rv, e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.onRequestDisallowInterceptTouchEvent(disallowIntercept);
        this.disallowIntercept = disallowIntercept;
    }

    private GestureDetector mGestureDetector;

    /**
     * 当点击Item的时候回调
     */
    public interface OnItemClickListener {
        /**
         * 当点击了Item回调
         *
         * @param viewHolder The view of ViewHolder
         * @param position   The view item
         */
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        mOnItemLongClickListener = l;
    }
}
