package lfp.support.adapter.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/13 15:50
 * </pre>
 */
public class DefaultItemDecoration extends RecyclerView.ItemDecoration {

    private int divider;

    public DefaultItemDecoration(Context context) {
        divider = dip2px(context, 4);
    }

    public DefaultItemDecoration(int divider) {
        this.divider = divider;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        outRect.bottom = divider;
        if (position == 0) {
            outRect.top = divider;
        }
    }

    private static int dip2px(Context context, float dp) {
        return (int) applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        return TypedValue.applyDimension(unit, value, metrics);
    }

}
