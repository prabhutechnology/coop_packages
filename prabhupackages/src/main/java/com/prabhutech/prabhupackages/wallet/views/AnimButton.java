package com.prabhutech.prabhupackages.wallet.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prabhutech.prabhupackages.R;


public class AnimButton extends FrameLayout implements View.OnClickListener {
    private Context context;

    private Button button;
    private ProgressBar progressBar;
    private String text = "Button";

    private OnClickListener onClickListener;

    public AnimButton(@NonNull Context context) {
        super(context);
    }

    public AnimButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_anim_button, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimButton, 0, 0);
        text = a.getString(R.styleable.AnimButton_text);
        a.recycle();

        button = view.findViewById(R.id.anim_button);
        progressBar = view.findViewById(R.id.anim_progressBar);

        setText(text);

        button.setOnClickListener(this);
        progressBar.setVisibility(GONE);
    }

    public void setText(String text) {
        button.setText(text);
    }

    public void setBackground(Drawable color) {
        button.setBackground(color);
    }

    public void setTextColor(int color) {
        button.setTextColor(color);
    }

    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (onClickListener != null) onClickListener.onClick(this);
    }

    public void setBusy(boolean busy) {
        if (busy) {
            button.setClickable(false);
            button.setFocusable(false);
            button.setTextColor(getResources().getColor(R.color.white));
            progressBar.setVisibility(VISIBLE);
            if (context != null)
                progressBar.setAnimation(com.prabhutech.prabhupackages.wallet.views.Balloon.up(context));
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                button.setClickable(true);
                button.setFocusable(true);
                button.setTextColor(getResources().getColor(R.color.white));
                progressBar.setVisibility(GONE);
                if (context != null)
                    progressBar.setAnimation(com.prabhutech.prabhupackages.wallet.views.Balloon.down(context));
            }, 2000);
        }
    }
}
