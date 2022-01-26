package com.prabhutech.prabhupackages.wallet.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

public class BiometricUtils {
    private static final String TAG = "BiometricUtils";

    public static boolean isBiometricPromptEnabled() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    /**
     * Fingerprint authentication is supported starting from Marshmallow
     */
    public static boolean isSdkVersionSupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * Check if device has fingerprint sensors
     */
    public static boolean isHardwareSupported(Context context) {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);
        return fingerprintManagerCompat.isHardwareDetected();
    }

    /**
     * Check if fingerprint is available for authentication
     */
    public static boolean isFingerprintAvailable(Context context) {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);
        return fingerprintManagerCompat.hasEnrolledFingerprints();
    }

    public static boolean isPermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /*
     * Check above conditions to determine if the app should enable fingerprint,
     * even when requested by the app
     */
    public static boolean shouldUseBiometric(Context context) {

        if (!isSdkVersionSupported()) {
            Log.d(TAG, "SDK version not supported");
            return false;
        }

        if (!isPermissionGranted(context)) {
            Log.d(TAG, "Permission not granted");
            return false;
        }

        if (!isHardwareSupported(context)) {
            Log.d(TAG, "No hardware detected");
            return false;
        }

        if (!isFingerprintAvailable(context)) {
            Log.d(TAG, "No fingerprint registered");
            return false;
        }


        return true;
    }
}
