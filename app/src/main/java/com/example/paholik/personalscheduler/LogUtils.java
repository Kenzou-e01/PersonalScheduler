package com.example.paholik.personalscheduler;

import android.util.Log;

public abstract class LogUtils {
    private static boolean showLogs = true;

    public static void enableLogs() {
        showLogs = true;
    }

    public static void disableLogs() {
        showLogs = false;
    }

    public static void d(String tag, String msg) {
        if(showLogs) {
            Log.d(tag, msg);
        }
    }
}
