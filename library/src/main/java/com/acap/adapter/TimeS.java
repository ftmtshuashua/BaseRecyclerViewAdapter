package com.acap.adapter;

import android.util.Log;

import java.text.MessageFormat;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/10 17:49
 * </pre>
 */
class TimeS {
    long time;
    int count = 1;

    public TimeS() {
        this.time = System.nanoTime() + 1500;
    }

    public void look() {
        long time1 = System.nanoTime();
        float l = (time1 - time) / 1000000f;
        Log.i("xx-" + count++, MessageFormat.format("耗时:{0,number,0.####}ms", l));
        time = System.nanoTime();
    }
}
