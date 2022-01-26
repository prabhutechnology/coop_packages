package com.prabhutech.prabhupackages.wallet.core.utils;

import android.app.ProgressDialog;

import com.prabhutech.prabhupackages.R;


public class ProgressDialogUtils {
    public void showProgressDialog(ProgressDialog progressDialog) {
        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void dismissProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }
}
