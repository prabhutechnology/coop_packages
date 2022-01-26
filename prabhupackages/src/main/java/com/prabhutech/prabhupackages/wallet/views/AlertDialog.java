package com.prabhutech.prabhupackages.wallet.views;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.prabhutech.prabhupackages.R;

public class AlertDialog implements DialogInterface {
    private Activity activity;
    private String title, message, positiveText, negativeText;
    private DialogInterface.OnClickListener positiveListener, negativeListener;
    private DialogInterface.OnDismissListener dismissListener;
    private DialogInterface.OnCancelListener cancelListener;
    private int buttonHighlight;
    private TextView tvTitle, tvMessage, tvPositive, tvNegative;
    private boolean cancellable;
    View separator;

    int primaryColor;

    private LayoutInflater inflater;
    Dialog dialog;

    private AlertDialog(Builder builder) {
        this.activity = builder.activity;
        this.title = builder.title;
        this.message = builder.message;
        this.positiveText = builder.positiveText;
        this.negativeText = builder.negativeText;
        this.positiveListener = builder.positiveListener;
        this.negativeListener = builder.negativeListener;
        this.dismissListener = builder.dismissListener;
        this.buttonHighlight = builder.buttonHighlight;
        this.cancellable = builder.cancellable;

        try {
            inflater = (LayoutInflater) builder.activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            primaryColor = this.activity.getResources().getColor(R.color.colorPrimary);
            initDialog();
        } catch (Exception e) {
            Log.e("AlertDialog", "AlertDialog: activity null" );
        }
    }

    private void initDialog() {
        View view = inflater.inflate(R.layout.alert_dialog_message, null);
        dialog = new Dialog(this.activity);

        tvTitle = view.findViewById(R.id.alertTitle);
        tvMessage = view.findViewById(R.id.alertMessage);
        tvPositive = view.findViewById(R.id.positive);
        tvNegative = view.findViewById(R.id.negative);
        separator = view.findViewById(R.id.separator);

        tvTitle.setText(this.title);
        tvMessage.setText(this.message);
        tvPositive.setText(this.positiveText);
        tvNegative.setText(this.negativeText);

        if (buttonHighlight == BUTTON_POSITIVE) {
            tvPositive.setTextColor(primaryColor);
        } else if (buttonHighlight == BUTTON_NEGATIVE) {
            tvNegative.setTextColor(primaryColor);
        }

        if (TextUtils.isEmpty(this.title)) {
            tvTitle.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(this.message)) {
            tvMessage.setVisibility(View.GONE);
        }

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(cancellable);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (positiveListener != null) {
            tvPositive.setOnClickListener(v -> {
                positiveListener.onClick(this, 0);
                dialog.dismiss();
            });
        }

        if (negativeListener != null) {
            tvNegative.setOnClickListener(v -> {
                negativeListener.onClick(this, 0);
                dialog.dismiss();
            });
        } else {
            tvNegative.setOnClickListener(v -> dialog.dismiss());
        }

        if (dismissListener != null) {
            dialog.setOnDismissListener(dismissListener);
            dialog.dismiss();
        }

        if (TextUtils.isEmpty(negativeText)) {
            switchToSingleButtonMode();
        }

        dialog.show();
    }

    private void switchToSingleButtonMode() {
        tvNegative.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);

        if (TextUtils.isEmpty(positiveText)) tvPositive.setText("Ok");
        if (positiveListener == null) tvPositive.setOnClickListener(v -> dialog.dismiss());

    }

    @Override
    public void cancel() {
        dialog.dismiss();
        if (cancelListener != null) cancelListener.onCancel(this);
    }

    @Override
    public void dismiss() {
        if (dismissListener != null) dismissListener.onDismiss(this);
    }
    public static class Builder {
        private Activity activity;
        private String title, message;
        private int buttonHighlight;
        private boolean cancellable = true;
        private String positiveText, negativeText;
        private DialogInterface.OnClickListener positiveListener, negativeListener;
        private DialogInterface.OnDismissListener dismissListener;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(String text, DialogInterface.OnClickListener listener) {
            this.positiveText = text;
            this.positiveListener = listener;
            return this;
        }

        public Builder setNegativeButton(String text, DialogInterface.OnClickListener listener) {
            this.negativeText = text;
            this.negativeListener = listener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
            this.dismissListener = dismissListener;
            return this;
        }

        public Builder setButtonHighlight(int highlight) {
            this.buttonHighlight = highlight;
            return this;
        }

        public AlertDialog show() {
            return new AlertDialog(this);
        }

        public Builder setCancellable(boolean isCancellable) {
            this.cancellable = isCancellable;
            return this;
        }
    }
}
