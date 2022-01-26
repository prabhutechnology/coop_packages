package com.prabhutech.prabhupackages.wallet.utils.views;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class DynamicAnimButton {
    private Context context;
    private Button animButton;
    private ProgressBar animProgressBar;

    public DynamicAnimButton(Context context, Button animButton, ProgressBar animProgressBar) {
        this.context = context;
        this.animButton = animButton;
        this.animProgressBar = animProgressBar;
    }

    public void setBusy(boolean busy) {
        if (busy) {
            animButton.setClickable(false);
            animButton.setFocusable(false);
            animProgressBar.setVisibility(View.VISIBLE);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                animButton.setClickable(true);
                animButton.setFocusable(true);
                animProgressBar.setVisibility(View.GONE);
            }, 2000);
        }
    }

    public void setButtonText(String buttonText) {
        animButton.setText(buttonText);
    }

    public void setVisibility(int visibility) {
        animButton.setVisibility(visibility);
        animProgressBar.setVisibility(visibility);
    }
}
