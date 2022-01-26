package com.prabhutech.prabhupackages.wallet.core.utils.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.prabhutech.prabhupackages.wallet.core.utils.interfaces.BasicResponseCallback;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    public static final int PERMISSION_REQUEST_CODE = 90;

    /**
     * Ask for a group of permissions at once.
     * @param c context
     * @param permissions list of permissions
     */
    public static void ask(Activity c, String[] permissions, String politeMsg, Integer permissionCode) {
        ArrayList<String> nonPermitted = new ArrayList<>();
        final int pcode = permissionCode == null ? PERMISSION_REQUEST_CODE : permissionCode;
        for (String perm : permissions)
            if (ContextCompat.checkSelfPermission(c, perm) != PackageManager.PERMISSION_GRANTED)
                nonPermitted.add(perm);

        String[] temp = nonPermitted.toArray(new String[nonPermitted.size()]);
        if (politeMsg != null && temp.length != 0) {
            requestDialog(c, politeMsg, new BasicResponseCallback() {
                @Override
                public void onSuccess() {
                    ActivityCompat.requestPermissions(c, temp, pcode);
                }

                @Override
                public void onFailure() {
                    // null
                }
            }).show();
        }
    }

    /**
     * After asking permission check again if any permissions by user were unset.
     * @param c
     * @param permissions
     * @param granted
     * @param leftOut list of left of permissions.
     */
    public static void recheck(Activity c, String[] permissions, int[] granted, OnLeftOut leftOut) {
        final List<String> nonPermitted = new ArrayList<>();
        for (int i = 0; i < granted.length; i++)
            if (granted[i] != PackageManager.PERMISSION_GRANTED)
                if (ActivityCompat.shouldShowRequestPermissionRationale(c, permissions[i]))
                    nonPermitted.add(permissions[i]);

        if (nonPermitted.size() != 0)
            leftOut.leftOut(nonPermitted.toArray(new String[nonPermitted.size()]));
    }



    public interface OnLeftOut {
        void leftOut(String[] permissions);
    }

    /**
     * Asks for permission. If user has already granted then just returns showing nothing.
     * Else the user is shown a rationale dialog. if 'politeMsg' is null then no rationale dialog is
     * shown. Also onSuccess() is only called if permission was already given
     * @param activity
     * @param permission
     * @param politeMsg
     * @param callback return onFailure() if user says no. onSuccess() does not guarantee that user has granted permission.
     */
    public static void ask(Activity activity, String permission, int permissionCode, String politeMsg, BasicResponseCallback callback) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (politeMsg != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) { // bypass testing
                // Show an explanation to the user
                requestDialog(activity, politeMsg, new BasicResponseCallback() {
                    @Override
                    public void onSuccess() {
                        requestPermission(activity, permission, permissionCode);
                    }

                    @Override
                    public void onFailure() {
                        // null
                        callback.onFailure();
                    }
                }).show();
            } else {
                // No explanation needed; request the permission
                requestPermission(activity, permission, permissionCode);
            }
        } else {
            // Permission has already been granted
            callback.onSuccess();
        }

    }

    public static AlertDialog.Builder requestDialog (Context context, String msg, BasicResponseCallback callback) {
        return new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    callback.onSuccess();
                });

    }

    private static void requestPermission (Activity activity, String permission, int permissionCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{ permission },
                permissionCode);
    }

    public static boolean checkPermission(Context context, String[] permission) {
        for (String p : permission)
            if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) return false;
        return true;
    }

    public static boolean isFinallyGranted(int[] granted) {
        // TODO: 9/2/19
        return false;
    }
}
