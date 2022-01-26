package com.prabhutech.prabhupackages.wallet.utils;

import android.util.Log;

import com.prabhutech.prabhupackages.BuildConfig;


public class DebugMode {
    public DebugMode() {
    }

    public DebugMode(String TAG, String message, String type) {
        if (TAG.equals("")) TAG = "DebugMode";
        TAG = "D " + TAG;

        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            switch (type.toLowerCase()) {
                case "d":
                    Log.d(TAG, message);
                    break;
                case "i":
                    Log.i(TAG, message);
                    break;
                case "w":
                    Log.w(TAG, message);
                    break;
                default:
                    Log.e(TAG, message);
                    break;
            }
        }
    }

    public void print(String message) {

        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            message = "D " + message;
            System.out.println(message);
        }
    }

    public void print(int message) {

        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            String msg = "D! " + (message);
            System.out.println(msg);
            System.out.println(message);
        }
    }

    public void showLog(String TAG, String message, String type) {
        if (TAG.equals("")) TAG = "DebugMode";
        TAG = "D " + TAG;

        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            switch (type) {
                case "d":
                    Log.d(TAG, message);
                    break;
                case "i":
                    Log.i(TAG, message);
                    break;
                case "w":
                    Log.w(TAG, message);
                    break;
                default:
                    Log.e(TAG, message);
                    break;
            }
        }
    }

    public void showLog(String TAG, String message) {
        if (TAG.equals("")) TAG = "DebugMode";
        TAG = "D " + TAG;

        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            Log.e(TAG, message);
        }
    }

    public static void log(String TAG, String message) {
        if (TAG.equals("")) TAG = "DebugMode";
        TAG = "D " + TAG;
        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            Log.e(TAG, message);
        }
    }
}
