package com.prabhutech.prabhupackages.wallet.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;

import com.prabhutech.prabhupackages.wallet.core.utils.permissions.PermissionManager;

public class AppUtils {
    /**
     * Checks if the given version matches the app's version.
     * @return
     */
    public static boolean currentVersionMatched(Context context, int versionCode) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return versionCode == packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void startAppUpdate(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            // TODO: 11/6/19 handle if google play store is not available
            // TODO: 11/6/19 handle focusing on google play store only
            i.setData(Uri.parse("market://details?id=" + appPackageName));
            activity.startActivity(i);
        } catch (android.content.ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void rateApp(Activity activity) {
        // TODO: 11/4/19
    }

    /**
     * Shake that phone shakira style.
     * @param context
     */
    public static void shakira(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            shakeItUp(context, VibrationEffect.createOneShot(100, 20));
    }

    /**
     * Shake that phone ricky martin style.
     * @param context
     */
    public static void rickyMartin(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            shakeItUp(context, VibrationEffect.createWaveform(new long[] { 100L, 200L }, 2));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void shakeItUp(Context context, VibrationEffect effect) {
        try {
            if (PermissionManager.checkPermission(context, new String[]{Manifest.permission.VIBRATE})) {
                Vibrator vibrator = ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(effect);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
