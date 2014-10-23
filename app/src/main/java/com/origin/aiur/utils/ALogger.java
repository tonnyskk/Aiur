package com.origin.aiur.utils;

import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by dongjia on 10/23/2014.
 */
public class ALogger {
    public enum LogPriority {
        error,
        warn,
        info,
        debug
    }

    public static void log(LogPriority p, Class<?> clazz, String msg) {
        log(clazz, msg, null, p);
    }

    public static void log(LogPriority p, Class<?> clazz, String msg, Throwable t) {
        log(clazz, msg, t, p);
    }

    public static void log(LogPriority p, Class<?> clazz, String msg, Object... msgArgs) {
        log(clazz, msg, null, p, msgArgs);
    }

    public static void log(LogPriority p, Class<?> clazz, String msg, Throwable t, Object... msgArgs) {
        log(clazz, msg, t, p, msgArgs);
    }

    private static void log(Class<?> clazz, String msg, Throwable t, LogPriority priority, Object... msgArgs) {
        try {
            if (msg != null && msgArgs != null && msgArgs.length > 0) {
                msg = String.format(msg, msgArgs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        switch (priority) {
            case error:
                Log.e(clazz.getName(), msg, t);
                break;
            case warn:
                Log.w(clazz.getName(), msg, t);
                break;
            case info:
                Log.i(clazz.getName(), msg, t);
                break;
            case debug:
                Log.d(clazz.getName(), msg, t);
                break;
        }

    }
}
