package com.gardencoder.shooter.utilites;

import android.util.Log;

import static com.gardencoder.shooter.utilites.Tools.isDebugEnabled;

/**
 * Created by Ramy on 11/12/16.
 */

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
