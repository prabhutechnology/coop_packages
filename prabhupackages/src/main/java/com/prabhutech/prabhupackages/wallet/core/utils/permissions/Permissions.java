package com.prabhutech.prabhupackages.wallet.core.utils.permissions;

import android.Manifest;

public class Permissions {
    /** Permission id for all requests made for camera */
    public static final int CAMERA_ALL_PERMISSION_REQUEST = 1001;
    /** Permission id for all requests made for gallery */
    public static final int GALLERY_ALL_PERMISSION_REQUEST = 1002;

    public static final int CAMERA_REQUEST = 1003;

    public static final int WRITE_EXTERNAL_STORAGE = 1004;

    public static final int READ_PHONE_STATE=1009;

    public static final String[] ALL = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE
    };
}
