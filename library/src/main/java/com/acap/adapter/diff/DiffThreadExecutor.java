package com.acap.adapter.diff;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/14 11:20
 * </pre>
 */
public class DiffThreadExecutor {


    public static Executor getMainThreadExecutor() {
        return new MainThreadExecutor();
    }

    public static Executor getDiffThreadExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    public static class MainThreadExecutor implements Executor {

        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(final Runnable command) {
            mHandler.post(command);
        }
    }

}
