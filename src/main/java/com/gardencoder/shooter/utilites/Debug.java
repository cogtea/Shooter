package com.gardencoder.shooter.utilites;

import android.util.Log;

import static com.gardencoder.shooter.utilites.Tools.isDebugEnabled;

public class Debug {


    public static void d(String tag, String message) {
        if (isDebugEnabled) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isDebugEnabled) {
            Log.e(tag, message);
        }
    }
}
