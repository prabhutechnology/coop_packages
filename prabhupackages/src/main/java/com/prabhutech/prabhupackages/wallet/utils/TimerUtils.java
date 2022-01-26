package com.prabhutech.prabhupackages.wallet.utils;

import android.os.Handler;

public class TimerUtils {
    public static Object setTimeout(Runnable runnable, long delay) {
        return new TimeoutEvent(runnable, delay);
    }

    public static void clearTimeout(Object timeoutEvent) {
        if (timeoutEvent instanceof TimeoutEvent) {
            ((TimeoutEvent) timeoutEvent).cancelTimeout();
        }
    }

    private static class TimeoutEvent {
        private static Handler handler = new Handler();
        private volatile Runnable runnable;

        private TimeoutEvent(Runnable task, long delay) {
            runnable = task;
            handler.postDelayed(() -> {
                if (runnable != null) {
                    runnable.run();
                }
            }, delay);
        }

        private void cancelTimeout() {
            runnable = null;
        }
    }

    public static void executeIntervalEvent(Runnable runnable, long delay) {
        new ExecuteIntervalEvent(runnable, delay);
    }

    private static class ExecuteIntervalEvent {
        private static Handler handler = new Handler();
        private volatile Runnable runnable;

        private ExecuteIntervalEvent(Runnable task, long delay) {
            runnable = task;
            handler.postDelayed(() -> {
                if (runnable != null) {
                    runnable.run();
                    executeIntervalEvent(runnable, delay);
                }
            }, delay);
        }
    }
}
