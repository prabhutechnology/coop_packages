package com.prabhutech.prabhupackages.wallet.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.prabhutech.prabhupackages.wallet.views.AlertDialog;
import com.prabhutech.prabhupackages.R;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;

public class ExceptionHandler {
    public static int RETRY = 0;
    public static int CANCEL = 1;

    // when error is supposed to be handled but cannot find the cause of it.
    public static int DISSED = -2;
    // when error is supposed to be handled by whoever comes next.
    public static int NOT_HANDLED = -1;

    public static void handleException(Activity activity, Throwable e, Callback callback) {
        boolean retriable = true;
        String msg = "";
        String title = "";
        if (e instanceof UnknownHostException) {
            showRetryDialog(
                    activity,
                    activity.getResources().getString(R.string.network_error),
                    activity.getResources().getString(R.string.please_check_your_connection_and_try_again),
                    (dialogInterface, i) -> callback.actionId(RETRY),
                    (dialogInterface, i) -> callback.actionId(CANCEL));
        } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
            showRetryDialog(
                    activity,
                    activity.getResources().getString(R.string.something_went_wrong),
                    activity.getResources().getString(R.string.please_try_again),
                    (dialogInterface, i) -> callback.actionId(RETRY),
                    (dialogInterface, i) -> callback.actionId(CANCEL));
        } else if (e instanceof ConnectException) {
            showRetryDialog(
                    activity,
                    activity.getResources().getString(R.string.service_unavailable),
                    activity.getResources().getString(R.string.unable_to_find_service),
                    (dialogInterface, i) -> callback.actionId(RETRY),
                    (dialogInterface, i) -> callback.actionId(CANCEL));
        } else if (e instanceof HttpException) {
            int code = ((HttpException) e).code();
            if (code == 404)
                showRetryDialog(
                        activity,
                        activity.getResources().getString(R.string.service_unavailable),
                        activity.getResources().getString(R.string.unable_to_find_service),
                        (dialogInterface, i) -> callback.actionId(RETRY),
                        (dialogInterface, i) -> callback.actionId(CANCEL));
            else if (code >= 500 && code < 600) { // checking like this may give major head aches in the future.
                showRetryDialog(
                        activity,
                        activity.getResources().getString(R.string.server_error),
                        activity.getResources().getString(R.string.please_try_again_later),
                        (dialogInterface, i) -> callback.actionId(RETRY),
                        (dialogInterface, i) -> callback.actionId(CANCEL));
            } else {
                callback.actionId(NOT_HANDLED);
            }
        } else if (e instanceof EOFException || e instanceof NullPointerException) {
            showRetryDialog(
                    activity,
                    activity.getResources().getString(R.string.server_error),
                    activity.getResources().getString(R.string.please_try_again_later),
                    (dialogInterface, i) -> callback.actionId(RETRY),
                    (dialogInterface, i) -> callback.actionId(CANCEL));
        } else {
            callback.actionId(NOT_HANDLED);
        }

    }

    private static void showRetryDialog(Activity activity, String title, String msg, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(activity.getResources().getString(R.string.retry), positiveListener)
                .setNegativeButton(activity.getResources().getString(R.string.cancel), negativeListener)
                .setButtonHighlight(AlertDialog.BUTTON_POSITIVE)
                .show();
    }

    public interface Callback {
        void actionId(int id);
    }
}
