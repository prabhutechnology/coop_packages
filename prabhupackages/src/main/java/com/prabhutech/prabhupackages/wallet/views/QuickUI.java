package com.prabhutech.prabhupackages.wallet.views;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.prabhutech.prabhupackages.wallet.utils.SimpleCallback;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.core.utils.AppUtils;
import com.prabhutech.prabhupackages.wallet.core.utils.interfaces.BasicResponseCallback;

public class QuickUI {
    public static void errDialog(Context context, String msg) {
        if (context == null) return;
        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.sorry))
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public static void errDialog(Activity activity, String msg) {
        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.sorry))
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public static void errSnack(View root, String msg) {
        final Snackbar snackbar = Snackbar.make(root, msg, Snackbar.LENGTH_LONG);
        // TODO: 8/18/19 customize snackbar
        snackbar.setAction(android.R.string.ok, view -> snackbar.dismiss());
        snackbar.show();
    }

    public static void createDialogTrio(Activity activity, String alertHead, String alertBody) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog_trio, null);
        TextView titleAlert = view.findViewById(R.id.alertTitle);
        TextView bodyAlert = view.findViewById(R.id.alertMessage);
        titleAlert.setText(alertHead);
        bodyAlert.setText(alertBody);
        Dialog dialog = new Dialog(activity);
        Button yes = dialog.findViewById(R.id.alertOkay);
        Button no = dialog.findViewById(R.id.alertCancel);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        yes.setOnClickListener(v -> {

        });
    }

    @Deprecated
    public static void showBasicAlertDialog(Activity activity, String title, String requestMessage) {
        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(requestMessage)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public static void showBasicAlertDialog(Activity activity, String title, String message, boolean isSkippable, SimpleCallback callback) {
        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isSkippable)
                .setPositiveButton(android.R.string.ok, ((dialog, which) -> {
                    dialog.dismiss();
                    callback.call();
                }))
                .show();
    }

    public static void showUpdateDialog(Activity activity, String title, String message, boolean isForced, BasicResponseCallback callback) {
        com.prabhutech.prabhupackages.wallet.views.AlertDialog.Builder builder = new com.prabhutech.prabhupackages.wallet.views.AlertDialog.Builder(activity)
                .setButtonHighlight(com.prabhutech.prabhupackages.wallet.views.AlertDialog.BUTTON_POSITIVE)
                .setTitle(title)
                .setMessage(message);

        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            AppUtils.startAppUpdate(activity);
            callback.onSuccess();
        };
        DialogInterface.OnDismissListener onDismissListener = dialog -> {
            if (!isForced) callback.onFailure();
        };

        if (isForced) {
            builder.setCancellable(false);
            builder.setPositiveButton(activity.getString(R.string.update), onClickListener);
        } else {
            builder.setPositiveButton(activity.getString(R.string.update), onClickListener);
            builder.setNegativeButton(activity.getString(R.string.later), (dialog, which) -> dialog.dismiss());
        }
        builder.setOnDismissListener(onDismissListener);

        builder.show();
    }

    public static void showAlertDialogDualResponse(Activity activity, String title, String message, BasicResponseCallback callback) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    callback.onSuccess();
                    dialogInterface.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, ((dialogInterface, i) -> {
                    callback.onFailure();
                    dialogInterface.dismiss();
                }))
                .show();
    }

    public static void showToast(Activity activity, String message, int len) {
        if (activity!=null && !TextUtils.isEmpty(message)) {
            com.prabhutech.prabhupackages.wallet.views.Toast.show(activity, message);
//            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, String message) {
        if (context!=null && !TextUtils.isEmpty(message))
            com.prabhutech.prabhupackages.wallet.views.Toast.show(context, message);
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*public static void showKycNotFilledAlert(Activity activity) {
        new com.prabhutech.prabhupackages.wallet.views.AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.kyc_not_verified))
                .setMessage(activity.getResources().getString(R.string.note_kyc_not_verified_alert))
                .setPositiveButton(activity.getResources().getString(R.string.update_kyc), (dI, i) -> {

                    if (UserCore.kycStatus == null) {
                        activity.finish();
                    } else if (UserCore.kycStatus.equals("pending")) {
                        activity.startActivity(new Intent(activity, KYCViewDetailsActivity.class));
                    } else {
                        activity.startActivity(new Intent(activity, KYCDetailsRegistrationActivity.class));
                    }

                    activity.finish();
                })
                .setNegativeButton(activity.getResources().getString(R.string.cancel), (dI, i) -> {
                    activity.finish();
                })
                .setButtonHighlight(BUTTON_POSITIVE)
                .setOnDismissListener(dialogInterface -> activity.finish())
                .show();
    }
    public static void showKycNotFilledAlert2(Activity activity) {
        new com.prabhutech.prabhupackages.wallet.views.AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.kyc_not_verified))
                .setMessage(activity.getResources().getString(R.string.note_kyc_not_verified_alert))
                .setPositiveButton(activity.getResources().getString(R.string.update_kyc), (dI, i) -> {

                    if (UserCore.kycStatus == null) {
                        activity.finish();
                    } else if (UserCore.kycStatus.equals("pending")) {
                        activity.startActivity(new Intent(activity, KYCViewDetailsActivity.class));
                    } else {
                        activity.startActivity(new Intent(activity, KYCDetailsRegistrationActivity.class));
                    }

                    activity.finish();
                })
                .setNegativeButton(activity.getResources().getString(R.string.cancel), (dI, i) -> {

                })
                .setButtonHighlight(BUTTON_POSITIVE)
                .show();
    }*/

    public static void showServiceNotAvailable(Activity activity) {
        new com.prabhutech.prabhupackages.wallet.views.AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.sorry))
                .setMessage(activity.getResources().getString(R.string.refer_a_friend_is_currently_not_available))
                .setPositiveButton(activity.getResources().getString(R.string.ok), (dI, i) -> activity.finish())
                .setCancellable(false)
                .setOnDismissListener(dialogInterface -> activity.finish())
                .show();
    }

    public static void showDialog(Activity activity, String message) {
        new com.prabhutech.prabhupackages.wallet.views.AlertDialog.Builder(activity)
                .setTitle(message)
                .setButtonHighlight(com.prabhutech.prabhupackages.wallet.views.AlertDialog.BUTTON_POSITIVE)
                .show();
    }

    public static void showNonConcelableDialog(Activity activity, String message) {
        new com.prabhutech.prabhupackages.wallet.views.AlertDialog.Builder(activity)
                .setTitle(message)
                .setButtonHighlight(com.prabhutech.prabhupackages.wallet.views.AlertDialog.BUTTON_POSITIVE)
                .setCancellable(false)
                .show();
    }
}
